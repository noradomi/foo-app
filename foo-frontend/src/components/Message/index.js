import React from 'react';
import moment from 'moment';
import './Message.css';
import { DollarCircleOutlined } from '@ant-design/icons';
import { Card } from 'antd';
import Meta from 'antd/lib/card/Meta';

export default function Message(props) {
	const { data, isTransfer, isMine, startsSequence, endsSequence, showTimestamp } = props;

	const friendlyTimestamp = moment(data.timestamp).format('LLLL');
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
			{showTimestamp && <div className="timestamp">{friendlyTimestamp}</div>}

			<div className="bubble-container">
				{isTransfer ? (
					// <div className="transfer-message">
					// 	<DollarCircleOutlined className="transfer-message-icon" />
					// <div className="transfer-message-amount">
					// 	Chuyển {`${data.message}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')} VND
					// </div>
					// </div>
					<Card style={{ borderRadius: '20px' }} actions={[ <span>Chạm để xem</span> ]}>
						<Meta
							avatar={
								<DollarCircleOutlined
									className={[ 'transfer-message-icon', `${isMine ? 'mine' : ''}` ].join(' ')}
								/>
							}
							title={
								<div className="transfer-message-amount">
									Chuyển {`${data.message}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')} VND
								</div>
							}
						/>
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
