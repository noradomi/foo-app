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
		<Row type="flex">
			<Col span={2} style={{ height: '100%' }}>
				{/* <div style={{ display: 'flex' }}>
					<div className="transaction-icon">
						{transferType === 1 ? (
							<ArrowRightOutlined style={{ color: 'rgb(81, 204, 40)' }} />
						) : (
							<ArrowLeftOutlined style={{ color: 'rgb(255, 0, 0)' }} />
						)}
					</div>
					<CustomAvatar type="panel-avatar" avatar={'Noradomi'} />
				</div> */}
				<div className="transaction-icon" style={{ height: '100%' }}>
					{transferType === 1 ? (
						<ArrowRightOutlined style={{ color: 'rgb(81, 204, 40)' }} />
					) : (
						<ArrowLeftOutlined style={{ color: 'rgb(255, 0, 0)' }} />
					)}
				</div>
			</Col>
			<Col span={7} style={{ height: '100%' }}>
				<div className="transaction-info" style={{ paddingLeft: '10px', overflow: 'hidden', paddingTop: 5 }}>
					<div className="transaction-user-name">{userId}</div>
					<div className="transaction-time">{moment(recordedTime * 1000).format('hh:mm A - DD/MM/YYYY')}</div>
				</div>
			</Col>
			<Col span={10} style={{ height: '100%' }}>
				<div>{description}</div>
			</Col>
			<Col span={3} style={{ height: '100%' }}>
				<div className={'amount-transfer ' + cssClass}>
					<span>{transferType === 0 ? `-${formattedAmount} VND` : `+${formattedAmount} VND`}</span>
				</div>
			</Col>
			<Col span={2} style={{ height: '100%' }}>
				<Button className="addfriend-btn" size={'small'}>
					<span>Detail</span>
				</Button>
			</Col>
		</Row>
	);
}

export default TransactionHistoryItem;
