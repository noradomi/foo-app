import { FieldTimeOutlined, MoneyCollectOutlined } from '@ant-design/icons';
import { Card, Col, Row, Statistic } from 'antd';
import moment from 'moment';
import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import grpcApi from '../../services/grpcApi';
import CustomAvatar from '../CustomAvatar';
import WallTransferList from '../WallTransferList';
import './Wallet.css';

function UserWallet(props) {
	const [ wallet, setWallet ] = useState({
		balance: 0,
		lastUpdated: 0
	});

	const user = useSelector((state) => state.user);

	useEffect(() => {
		grpcApi.getBalance('Phuc', (err, response) => {
			const dataResponse = response.getData();
			const balance = dataResponse.getBalance();
			const lastUpdated = dataResponse.getLastUpdated();
			setWallet({ balance, lastUpdated });
		});
	}, []);
	return (
		<div className="wallet-info">
			<div className="profile">FooApp Wallet</div>
			<div className="wallet-user">
				<CustomAvatar type="user-avatar" avatar={'Noradomi'} />
				<h5 className="wallet-username">{user.name}</h5>
			</div>
			<div style={{ width: '100%' }}>
				<Row justify="center">
					<Col>
						<Card className="wallet-card">
							<Statistic
								className="wallet-balance"
								title="Balance"
								value={wallet.balance}
								precision={0}
								valueStyle={{ color: '#3f8600' }}
								prefix={<MoneyCollectOutlined />}
								suffix="VND"
							/>
							{/* <Input.Password
								className="wallet-balance"
								prefix={<MoneyCollectOutlined />}
								suffix="RMB"
								value={`${wallet.balance} VND`}
								iconRender={(visible) => (visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />)}
								readOnly
								bordered={false}
							/> */}
							<Statistic
								className="wallet-last-updated"
								title="Last updated"
								value={moment(wallet.lastUpdated * 1000).format('hh:mm A - DD/MM/YYYY')}
								precision={0}
								valueStyle={{ color: '#cf1322' }}
								prefix={<FieldTimeOutlined />}
								// suffix="%"
							/>
						</Card>
					</Col>
				</Row>
				<WallTransferList />
			</div>
		</div>
	);
}

export default UserWallet;
