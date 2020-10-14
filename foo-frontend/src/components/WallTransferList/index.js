import { ProfileFilled } from '@ant-design/icons';
import { Card } from 'antd';
import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { getFriendList } from '../../services/load-friend-list';
import WalletTransferListItem from '../WalletTransferListItem';
import './WalletTransferList.css';

function WalletTransferList(props) {
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
		<Card bordered={false} style={{ margin: '30px' }}>
			<div className="wallet-list-title">
				<ProfileFilled /> Danh sách chuyển tiền
			</div>
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
	);
}

function mapStateToProps(state) {
	return {
		friendList: state.friendList,
		userMapHolder: state.userMapHolder
	};
}

export default connect(mapStateToProps, null)(WalletTransferList);
