import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { getNotFriendList } from '../../services/load-not-friend-list';
import AddFriendListItem from '../AddFriendListItem';
import './AddFriendList.css';
import { Empty } from 'antd';

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

	return (
		<div className="addfriend-list">
			<div className="addfriend-user-list-title">Danh sách kết bạn ({conversations.length})</div>
			<div className="addfriend-list-scroll">
				{conversations.length > 0 ? (
					conversations.map((conversation) => <AddFriendListItem key={conversation.id} data={conversation} />)
				) : (
					<Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description="Rỗng" />
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
