import { PlusOutlined } from '@ant-design/icons';
import { Button, message } from 'antd';
import React, { useState } from 'react';
import { doAddFriend } from '../../services/addfriend';
import CustomAvatar from '../CustomAvatar';
import './AddFriendListItem.css';

AddFriendListItem.propTypes = {};

function AddFriendListItem(props) {
	const { id, name, avatar } = props.data;
	const [ btnVisbale, setBtnVisible ] = useState('visible');

	const handleAddFriend = () => {
		setBtnVisible('none');
		doAddFriend(id)
			.then(() => {
				message.success('Kết bạn thành công với ' + name);
			})
			.catch(() => {
				message.error('Kết bạn thất bại, thử lại !');
			});
	};
	return (
		<div className="addfriend-list-item">
			<div style={{ width: 45 }}>
				<CustomAvatar type="panel-avatar" avatar={avatar} />
			</div>
			<div className="addfriend-info" style={{ overflow: 'hidden', paddingTop: 5 }}>
				<div className="addfriend-name">{name}</div>
			</div>
			<Button
				onClick={handleAddFriend}
				style={{ display: btnVisbale }}
				className="addfriend-btn"
				size={'small'}
				icon={<PlusOutlined />}
			>
				<span>Kết bạn</span>
			</Button>
		</div>
	);
}

export default AddFriendListItem;
