import { RightCircleFilled, LeftCircleFilled, ArrowLeftOutlined, ArrowRightOutlined } from '@ant-design/icons';
import moment from 'moment';
import React from 'react';
import CustomAvatar from '../CustomAvatar';
import './TransactionHistoryItem.css';
import { Row, Col, Button } from 'antd';

function TransactionHistoryItem(props) {
	const { userId, description, recordedTime, amount, transferType } = props.data;

	const formattedAmount = `${amount}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',');

	const cssClass = transferType === 0 ? 'withdraw' : 'deposit';
	const cssTransfer = transferType === 0 ? 'out' : 'in';

	return (
		<Row justify="space-around" align="middle" className={`transaction-item transaction-item-${cssTransfer}`}>
			<Col span={2}>
				<div className={`transaction-icon transaction-icon-${cssTransfer}`}>
					{transferType === 1 ? <span>INT</span> : <span>OUT</span>}
				</div>
			</Col>
			<Col span={8}>
				<div className="transaction-list-item">
					<div style={{ width: 40 }}>
						<CustomAvatar type="panel-avatar" avatar={'No'} />
					</div>
					<div
						className="transaction-info"
						style={{ paddingLeft: '10px', overflow: 'hidden', paddingTop: 5 }}
					>
						<div className="transaction-user-name">{userId}</div>
						<div className="transaction-time">
							{moment(recordedTime * 1000).format('hh:mm A - DD/MM/YYYY')}
						</div>
					</div>
				</div>
			</Col>
			<Col span={9}>
				<div>{description}</div>
			</Col>
			<Col span={3}>
				<div className={'amount-transfer ' + cssClass}>
					<span>{transferType === 0 ? `-${formattedAmount} VND` : `+${formattedAmount} VND`}</span>
				</div>
			</Col>
			<Col span={2}>
				<Button className="addfriend-btn" size={'small'}>
					<span>Detail</span>
				</Button>
			</Col>
		</Row>
	);
}

export default TransactionHistoryItem;
