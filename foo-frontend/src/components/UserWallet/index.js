import { FieldTimeOutlined, MoneyCollectOutlined } from '@ant-design/icons';
import { Card, Col, Row, Statistic } from 'antd';
import moment from 'moment';
import React, { useEffect, useState } from 'react';
import { useSelector } from 'react-redux';
import grpcApi from '../../services/grpcApi';
import CustomAvatar from '../CustomAvatar';
import WallTransferList from '../WallTransferList';
import './Wallet.css';
import { getBalace } from '../../services/get-balace';

function UserWallet(props) {
	const wallet = useSelector((state) => state.wallet);

	const user = useSelector((state) => state.user);

	useEffect(() => {
		getBalace();
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
								title="Số dư"
								value={wallet.balance}
								precision={0}
								valueStyle={{ color: '#3f8600' }}
								prefix={<MoneyCollectOutlined />}
								suffix="VND"
							/>
							<Statistic
								className="wallet-last-updated"
								title="Lần cập nhật cuối"
								value={moment(wallet.lastUpdated * 1000).format('HH:MM - DD/MM/YYYY')}
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
