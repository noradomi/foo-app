import React, { useEffect } from 'react';
import ConversationSearch from '../ConversationSearch';
import ConversationListItem from '../ConversationListItem';
import Toolbar from '../Toolbar';
import ToolbarButton from '../ToolbarButton';
import './ConversationList.css';
import { getUserList } from '../../services/view-user-list';
import { connect } from 'react-redux';
import { wsConnect } from '../../services/chat-single';

function ConversationList(props) {
	// Init user list
	useEffect(() => {
		getUserList().then(() => {
			wsConnect();
		});
	}, []);

	const conversations = props.userList.map((res) => {
		return {
			name: res.name,
			text: `@${res.name}`,
			id: res.userId,
			avatar: res.avatar,
			online: res.online
		};
	});

	return (
		<div className="conversation-list">
			<Toolbar
				title="Foo App Chat"
				leftItems={[ <ToolbarButton key="cog" icon="ion-ios-cog" /> ]}
				rightItems={[ <ToolbarButton key="cog" icon="ion-ios-notifications-outline" /> ]}
			/>
			<ConversationSearch />
			<div className="conversation-list-scroll">
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
