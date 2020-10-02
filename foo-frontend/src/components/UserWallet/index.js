import { FieldTimeOutlined, MoneyCollectOutlined } from '@ant-design/icons';
import { Card, Col, Row, Statistic } from 'antd';
import React, { useEffect, useState } from 'react';
import grpcApi from '../../services/grpcApi';
import CustomAvatar from '../CustomAvatar';

function UserWallet(props) {
	const [ wallet, setWallet ] = useState({
		balance: 0,
		lastUpdated: 0
	});

	useEffect(() => {
		grpcApi.getBalance('Phuc', (err, response) => {
			const dataResponse = response.getData();
			const balance = dataResponse.getBalance();
			const lastUpdated = dataResponse.getLastUpdated();
			setWallet({ balance, lastUpdated });
		});
		grpcApi.getHistory((err, response) => {
			const rs = response.getData().getHistoriesList();
			rs.forEach((x) => {
				console.log(x.getDescription());
			});
		});
	}, []);
	return (
		<div className="conversation-list">
			<div className="profile">FooApp Wallet</div>
			<div style={{ margin: '0 auto', textAlign: 'center' }}>
				<CustomAvatar type="user-avatar" avatar={'Noradomi'} />
				<p>Noradomi</p>
			</div>
			<div style={{ width: '100%' }}>
				<Row justify="center">
					<Col>
						<Card>
							<Statistic
								title="Balance"
								value={wallet.balance}
								precision={0}
								valueStyle={{ color: '#3f8600' }}
								prefix={<MoneyCollectOutlined />}
								suffix="VND"
							/>
						</Card>
					</Col>
				</Row>
				<Row justify="center">
					<Col>
						<Card>
							<Statistic
								title="Last updated"
								value={wallet.lastUpdated}
								precision={0}
								valueStyle={{ color: '#cf1322' }}
								prefix={<FieldTimeOutlined />}
								// suffix="%"
							/>
						</Card>
					</Col>
				</Row>
			</div>
		</div>
	);
}

export default UserWallet;
