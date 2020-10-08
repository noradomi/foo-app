import { Divider } from 'antd';
import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { getFriendList } from '../../services/load-friend-list';
import WalletTransferListItem from '../WalletTransferListItem';
import './WalletTransferList.css';

function WalletTransferList(props) {
	// Init user list
	useEffect(() => {
		getFriendList();
	}, []);

	const conversations = props.friendList.map((res) => {
		const { name, userId, avatar } = res;

		return {
			name: name,
			id: userId,
			avatar: avatar
		};
	});
	return (
		<div className="wallet-list">
			<Divider orientation="left" plain>
				Bạn ({conversations.length})
			</Divider>
			<div className="wallet-list-scroll">
				{conversations.length > 0 ? (
					conversations.map((conversation) => (
						<WalletTransferListItem key={conversation.id} data={conversation} />
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
		userMapHolder: state.userMapHolder
	};
}

export default connect(mapStateToProps, null)(WalletTransferList);
