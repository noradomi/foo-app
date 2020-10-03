import React, { useState } from 'react';
import PropTypes from 'prop-types';
import CustomAvatar from '../CustomAvatar';
import Modal from 'antd/lib/modal/Modal';
import { Alert, Input } from 'antd';
import './AddFriend.css';
import { UserAddOutlined } from '@ant-design/icons';

AddFriend.propTypes = {};

function AddFriend(props) {
	const [ visible, setVisible ] = useState(false);
	const showModal = () => {
		setVisible(true);
	};

	const handleOk = (e) => {
		console.log(e);
		// var un = $('#add-user-name').val();
		// $('#add-user-name').val('');
		// this.props.addNewFriend(un);
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
				title="Add New Friend"
				visible={visible}
				onOk={handleOk}
				onCancel={handleCancel}
				okText="Add"
				cancelText="Cancel"
			>
				{/* {this.props.addFriendError ? <Alert message={this.props.addFriendErrorMessage} type="error" /> : ''} */}
				<p className="model-label">Please enter user name:</p>
				<Input id="add-user-name" className="add-user-name" onPressEnter={handleOk} />
			</Modal>
		</div>
	);
}

export default AddFriend;
