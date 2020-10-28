import { DollarCircleFilled, FieldTimeOutlined } from '@ant-design/icons';
import { Card, Statistic } from 'antd';
import moment from 'moment';
import React, { useEffect } from 'react';
import Ravatar from 'react-avatar';
import { useSelector } from 'react-redux';
import { getBalace } from '../../services/get-balace';
import './Wallet.css';
import { processUsernameForAvatar } from '../../utils/utils';

function UserWallet(props) {
	const wallet = useSelector((state) => state.wallet);
	const user = useSelector((state) => state.user);

	useEffect(() => {
		getBalace();
	}, []);

	return (
		<Card bordered={false} className="wallet-info">
			<div className="wallet-user">
				<Ravatar name={processUsernameForAvatar(user.name)} className="custom-avatar" size="90" />
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
					<span>
						<FieldTimeOutlined />
						{'   '}
						{moment(wallet.lastUpdated * 1000).format('hh:mm A - DD/MM/YYYY')}
					</span>
				</div>
			</div>
		</Card>
	);
}

export default UserWallet;
