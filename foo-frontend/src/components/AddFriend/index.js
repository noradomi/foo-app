import { UserAddOutlined } from '@ant-design/icons';
import Modal from 'antd/lib/modal/Modal';
import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import AddFriendList from '../AddFriendList';
import './AddFriend.css';

AddFriend.propTypes = {};

function AddFriend(props) {
	const [ visible, setVisible ] = useState(false);
	const history = useHistory();
	const showModal = () => {
		setVisible(true);
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
					<div className="new-text">Thêm bạn mới</div>
				</a>
			</div>
			<Modal
				title={
          <>
            <div className="addfriend-header">
            <img
							src="https://zalo-chat-static.zadn.vn/v1/NewFr@2x.png"
							style={{ borderRadius: '50%', width: '50px', height: '50px', marginRight: '11px' }}
						/>
						<span className="addfriend-title">Thêm bạn mới</span>
            </div>
          
          </>
				
				}
				onCancel={handleCancel}
				visible={visible}
				footer={null}
				destroyOnClose={true}
				width={400}
				bodyStyle={{ backgroundColor: 'rgb(245, 245, 245)', padding: 0 }}
			>
				<AddFriendList />
			</Modal>
		</div>
	);
}

export default AddFriend;
