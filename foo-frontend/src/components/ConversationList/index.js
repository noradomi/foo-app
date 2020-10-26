import { Divider, Empty } from 'antd';
import React, { useEffect } from 'react';
import { Scrollbars } from 'react-custom-scrollbars';
import { connect } from 'react-redux';
import { getFriendList } from '../../services/load-friend-list';
import AddFriendModal from '../AddFriend';
import ConversationListItem from '../ConversationListItem';
import './ConversationList.css';

function ConversationList(props) {
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

	return (
		<div className="conversation-list">
			<div className="profile">FooApp - {props.user.name} !</div>
			<AddFriendModal />
			<Divider className="user-list-title" orientation="left" plain style={{ color: '#e5e5e5' }}>
				<span style={{ color: '#000', fontSize: '16px' }}>Bạn ({conversations.length})</span>
			</Divider>
			<div className="conversation-list-scroll">
				<Scrollbars autoHide autoHideTimeout={500} autoHideDuration={200} className="custom-scrollbars">
					{conversations.length > 0 ? (
						conversations.map((conversation) => (
							<ConversationListItem key={conversation.id} data={conversation} />
						))
					) : (
						<Empty
							image={Empty.PRESENTED_IMAGE_SIMPLE}
							description={<span>Bạn chưa kết bạn với ai cả</span>}
						/>
					)}
				</Scrollbars>
			</div>
		</div>
	);
}

function mapStateToProps(state) {
	return {
		friendList: state.friendList,
		user: state.user
	};
}

export default connect(mapStateToProps, null)(ConversationList);
