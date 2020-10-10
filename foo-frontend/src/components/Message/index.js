import { DollarCircleFilled } from '@ant-design/icons';
import { Card } from 'antd';
import Meta from 'antd/lib/card/Meta';
import moment from 'moment';
import React from 'react';
import './Message.css';

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
					<Card style={{ borderRadius: '20px' }} actions={[ <span>Chạm để xem</span> ]}>
						<Meta
							avatar={
								<DollarCircleFilled
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
