import { UserAddOutlined, StepForwardOutlined } from '@ant-design/icons';
import Modal from 'antd/lib/modal/Modal';
import React, { useState } from 'react';
import AddFriendList from '../AddFriendList';
import './AddFriend.css';
import { useHistory } from 'react-router-dom';

AddFriend.propTypes = {};

function AddFriend(props) {
	const [ visible, setVisible ] = useState(false);
	const history = useHistory();
	const showModal = () => {
		setVisible(true);
	};

	const handleOk = (e) => {
		console.log(e);
	};

	const handleCancel = (e) => {
		setVisible(false);
		history.push('/');
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
				title={
          <>
          <img
							src="https://zalo-chat-static.zadn.vn/v1/NewFr@2x.png"
							style={{ borderRadius: '50%', width: '50px', height: '50px', marginRight: '11px' }}
						/>
						<span>Add new friend</span>
    </>
				
				}
				onCancel={handleCancel}
				visible={visible}
				footer={null}
				destroyOnClose={true}
				width={380}
				bodyStyle={{ backgroundColor: 'rgb(245, 245, 245)' }}
			>
				<AddFriendList />
			</Modal>
		</div>
	);
}

export default AddFriend;
