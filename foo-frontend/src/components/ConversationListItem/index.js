import React, { useEffect } from 'react';
import shave from 'shave';
import CustomAvatar from '../CustomAvatar/index';
import { setSelectedUserAction } from '../../actions/fooAction';
import './ConversationListItem.css';
import { connect } from 'react-redux';

function ConversationListItem(props) {
	useEffect(() => {
		shave('.conversation-snippet', 20);
	});

	let selectedItemStyle = {};

	const { name, avatar, id, text } = props.data;

	if (props.selectedUser.id === id) {
		selectedItemStyle.backgroundColor = 'rgb(230, 235, 245)';
	}

	let clickHandle = () => {
		props.setActiveItem({});
	};

	let status = props.userMapHolder.userMap.get(id).online;

	return (
		<div className="conversation-list-item" onClick={clickHandle} style={selectedItemStyle}>
			<div className="conversation-avatar" style={{ width: 60 }}>
				<CustomAvatar type="user-avatar" avatar={avatar} />
			</div>
			{status ? <div className="status-point online" /> : <div className="status-point offline" />}
			<div className="conversation-info" style={{ paddingLeft: '10px', overflow: 'hidden', paddingTop: 5 }}>
				<div className="user-name">{name}</div>
				<div className="history-message">{text}</div>
			</div>
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
		setActiveItem: (id) => dispatch(setSelectedUserAction(id))
	};
}

export default connect(mapStateToProps, mapDispathToProps)(ConversationListItem);
