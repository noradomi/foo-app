import React, { useEffect } from 'react';
import ConversationSearch from '../ConversationSearch';
import SignOutButton from '../SignOutButton';
import ConversationListItem from '../ConversationListItem';
import Toolbar from '../Toolbar';
import ToolbarButton from '../ToolbarButton';
import axios from 'axios';
import './ConversationList.css';
import { getUserList } from '../../services/view-user-list';
import { connect } from 'react-redux';

import { wsConnect } from '../../services/chat-single';

// getUserList();
function ConversationList(props) {

	// Init user list
	useEffect(() => {
		getUserList()
			.then(() => {
				console.log('Load user list done');
				wsConnect();
			})
			.catch(() => console.log('Load user list failed'));
	}, []);

	
	const conversations = props.userList.map((res) => {
		console.log('Username: ' + res.name + ' - ' + res.online);
		return {
			name: res.name,
			text: 'Online',
			id: res.userId,
			avatar: res.avatar,
			online: res.online
		};
	});

	return (
		<div className="conversation-list">
			<Toolbar
				title="Messenger"
				leftItems={[ <ToolbarButton key="cog" icon="ion-ios-cog" /> ]}
				rightItems={[
					<ToolbarButton key="cog" icon="ion-ios-notifications-outline" />
				]}
			/>
			<div className="conversation-list-scroll">
				<ConversationSearch />
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
	
	};
}

// function mapDispatchToProps(dispatch) {
//   return {
//     loadUserList(){
// 		getUserList();
// 	}
//   }
// }

export default connect(mapStateToProps, null)(ConversationList);
