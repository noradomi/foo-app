import {
	CalendarOutlined,
	CaretRightOutlined,
	CaretRightFilled,
	InfoOutlined,
	ArrowRightOutlined
} from '@ant-design/icons';
import { Collapse, List, message, Spin, Badge, Skeleton, Tooltip, Button } from 'antd';
import moment from 'moment';
import React, { useEffect, useRef, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect, useSelector } from 'react-redux';
import { loadTransactionHistory } from '../../services/load-transaction-history';
import TransactionHistoryItem from '../TransactionHistoryItem';
import './AddressBook.css';
import Avatar from 'antd/lib/avatar/avatar';
import CustomAvatar from '../CustomAvatar';

const { Panel } = Collapse;

function AddressBook(props) {
	const [ loading, setLoading ] = useState(false);
	const [ hasMore, setHasMore ] = useState(true);
	let nextPageToken = useRef(0);

	let data = useSelector((state) => state.transactionHistory);
	console.log('Data: ', data);

	useEffect(() => {
		loadTransactionHistory(20, 0).then((x) => {
			console.log(' next token: ', x);
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
			console.log(' next token: ', x);
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
					console.log('Load more');
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
							{/* <List
								dataSource={x.data}
								renderItem={(item) => (
									<List.Item key={item.id}>
										<TransactionHistoryItem data={item} />
									</List.Item>
								)}
							>
								{loading &&
								hasMore && (
									<div className="demo-loading-container">
										<Spin />
									</div>
								)}
							</List> */}
							<List
								itemLayout="horizontal"
								dataSource={x.data}
								renderItem={(item) => (
									<Badge.Ribbon text={<span>Chuyển tiền đến</span>} placement="start">
										<div style={{ paddingTop: '18px' }} />
										<List.Item
											actions={[
												<Tooltip placement="topLeft" title={'Chi tiết'}>
													<Button
														className="addfriend-btn"
														size={'small'}
														shape="circle"
														icon={<InfoOutlined />}
													/>
												</Tooltip>
											]}
											className="transaction-history-item"
										>
											<List.Item.Meta
												avatar={<CustomAvatar type="panel-avatar" avatar={item.userName} />}
												title={
													<a href="https://ant.design">
														{item.userName}
														{moment(item.recordedTime * 1000).format(
															'hh:mm A - DD/MM/YYYY'
														)}
													</a>
												}
												description={item.description}
											/>
											<div>{`${item.amount}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')} VND</div>
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

export default AddressBook;
