import React, { useEffect } from 'react';
import { connect, useSelector } from 'react-redux';
import { wsConnect } from '../../services/chat-single';
import { getFriendList } from '../../services/load-friend-list';
import AddFriendModal from '../AddFriend';
import ConversationListItem from '../ConversationListItem';
import ConversationSearch from '../ConversationSearch';
import './ConversationList.css';

function ConversationList(props) {
	const webSocket = useSelector((state) => state.webSocket);

	if (webSocket.webSocket === null) {
		wsConnect();
	}

	// Init user list
	useEffect(() => {
		getFriendList();
	}, []);

	const conversations = props.friendList.map((res) => {
		const { name, userId, avatar, online, lastMessage } = res;

		return {
			name: name,
			text: lastMessage,
			id: userId,
			avatar: avatar,
			online: online
		};
	});

	console.log(conversations.length);

	return (
		<div className="conversation-list">
			<div className="profile">FooApp - {props.user.name}</div>
			<ConversationSearch />
			<AddFriendModal />
			<div className="conversation-list-scroll">
				<div className="user-list-title">Friends ({conversations.length})</div>
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
		friendList: state.friendList,
		user: state.user,
		websocket: state.webSocket
	};
}

export default connect(mapStateToProps, null)(ConversationList);
