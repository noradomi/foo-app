import { Button, List, message, Spin } from 'antd';
import React, { useEffect, useState, useRef } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import reqwest from 'reqwest';
import CustomAvatar from '../CustomAvatar';
import grpcApi from '../../services/grpcApi';
import './AddressBook.css';
import TransactionHistoryItem from '../TransactionHistoryItem';

const fakeDataUrl = 'https://randomuser.me/api/?results=20&inc=name,gender,email,nat&noinfo';

function AddressBook(props) {
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
				message.warning('Transaction history: No data');
				setLoading(false);
				setHasMore(false);
				return;
			} else {
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

				setData(items);
				setLoading(false);
			}
		});
	}, []);

	// useEffect(() => {
	// 	fetchData((res) => {
	// 		setData(res.results);
	// 	});
	// }, []);

	// const fetchData = (callback) => {
	// 	reqwest({
	// 		url: fakeDataUrl,
	// 		type: 'json',
	// 		method: 'get',
	// 		contentType: 'application/json',
	// 		success: (res) => {
	// 			callback(res);
	// 		}
	// 	});
	// };

	const handleInfiniteOnLoad = () => {
		grpcApi.getHistory(5, nextPageToken.current, (err, response) => {
			const history = response.getData().getItemsList();
			nextPageToken.current = response.getData().getNextPageToken();
			console.log('next page token: ' + nextPageToken.current);
			if (nextPageToken.current === 0) {
				message.warning('Transacion history loaded all');
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
							{/* <List.Item.Meta
									avatar={
										<Avatar src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png" />
									}
									title={<a href="https://ant.design">{item.name.last}</a>}
									description={item.email}
								/> */}
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
