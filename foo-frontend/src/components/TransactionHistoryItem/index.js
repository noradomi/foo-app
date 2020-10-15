import { InfoOutlined, CaretRightFilled } from '@ant-design/icons';
import { Button, Col, Row, Tooltip, Badge } from 'antd';
import moment from 'moment';
import React from 'react';
import CustomAvatar from '../CustomAvatar';
import './TransactionHistoryItem.css';
import { List } from 'antd/lib/form/Form';
import Avatar from 'antd/lib/avatar/avatar';

function TransactionHistoryItem(props) {
	const { userName, description, recordedTime, amount, transferType } = props.data;

	const formattedAmount = `${amount}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',');

	const cssClass = transferType === 0 ? 'withdraw' : 'deposit';
	const cssTransfer = transferType === 0 ? 'out' : 'in';

	return (
		// <Row justify="space-around" align="middle" className={`transaction-item transaction-item-${cssTransfer}`}>
		// 	<Col span={2}>
		// 		<div className={`transaction-icon transaction-icon-${cssTransfer}`}>
		// 			{transferType === 1 ? <span>INT</span> : <span>OUT</span>}
		// 		</div>
		// 	</Col>
		// 	<Col span={6}>
		// 		<div className="transaction-list-item">
		// 			<div style={{ width: 40 }}>
		// <CustomAvatar type="panel-avatar" avatar={userName} />
		// 			</div>
		// 			<div
		// 				className="transaction-info"
		// 				style={{ paddingLeft: '10px', overflow: 'hidden', paddingTop: 5 }}
		// 			>
		// 				<div className="transaction-user-name">{userName}</div>
		// 				<div className="transaction-time">
		// 					{moment(recordedTime * 1000).format('hh:mm A - DD/MM/YYYY')}
		// 				</div>
		// 			</div>
		// 		</div>
		// 	</Col>
		// 	<Col span={12}>
		// 		<div>{description}</div>
		// 	</Col>
		// 	<Col span={3}>
		// 		<div className={'amount-transfer ' + cssClass}>
		// 			<span>{transferType === 0 ? `-${formattedAmount} VND` : `+${formattedAmount} VND`}</span>
		// 		</div>
		// 	</Col>
		// 	<Col span={1}>
		// <Tooltip placement="topLeft" title={'Chi tiết'}>
		// 	<Button className="addfriend-btn" size={'small'} shape="circle" icon={<InfoOutlined />} />
		// </Tooltip>
		// 	</Col>
		// </Row>
		<Badge.Ribbon
			text={
				<span>
					<CaretRightFilled /> Chuyển tiền
				</span>
			}
			placement="start"
		>
			<div style={{ paddingTop: '15px' }} />
			<List.Item
				actions={[
					<Tooltip placement="topLeft" title={'Chi tiết'}>
						<Button className="addfriend-btn" size={'small'} shape="circle" icon={<InfoOutlined />} />
					</Tooltip>
				]}
				style={{ border: '1px solid red', marginTop: '10px', padding: '8px 10px' }}
			>
				<List.Item.Meta
					avatar={<CustomAvatar type="panel-avatar" avatar={userName} />}
					title={<span>{moment(recordedTime * 1000).format('hh:mm A - DD/MM/YYYY')}</span>}
					description={{ description }}
				/>
				<div>-${formattedAmount} VND`</div>
			</List.Item>
		</Badge.Ribbon>
	);
}

export default TransactionHistoryItem;
