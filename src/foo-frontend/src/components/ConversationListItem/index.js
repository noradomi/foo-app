import React, { useEffect } from 'react';
import shave from 'shave';
import CustomAvatar from '../CustomAvatar/index';
import './ConversationListItem.css';

export default function ConversationListItem(props) {
	useEffect(() => {
		shave('.conversation-snippet', 20);
	});

	const { name, text, avatar } = props.data;

	return (
		<div className="conversation-list-item">
			{/* <img className="conversation-photo" src={photo} alt="conversation" /> */}
			<div style={{ width: 60 }}>
				<CustomAvatar type="user-avatar" avatar={avatar} />
			</div>
			<div className="conversation-info" style={{paddingLeft: "10px",overflow: 'hidden', paddingTop: 5}}>
				{/* <h1 className="conversation-title">{name}</h1>
				<p className="conversation-snippet">{text}</p> */}
        <div className="user-name">{name}</div>
                  <div className="history-message">{text}</div>
			</div>
		</div>
	);
}
