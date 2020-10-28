import { Empty } from 'antd';
import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { getNotFriendList } from '../../services/load-not-friend-list';
import AddFriendListItem from '../AddFriendListItem';
import './AddFriendList.css';
import Scrollbars from 'react-custom-scrollbars';

function AddFriendList(props) {
	const [ hasMore, setHasMore ] = useState(true);

	// Init user list
	useEffect(() => {
		getNotFriendList(0).then((dataSize) => {
			if (dataSize === 0) {
				setHasMore(false);
			}
		});
	}, []);

	const conversations = props.userList.map((res) => {
		const { name, userId, avatar } = res;
		return {
			name: name,
			id: userId,
			avatar: avatar
		};
	});

	const handleInfiniteOnLoad = () => {
		getNotFriendList(conversations.length).then((dataSize) => {
			if (dataSize === 0) {
				setHasMore(false);
			}
		});
	};

	return (
		<div className="addfriend-list">
			<div className="addfriend-user-list-title">Danh sách kết bạn ({conversations.length})</div>
			<div className="addfriend-list-scroll">
				<Scrollbars>
					{conversations.length > 0 ? (
						<InfiniteScroll
							initialLoad={false}
							pageStart={0}
							loadMore={() => {
								handleInfiniteOnLoad();
							}}
							hasMore={hasMore}
							useWindow={false}
						>
							{conversations.map((conversation) => (
								<AddFriendListItem key={conversation.id} data={conversation} />
							))}
						</InfiniteScroll>
					) : (
						<Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description="Rỗng" />
					)}
				</Scrollbars>
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
