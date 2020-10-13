import {
	EditOutlined,
	EllipsisOutlined,
	FieldTimeOutlined,
	MoneyCollectOutlined,
	SettingOutlined,
	DollarCircleFilled
} from '@ant-design/icons';
import { Card, Col, Row, Statistic } from 'antd';
import Meta from 'antd/lib/card/Meta';
import moment from 'moment';
import React, { useEffect } from 'react';
import { useSelector } from 'react-redux';
import { getBalace } from '../../services/get-balace';
import CustomAvatar from '../CustomAvatar';
import WallTransferList from '../WallTransferList';
import Ravatar from 'react-avatar';
import './Wallet.css';

function UserWallet(props) {
	const wallet = useSelector((state) => state.wallet);

	const user = useSelector((state) => state.user);

	useEffect(() => {
		getBalace();
	}, []);
	return (
		<Card bordered={false} className="wallet-info">
			{/* <div className="profile">FooApp Wallet</div> */}
			<div style={{ width: '100%', margin: '0 10px' }}>
				{/* <div className="wallet-user">
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
				<WallTransferList /> */}
			</div>

			<div className="wallet-user">
				<Ravatar name={user.name} className="custom-avatar" size="90" />
				<h5 className="wallet-username">{user.name}</h5>
			</div>
			<div className="wallet-balance-card">
				<div className="wallet-balance">
					<div className="wallet-balance-icon">
						<DollarCircleFilled style={{ color: '#fff', fontSize: '30px' }} />
					</div>
					<p className="wallet-balance-title">Số dư</p>
					<h3 className="wallet-balance-number">
						<Statistic
							className="wallet-balance"
							value={wallet.balance}
							precision={0}
							valueStyle={{ color: '#3f8600' }}
							suffix="VND"
						/>
					</h3>
				</div>
				<div className="wallet-time">
					{/* <Statistic
						style={{ fontSize: '10px' }}
						value={moment(wallet.lastUpdated * 1000).format('hh:mm A - DD/MM/YYYY')}
						precision={0}
						valueStyle={{ color: '#C97064' }}
						prefix={}
					/> */}
					<span>
						<FieldTimeOutlined />
						{'   '}
						{moment(wallet.lastUpdated * 1000).format('hh:mm A - DD/MM/YYYY')}
					</span>
				</div>
			</div>
			{/* <Row justify="center">
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
			</Row> */}
		</Card>
		// <div className="wallet-info">

		// </div>
	);
}

export default UserWallet;
