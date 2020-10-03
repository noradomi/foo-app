import { PlusCircleOutlined } from '@ant-design/icons';
import { Button, message } from 'antd';
import React from 'react';
import CustomAvatar from '../CustomAvatar';
import { doAddFriend } from '../../services/addfriend';
import './AddFriendListItem.css';

AddFriendListItem.propTypes = {};

function AddFriendListItem(props) {
	const { id, name, avatar } = props.data;

	const handleAddFriend = () => {
		doAddFriend(id)
			.then(() => {
				message.success('Add friend with ' + id);
			})
			.catch(() => {
				message.error('Add friend failed');
			});
	};
	return (
		<div className="addfriend-list-item">
			<div style={{ width: 40 }}>
				<CustomAvatar type="panel-avatar" avatar={avatar} />
			</div>
			<div className="addfriend-info" style={{ paddingLeft: '10px', overflow: 'hidden', paddingTop: 5 }}>
				<div className="addfriend-name">{name}</div>
			</div>
			<Button className="addfriend-btn" type="primary" size={'small'} danger onClick={handleAddFriend}>
				ADD
			</Button>
		</div>
	);
}

export default AddFriendListItem;
