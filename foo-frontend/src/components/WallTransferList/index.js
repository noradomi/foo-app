import { Card, Divider } from 'antd';
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
			{/* <Divider plain style={{ color: 'rgb(122, 134, 154)' }}>
				Danh sách chuyển tiền ({conversations.length})
			</Divider> */}

			<Card
				className="wallet-list-card"
				title={<span>Danh sách chuyển tiền ({conversations.length})</span>}
				bodyStyle={{ padding: '10px 0' }}
			>
				<div className="wallet-list-scroll">
					{conversations.length > 0 ? (
						conversations.map((conversation) => (
							<WalletTransferListItem key={conversation.id} data={conversation} />
						))
					) : (
						''
					)}
				</div>
			</Card>
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
