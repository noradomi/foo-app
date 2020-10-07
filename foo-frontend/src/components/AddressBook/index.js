import { Button, List, message, Spin } from 'antd';
import React, { useEffect, useState, useRef } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import reqwest from 'reqwest';
import CustomAvatar from '../CustomAvatar';
import grpcApi from '../../services/grpcApi';
import './AddressBook.css';
import TransactionHistoryItem from '../TransactionHistoryItem';
import { ArrowRightOutlined, ArrowLeftOutlined } from '@ant-design/icons';
import { useSelector } from 'react-redux';
import { loadTransactionHistory } from '../../services/loadTransactionHistory';

const fakeDataUrl = 'https://randomuser.me/api/?results=20&inc=name,gender,email,nat&noinfo';

function AddressBook(props) {
	// const data = useSelector((state) => state.transactionHistory);
	const [ data, setData ] = useState([]);
	const [ loading, setLoading ] = useState(false);
	const [ hasMore, setHasMore ] = useState(true);
	let nextPageToken = useRef(0);

	useEffect(() => {
		grpcApi.getHistory(20, 0, (err, response) => {
			const history = response.getData().getItemsList();
			nextPageToken.current = response.getData().getNextPageToken();
			console.log('next page token: ' + nextPageToken.current);
			if (nextPageToken.current === 0) {
				message.warning('Lịch sử giao dịch rỗng');
				setLoading(false);
				setHasMore(false);
				return;
			} else {
				const items = [];
				history.forEach((x) => {
					console.log(x.getUserId());
					const item = {
						userId: x.getUserId(),
						description: x.getDescription(),
						amount: x.getAmount(),
						recordedTime: x.getRecordedTime(),
						transferType: x.getTransferType()
					};
					items.push(item);
				});
				setData(items);
				setLoading(false);
			}
		});
		// loadTransactionHistory(20, 0).then((x) => {
		// 	nextPageToken.current = x;
		// 	console.log('Nex page token: ' + nextPageToken.current);
		// 	if (x === 0) {
		// 		message.warning('Lịch sử giao dịch rỗng');
		// 		setLoading(false);
		// 		setHasMore(false);
		// 	} else {
		// 		setLoading(false);
		// 	}
		// });
	}, []);

	const handleInfiniteOnLoad = () => {
		grpcApi.getHistory(5, nextPageToken.current, (err, response) => {
			const history = response.getData().getItemsList();
			nextPageToken.current = response.getData().getNextPageToken();
			console.log('next page token: ' + nextPageToken.current);
			if (nextPageToken.current === 0) {
				message.warning('Đã tải hết lịch sử giao dịch');
				setLoading(false);
				setHasMore(false);
				return;
			} else {
				let newData = data;
				const items = [];
				let i = 0;
				history.forEach((x) => {
					console.log(x.getUserId());
					const item = {
						key: i,
						userId: x.getUserId(),
						description: x.getDescription(),
						amount: x.getAmount(),
						recordedTime: x.getRecordedTime(),
						transferType: x.getTransferType()
					};
					items.push(item);
					i++;
				});
				console.log('Transfer types: ');
				items.forEach((x) => console.log(x.transferType));
				newData = newData.concat(items);
				setData(newData);
				setLoading(false);
			}
		});
		// loadTransactionHistory(20, nextPageToken.current).then((x) => {
		// 	nextPageToken.current = x;
		// 	console.log('Nex page token: ' + nextPageToken.current);
		// 	if (x === 0) {
		// 		message.warning('Đã tải hết lịch sử giao dịch');
		// 		setLoading(false);
		// 		setHasMore(false);
		// 	} else {
		// 		setLoading(false);
		// 	}
		// });
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

export default AddressBook;
