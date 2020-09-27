import React, { useEffect } from 'react';
import { connect, useSelector } from 'react-redux';
import { wsConnect } from '../../services/chat-single';
import { getUserList } from '../../services/view-user-list';
import ConversationListItem from '../ConversationListItem';
import ConversationSearch from '../ConversationSearch';
import './ConversationList.css';

function ConversationList(props) {
	const messageList = useSelector((state) => {
		return state.chatMessagesHolder.chatMessages;
	});

	console.log(messageList);

	// Init user list
	useEffect(() => {
		getUserList().then(() => {
			wsConnect();
		});
	}, []);

	const conversations = props.userList.map((res) => {
		const { name, userId, avatar, online } = res;
		const messagesState = messageList.get(res.userId);
		const messages = messagesState.map((item) => {
			return {
				message: item.message
			};
		});

		console.log(messages);
		const lastMessage = messages.length === 0 ? 'No message' : messages[messages.length - 1];
		return {
			name: name,
			text: lastMessage,
			id: userId,
			avatar: avatar,
			online: online
		};
	});

	return (
		<div className="conversation-list">
			<div className="profile">FooApp - {props.user.name}</div>
			<ConversationSearch />
			<div className="conversation-list-scroll">
				<div className="user-list-title">Users (15)</div>
				{conversations.length > 0 ? (
					conversations.map((conversation) => (
						<ConversationListItem key={conversation.id} data={conversation} />
					))
				) : (
					''
				)}
			</div>
		</div>
	);
}

function mapStateToProps(state) {
	return {
		userList: state.userList,
		user: state.user
	};
}

export default connect(mapStateToProps, null)(ConversationList);
