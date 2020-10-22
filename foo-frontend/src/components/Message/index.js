import { DollarCircleFilled } from '@ant-design/icons';
import { Card, Divider } from 'antd';
import Meta from 'antd/lib/card/Meta';
import moment from 'moment';
import React from 'react';
import './Message.css';

moment.locale('vi');
export default function Message(props) {
	const { data, isTransfer, isMine, startsSequence, endsSequence, showTimestamp } = props;

	const friendlyTimestamp = moment(data.timestamp).format('DD-MM-YYYY  hh:mm A');
	return (
		<div
			className={[
				'message',
				`${isMine ? 'mine' : ''}`,
				`${startsSequence ? 'start' : ''}`,
				`${endsSequence ? 'end' : ''}`,
				`${isTransfer ? 'transfer' : ''}`
			].join(' ')}
			style={{ wordWrap: 'break-word' }}
		>
			{showTimestamp && (
				<Divider className="timestamp-divider" style={{ color: '#e5e5e5' }}>
					<div className="timestamp">{friendlyTimestamp}</div>
				</Divider>
			)}

			<div className="bubble-container">
				{isTransfer ? (
					<Card style={{ borderRadius: '15px' }}>
						<Meta
							avatar={
								<DollarCircleFilled
									className={[ 'transfer-message-icon', `${isMine ? 'mine' : ''}` ].join(' ')}
								/>
							}
							title={
								<div className="transfer-message-amount">
									{isMine ? 'Chuyển' : 'Đã chuyển'}{' '}
									{`${data.message}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')} VND
								</div>
							}
						/>
						<div>Chuyển tiền</div>
					</Card>
				) : (
					<div className="bubble" title={friendlyTimestamp}>
						{data.message}
					</div>
				)}
			</div>
		</div>
	);
}
