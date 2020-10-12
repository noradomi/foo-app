import {
	EditOutlined,
	EllipsisOutlined,
	FieldTimeOutlined,
	MoneyCollectOutlined,
	SettingOutlined
} from '@ant-design/icons';
import { Card, Col, Row, Statistic } from 'antd';
import Avatar from 'antd/lib/avatar/avatar';
import Meta from 'antd/lib/card/Meta';
import moment from 'moment';
import React, { useEffect } from 'react';
import { useSelector } from 'react-redux';
import { getBalace } from '../../services/get-balace';
import CustomAvatar from '../CustomAvatar';
import WallTransferList from '../WallTransferList';
import './Wallet.css';

function UserWallet(props) {
	const wallet = useSelector((state) => state.wallet);

	const user = useSelector((state) => state.user);

	useEffect(() => {
		getBalace();
	}, []);
	return (
		<div className="wallet-info">
			<div className="profile">FooApp Wallet</div>

			{/* <Card
				style={{ width: 300 }}
				cover={<img alt="example" src="https://gw.alipayobjects.com/zos/rmsportal/JiqGstEfoWAOHiTxclqi.png" />}
				actions={[
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
							value={moment(wallet.lastUpdated * 1000).format('hh:mm A - DD/MM/YYYY')}
							precision={0}
							valueStyle={{ color: '#C97064' }}
							prefix={<FieldTimeOutlined />}
							// suffix="%"
						/>
					</Card> 
				]}
			>
				<Meta
					avatar={<Avatar src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png" />}
					title="Card title"
					description="This is the description"
				/>
			</Card> */}
			<div style={{ width: '100%' }}>
				<div className="wallet-user">
					<CustomAvatar type="user-avatar" avatar={user.name} />
					<h5 className="wallet-username">{user.name}</h5>
				</div>
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
								value={moment(wallet.lastUpdated * 1000).format('hh:mm A - DD/MM/YYYY')}
								precision={0}
								valueStyle={{ color: '#C97064' }}
								prefix={<FieldTimeOutlined />}
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
