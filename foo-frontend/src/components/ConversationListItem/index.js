import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import shave from 'shave';
import { setSelectedUserAction, setUnseenMessages } from '../../actions/fooAction';
import { resetUnseen } from '../../services/reset-unseen';
import CustomAvatar from '../CustomAvatar/index';
import './ConversationListItem.css';

function ConversationListItem(props) {
	useEffect(() => {
		shave('.history-message', 20);
	});

	let selectedItemStyle = {};

	const { name, avatar, id } = props.data;

	if (props.selectedUser.id === id) {
		selectedItemStyle.backgroundColor = 'rgb(230, 235, 245)';
	}

	const userInfo = props.userMapHolder.userMap.get(id);
	const status = userInfo.online;
	const lastMessage = userInfo.lastMessage || 'No message';
	const unreadMessages = userInfo.unreadMessages;

	let clickHandle = () => {
		const selectedUser = {
			id: id,
			name: name,
			avatar: avatar
		};
		props.setActiveItem(selectedUser);
		if (unreadMessages > 0) {
			props.resetUnseenMessges(id);
			resetUnseen(id);
		}
	};

	return (
		<div className="conversation-list-item" onClick={clickHandle} style={selectedItemStyle}>
			<div className="conversation-avatar" style={{ width: 60 }}>
				<CustomAvatar type="user-avatar" avatar={avatar} />
			</div>
			{status ? <div className="status-point online" /> : <div className="status-point offline" />}
			<div className="conversation-info" style={{ paddingLeft: '10px', overflow: 'hidden', paddingTop: 5 }}>
				<div className="user-name">{name}</div>
				<div className="history-message">{lastMessage}</div>
			</div>
			{unreadMessages > 0 ? <div className="unread">{unreadMessages}</div> : ''}
		</div>
	);
}

function mapStateToProps(state) {
	return {
		selectedUser: state.selectedUser,
		userMapHolder: state.userMapHolder
	};
}

function mapDispathToProps(dispatch) {
	return {
		setActiveItem: (user) => dispatch(setSelectedUserAction(user)),
		resetUnseenMessges: (userId) => dispatch(setUnseenMessages(userId, 0))
	};
}

export default connect(mapStateToProps, mapDispathToProps)(ConversationListItem);
