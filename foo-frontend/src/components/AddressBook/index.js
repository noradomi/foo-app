import { Button, List, message, Spin } from 'antd';
import React, { useEffect, useState, useRef } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import reqwest from 'reqwest';
import CustomAvatar from '../CustomAvatar';
import grpcApi from '../../services/grpcApi';
import './AddressBook.css';

const fakeDataUrl = 'https://randomuser.me/api/?results=20&inc=name,gender,email,nat&noinfo';

function AddressBook(props) {
	const [ data, setData ] = useState([]);
	const [ loading, setLoading ] = useState(false);
	const [ hasMore, setHasMore ] = useState(true);
	let nextPageToken = useRef(0);

	useEffect(() => {
		grpcApi.getHistory(5, 0, (err, response) => {
			const history = response.getData().getItemsList();
			nextPageToken.current = response.getData().getNextPageToken();
			if (nextPageToken.current === 0) {
				message.warning('Infinite List loaded all');
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
						recordedTime: x.getRecordedTime()
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
		grpcApi.getHistory(5, 0, (err, response) => {
			const history = response.getData().getItemsList();
			nextPageToken.current = response.getData().getNextPageToken();
			if (nextPageToken.current === 0) {
				message.warning('Infinite List loaded all');
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
						recordedTime: x.getRecordedTime()
					};
					items.push(item);
					i++;
				});
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
				{/* <List
						dataSource={this.state.data}
						renderItem={(item) => (
							<List.Item
								key={item.id}
								actions={[
									<Button type="primary" shape="circle" icon={<DownloadOutlined />} size={'large'} />
								]}
							>
								<List.Item.Meta
									avatar={
										<Avatar src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png" />
									}
									title={<a href="https://ant.design">{item.name.last}</a>}
									// description={item.email}
								/>
					
							</List.Item>
						)}
					>
						{this.state.loading &&
						this.state.hasMore && (
							<div className="demo-loading-container">
								<Spin />
							</div>
						)}
					</List> */}
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
							<div className="addfriend-list-item" style={{ width: '100%' }}>
								<div style={{ width: 40 }}>
									<CustomAvatar type="panel-avatar" avatar={'Noradomi'} />
								</div>
								<div className="addfriend-info" style={{ overflow: 'hidden', paddingTop: 5 }}>
									<div className="addfriend-name">Vo Trong Phyc</div>
								</div>
								<Button
									// onClick={() => setVisible(true)}
									// style={{ display: btnVisbale }}
									className="addfriend-btn"
									size={'small'}
									// loading={loadings[0]}
								>
									<span>Transfer</span>
								</Button>
							</div>
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
