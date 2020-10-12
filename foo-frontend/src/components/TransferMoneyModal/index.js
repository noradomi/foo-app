import { CloseOutlined, SwapOutlined } from '@ant-design/icons';
import { Modal } from 'antd';
import 'antd/dist/antd.css';
import React from 'react';
import { useSelector } from 'react-redux';
import { TransferFormSteps } from '../TransferFormSteps';
import './TransferMoneyModal.css';

const TransferMoneyModal = ({ userInfo, visible, onCancel }) => {
	let selectedUser = null;
	if (userInfo !== null) {
		selectedUser = userInfo;
	} else {
		selectedUser = useSelector((state) => state.selectedUser);
	}

	return (
		<Modal
			visible={visible}
			destroyOnClose={true}
			title={
				<div style={{ textAlign: 'center' }}>
					<span style={{ fontSize: '23px' }}>
						{' '}
						<SwapOutlined style={{ fontSize: '30px', color: 'rgb(103, 174, 63)' }} /> Chuyển tiền
					</span>
				</div>
			}
			footer=""
			cancelText={
				<span>
					<CloseOutlined /> Đóng
				</span>
			}
			onCancel={onCancel}
			width={600}
		>
			<TransferFormSteps selectedUser={selectedUser} />
		</Modal>
	);
};

export default TransferMoneyModal;
