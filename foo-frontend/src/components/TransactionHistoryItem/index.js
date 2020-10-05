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

	return (
		<Row className="transaction-list-item">
			<Col span={2}>
				<div style={{ display: 'flex' }}>
					<div className="transaction-icon">
						{transferType === 1 ? (
							<ArrowRightOutlined style={{ color: 'rgb(81, 204, 40)' }} />
						) : (
							<ArrowLeftOutlined style={{ color: 'rgb(255, 0, 0)' }} />
						)}
					</div>
					<CustomAvatar type="panel-avatar" avatar={'Noradomi'} />
				</div>
			</Col>
			<Col span={7}>
				<div className="transaction-info" style={{ paddingLeft: '10px', overflow: 'hidden', paddingTop: 5 }}>
					<div className="transaction-user-name">{userId}</div>
					<div className="history-message">{moment(recordedTime * 1000).format('hh:mm A - DD/MM/YYYY')}</div>
				</div>
			</Col>
			<Col span={10}>
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
