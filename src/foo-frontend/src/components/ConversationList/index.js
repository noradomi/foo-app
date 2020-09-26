import React, { useEffect } from 'react';
import ConversationSearch from '../ConversationSearch';
import ConversationListItem from '../ConversationListItem';
import Toolbar from '../Toolbar';
import ToolbarButton from '../ToolbarButton';
import './ConversationList.css';
import { getUserList } from '../../services/view-user-list';
import { connect, useSelector } from 'react-redux';
import { wsConnect } from '../../services/chat-single';

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
			{/* <Toolbar
				title="Foo App Chat"
				leftItems={[ <ToolbarButton key="cog" icon="ion-ios-cog" /> ]}
				rightItems={[ <ToolbarButton key="cog" icon="ion-ios-notifications-outline" /> ]}
			/> */}
			<div className="profile">FooApp - Noradomi</div>
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
		userList: state.userList
	};
}

export default connect(mapStateToProps, null)(ConversationList);
