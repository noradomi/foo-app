import React, { useState, useEffect } from 'react';
import ConversationSearch from '../ConversationSearch';
import SignOutButton from '../SignOutButton';
import ConversationListItem from '../ConversationListItem';
import Toolbar from '../Toolbar';
import ToolbarButton from '../ToolbarButton';
import axios from 'axios';
import './ConversationList.css';
import { getUserList } from '../../services/view-user-list';
import { connect } from 'react-redux';
import { hanldeLogout } from '../../services/logout';
import { useHistory } from 'react-router-dom';

getUserList();
function ConversationList(props) {
	
	// useEffect(() => {
	// 	getUserList()
	// 	.then(() => console.log("Load user list dont"))
	// 	.catch(() => console.log("Load user list failed"));
	// })

	let history = useHistory();
	const conversations = props.userList.map((res) => {
		return {
			name: res.name,
			text: 'Online',
			id: res.userId,
			avatar: res.avatar
		};
	});

	return (
		<div className="conversation-list">
			<Toolbar
				title="Messenger"
				leftItems={[ <ToolbarButton key="cog" icon="ion-ios-cog" /> ]}
				rightItems={[
					<SignOutButton
						key="add"
						icon="ion-ios-log-out"
						onClick={() => {
							hanldeLogout().then(() => history.push('/login'));
						}}
					/>
				]}
			/>
			<ConversationSearch />
			{conversations.map((conversation) => <ConversationListItem key={conversation.name} data={conversation} />)}
		</div>
	);
}

function mapStateToProps(state) {
	return {
		userList: state.userList
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
