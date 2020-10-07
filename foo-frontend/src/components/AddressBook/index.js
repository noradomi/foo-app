import { Button, List, message, Spin } from 'antd';
import React, { useEffect, useState, useRef } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import reqwest from 'reqwest';
import CustomAvatar from '../CustomAvatar';
import grpcApi from '../../services/grpcApi';
import './AddressBook.css';
import TransactionHistoryItem from '../TransactionHistoryItem';
import { ArrowRightOutlined, ArrowLeftOutlined } from '@ant-design/icons';
import { connect, useSelector } from 'react-redux';
import { loadTransactionHistory } from '../../services/loadTransactionHistory';

const fakeDataUrl = 'https://randomuser.me/api/?results=20&inc=name,gender,email,nat&noinfo';

function AddressBook(props) {
	const [ loading, setLoading ] = useState(false);
	const [ hasMore, setHasMore ] = useState(true);
	let nextPageToken = useRef(0);

	useEffect(() => {
		loadTransactionHistory(20, 0).then((x) => {
			nextPageToken.current = x;
			if (x === 0) {
				message.info('Bạn chưa có lịch sử giao dịch nào');
				setLoading(false);
				setHasMore(false);
			} else {
				setLoading(false);
			}
		});
	}, []);

	let data = props.transactionHistory;

	const handleInfiniteOnLoad = () => {
		loadTransactionHistory(20, nextPageToken.current).then((x) => {
			nextPageToken.current = x;
			if (x === 0) {
				message.info('Đã tải hết lịch sử giao dịch');
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
				loadMore={handleInfiniteOnLoad}
				hasMore={!loading && hasMore}
				useWindow={false}
			>
				<List
					dataSource={data}
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
				</List>
			</InfiniteScroll>
		</div>
	);
}

let mapStateToProps = (state) => {
	return {
		transactionHistory: state.transactionHistory
	};
};

export default connect(mapStateToProps, null)(AddressBook);
