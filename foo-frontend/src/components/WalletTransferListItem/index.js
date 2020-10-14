import { SwapOutlined } from '@ant-design/icons';
import { Button } from 'antd';
import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import CustomAvatar from '../CustomAvatar';
import TransferMoneyModal from '../TransferMoneyModal';
import './WalletTransferListItem.css';

function WalletTransferListItem(props) {
	const { name, avatar } = props.data;
	const [ visible, setVisible ] = useState(false);
	const dispatch = useDispatch();

	const onCreate = (values) => {
		setVisible(false);
	};
	return (
		<div className="addfriend-list-item">
			<div style={{ width: 40 }}>
				<CustomAvatar type="panel-avatar" avatar={avatar} />
			</div>
			<div className="addfriend-info" style={{ overflow: 'hidden', paddingTop: 5 }}>
				<div className="addfriend-name">{name}</div>
			</div>
			<Button icon={<SwapOutlined />} onClick={() => setVisible(true)} className="addfriend-btn" size={'small'}>
				<span>Chuyển tiền</span>
			</Button>
			<TransferMoneyModal
				userInfo={props.data}
				visible={visible}
				onCreate={onCreate}
				onCancel={() => {
					dispatch({ type: 'RESET_STEP_FORM', data: {} });
					setVisible(false);
				}}
			/>
		</div>
	);
}

export default WalletTransferListItem;
