import { CalendarOutlined, CaretRightOutlined, FieldTimeOutlined } from '@ant-design/icons';
import { Badge, Collapse, List, message } from 'antd';
import moment from 'moment';
import React, { useEffect, useRef, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { useSelector } from 'react-redux';
import { loadTransactionHistory } from '../../services/load-transaction-history';
import { processUsernameForAvatar } from '../../utils/utils';
import CustomAvatar from '../CustomAvatar';
import './TransactionHistoryList.css';

const { Panel } = Collapse;

function TransactionHistoryList(props) {
	const [ loading, setLoading ] = useState(false);
	const [ hasMore, setHasMore ] = useState(true);
	let nextPageToken = useRef(0);

	let data = useSelector((state) => state.transactionHistory);

	useEffect(() => {
		loadTransactionHistory(20, 0).then((x) => {
			nextPageToken.current = x;
			if (x === 0) {
				message.warning('Bạn chưa có lịch sử giao dịch nào');
				setLoading(false);
				setHasMore(false);
			} else {
				setLoading(false);
			}
		});
	}, []);

	let groups = data.reduce(function(r, o) {
		var date = moment(o.recordedTime * 1000).format('YYYY-MM-DD');
		var m = date.split('-')[1];
		var y = date.split('-')[0];
		r[m] ? r[m].data.push(o) : (r[m] = { group: `Tháng ${m}/${y}`, data: [ o ] });
		return r;
	}, {});

	var result = Object.keys(groups).map(function(k) {
		return groups[k];
	});

	let i = 1;
	result.forEach((x) => {
		x.key = i;
		i++;
	});

	const handleInfiniteOnLoad = () => {
		loadTransactionHistory(20, nextPageToken.current).then((x) => {
			nextPageToken.current = x;
			if (x === 0) {
				message.warning('Đã tải hết lịch sử giao dịch');
				setLoading(false);
				setHasMore(false);
			} else {
				setLoading(false);
			}
		});
	};

	return (
		<div className="demo-infinite-container">
			<InfiniteScroll
				initialLoad={false}
				pageStart={0}
				loadMore={() => {
					handleInfiniteOnLoad();
				}}
				hasMore={!loading && hasMore}
				useWindow={false}
			>
				<Collapse
					expandIcon={({ isActive }) => <CaretRightOutlined rotate={isActive ? 90 : 0} />}
					bordered={false}
					defaultActiveKey={[ '1' ]}
					expandIconPosition={'right'}
				>
					{result.map((x) => (
						<Panel
							header={
								<div className="panel-header">
									<CalendarOutlined style={{ color: '#0068ff', marginRight: '8px' }} /> {x.group}
								</div>
							}
							key={x.key}
						>
							<List
								itemLayout="horizontal"
								dataSource={x.data}
								renderItem={(item) => (
									<Badge.Ribbon
										text={
											<span>{item.transferType === 0 ? 'Chuyển tiền đến' : 'Nhận tiền từ'}</span>
										}
										placement="start"
										color={item.transferType !== 0 ? 'green' : 'blue'}
									>
										<div style={{ paddingTop: '13px' }} />
										<List.Item className="transaction-history-item">
											<List.Item.Meta
												avatar={
													<CustomAvatar
														type="panel-avatar"
														avatar={processUsernameForAvatar(item.userName)}
													/>
												}
												title={
													<div>
														<span
															style={{
																marginRight: '20px',
																fontWeight: 600,
																fontSize: '16px'
															}}
														>
															{item.userName}
														</span>
														<span style={{ color: 'rgba(0, 0, 0, 0.45)' }}>
															<FieldTimeOutlined style={{ marginRight: '5px' }} />
															{moment(item.recordedTime * 1000).format(
																'hh:mm A - DD/MM/YYYY'
															)}
														</span>
													</div>
												}
												description={item.description}
											/>
											{item.transferType === 0 ? (
												<div style={{ color: '#008dfa', fontWeight: 600, marginRight: '20px' }}>
													- {`${item.amount}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')} VND
												</div>
											) : (
												<div style={{ color: 'green', fontWeight: 600, marginRight: '20px' }}>
													+ {`${item.amount}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')} VND
												</div>
											)}
										</List.Item>
									</Badge.Ribbon>
								)}
							/>
						</Panel>
					))}
				</Collapse>
			</InfiniteScroll>
		</div>
	);
}

export default TransactionHistoryList;
