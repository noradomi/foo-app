import { UserAddOutlined } from '@ant-design/icons';
import Modal from 'antd/lib/modal/Modal';
import React, { useState } from 'react';
import AddFriendList from '../AddFriendList';
import './AddFriend.css';

AddFriend.propTypes = {};

function AddFriend(props) {
	const [ visible, setVisible ] = useState(false);
	const showModal = () => {
		setVisible(true);
	};

	const handleOk = (e) => {
		console.log(e);
	};

	const handleCancel = (e) => {
		setVisible(false);
	};
	return (
		<div>
			<div className="new-action-menu" onClick={showModal}>
				<a href="#">
					{/* <CustomAvatar type="new-avatar" /> */}
					<UserAddOutlined style={{ fontSize: '30px' }} />
					<div className="new-text">Add New Friend</div>
				</a>
			</div>
			<Modal
				width="420px"
				onCancel={handleCancel}
				title="Add New Friend"
				visible={visible}
				footer={null}
				width={600}
			>
				<AddFriendList />
			</Modal>
		</div>
	);
}

export default AddFriend;
