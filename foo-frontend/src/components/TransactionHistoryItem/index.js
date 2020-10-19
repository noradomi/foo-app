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
