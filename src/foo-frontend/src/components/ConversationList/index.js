import React, { useState, useEffect } from 'react';
import ConversationSearch from '../ConversationSearch';
import SignOutButton from '../SignOutButton';
import ConversationListItem from '../ConversationListItem';
import Toolbar from '../Toolbar';
import ToolbarButton from '../ToolbarButton';
import axios from 'axios';
import './ConversationList.css';
import {getUserList} from '../../services/view-user-list';
import { connect } from 'react-redux';


getUserList();
function ConversationList(props) {
	// const [ conversations, setConversations ] = useState([]);
	// useEffect(() => {
	// 	getConversations();
	// }, []);

	// const getConversations = () => {
	// 	axios.get('https://randomuser.me/api/?results=20').then((response) => {
	// 		let newConversations = response.data.results.map((result) => {
	// 			return {
	// 				photo: result.picture.large,
	// 				name: `${result.name.first} ${result.name.last}`,
	// 				text: 'Hello world! This is a long message that needs to be truncated.'
	// 			};
	// 		});
	// 		setConversations([ ...conversations, ...newConversations ]);
	// 	});
  // };
  
  const conversations = props.userList.map(res => {
    return {
      name: res.name,
      text: 'Online',
      id: res.userId,
      avatar: res.avatar
    }
  })

	return (
		<div className="conversation-list">
			<Toolbar
				title="Messenger"
				leftItems={[ <ToolbarButton key="cog" icon="ion-ios-cog" /> ]}
				rightItems={[ <SignOutButton key="add" icon="ion-ios-add-circle-outline" /> ]}
			/>
			<ConversationSearch />
			{conversations.map((conversation) => <ConversationListItem key={conversation.name} data={conversation} />)}
		</div>
	);
}

function mapStateToProps(state) {
  return {
    userList: state.userList
  }
}

// function mapDispatchToProps(dispatch) {
//   return {
//     load(){
//       getUserList()
//     }
//   }
// }

export default connect(mapStateToProps,null)(ConversationList);