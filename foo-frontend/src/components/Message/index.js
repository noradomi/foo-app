import { DollarCircleFilled } from '@ant-design/icons';
import { Card, Divider } from 'antd';
import Meta from 'antd/lib/card/Meta';
import moment from 'moment';
import React from 'react';
import './Message.css';
import CustomAvatar from '../CustomAvatar';

moment.locale('vi');
export default function Message(props) {
	const { data, isTransfer, isMine, startsSequence, endsSequence, showTimestamp } = props;
	const friendlyTimestamp = moment(data.timestamp).format('hh:mm DD/MM/YYYY  ');
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
				<Divider className="timestamp-divider" style={{ color: '#e5e5e5', margin: '5px 0px' }}>
					<div className="timestamp">{friendlyTimestamp}</div>
				</Divider>
			)}

			<div className="bubble-container">
				<div className={[ 'message-item', `${isMine ? 'mine' : ''}` ].join(' ')}>
					<div style={{ minWidth: '45px', marginTop: '20px' }}>
						<CustomAvatar type="chat-avatar" avatar={data.avatar} show={startsSequence} />
					</div>
					{isTransfer ? (
						<Card>
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
							{endsSequence ? (
								<div className="end-sequence-time">{moment(data.timestamp).format('hh:mm')}</div>
							) : (
								''
							)}
						</Card>
					) : (
						<div className="bubble" title={friendlyTimestamp}>
							<div style={{ minWidth: '32px' }}>
								<span>{data.message}</span>
							</div>
							{endsSequence ? (
								<div className="end-sequence-time">{moment(data.timestamp).format('hh:mm')}</div>
							) : (
								''
							)}
						</div>
					)}
				</div>
			</div>
		</div>
	);
}
