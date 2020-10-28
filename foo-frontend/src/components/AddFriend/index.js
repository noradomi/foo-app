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

	const handleCancel = (e) => {
		setVisible(false);
	};
	return (
		<div>
			<div className="new-action-menu" onClick={showModal}>
				<UserAddOutlined style={{ fontSize: '30px', color: 'rgb(62, 130, 239)' }} />
				<div className="new-text">Thêm bạn mới</div>
			</div>
			<Modal
				title={
					<div className="addfriend-header">
						<span style={{ color: 'rgb(62, 130, 239)', fontSize: '30px', marginRight: '10px' }}>
							<UserAddOutlined />
						</span>
						<span className="addfriend-title">Thêm bạn mới</span>
					</div>
				}
				onCancel={handleCancel}
				visible={visible}
				footer={null}
				destroyOnClose={true}
				width={430}
				bodyStyle={{ backgroundColor: 'rgb(245, 245, 245)', padding: 0 }}
			>
				<AddFriendList />
			</Modal>
		</div>
	);
}

export default AddFriend;
