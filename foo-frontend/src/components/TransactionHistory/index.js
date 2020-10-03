import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { List } from 'antd/lib/form/Form';
import grpcApi from '../../services/grpcApi';
import Avatar from 'antd/lib/avatar/avatar';
import { Table } from 'antd';

TransactionHistory.propTypes = {};

function TransactionHistory(props) {
	const columns = [
		{
			title: 'UserId',
			dataIndex: 'userId',
			width: 350
		},
		{
			title: 'Amount',
			dataIndex: 'amount',
			width: 150
		},

		{
			title: 'Description',
			dataIndex: 'description',
			width: 350
		},
		{
			title: 'Time',
			dataIndex: 'recordedTime'
		}
	];

	const [ data, setData ] = useState([]);
	useEffect(() => {
		grpcApi.getHistory((err, response) => {
			const history = response.getData().getItemsList();

			if (history.length !== 0) {
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
			}
		});
	}, []);

	return (
		// <List
		// 		itemLayout="horizontal"
		// 		dataSource={data}
		// 		renderItem={item => (
		// 			<List.Item>
		// 				<List.Item.Meta
		// 					avatar={<Avatar src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png" />}
		// 					title={<a href="https://ant.design">{item.userId}</a>}
		// 					description="aaaa"
		// 				/>
		// 			</List.Item>
		// 		)}
		// 	/>
		<div className="transaction-history">
			<div className="chat-header">
				<h2>Transaction History</h2>
			</div>
			<Table columns={columns} dataSource={data} scroll={{ y: 500 }} />
		</div>
	);
}

export default TransactionHistory;
