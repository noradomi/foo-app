import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { getNotFriendList } from '../../services/load-not-friend-list';
import AddFriendListItem from '../AddFriendListItem';
import './AddFriendList.css';

function AddFriendList(props) {
	// Init user list
	useEffect(() => {
		getNotFriendList();
	}, []);

	const conversations = props.userList.map((res) => {
		const { name, userId, avatar } = res;

		return {
			name: name,
			id: userId,
			avatar: avatar
		};
	});

	console.log(conversations.length);

	return (
		<div className="addfriend-list">
			{/* <ConversationSearch /> */}
			<div className="addfriend-user-list-title">Danh sách kết bạn ({conversations.length})</div>
			<div className="addfriend-list-scroll">
				{conversations.length > 0 ? (
					conversations.map((conversation) => <AddFriendListItem key={conversation.id} data={conversation} />)
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

export default connect(mapStateToProps, null)(AddFriendList);
