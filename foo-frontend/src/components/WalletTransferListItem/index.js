import { Button } from 'antd';
import React, { useState } from 'react';
import grpcApi from '../../services/grpcApi';
import CustomAvatar from '../CustomAvatar';
import TransferMoneyModal from '../TransferMoneyModal';
import './WalletTransferListItem.css';

function WalletTransferListItem(props) {
	const { id, name, avatar } = props.data;
	const [ btnVisbale, setBtnVisible ] = useState('visible');
	const [ loadings, setLoadings ] = useState([]);
	const [ visible, setVisible ] = useState(false);

	const onCreate = (values) => {
		console.log('Received values of form: ', values);

		const request = {
			receiver: values.receiver,
			amount: values.money.number,
			description: values.description,
			confirmPassword: values.confirmPassword
		};
		grpcApi.transferMoney(request, (err, response) => {
			const code = response.getStatus().getCode();
			console.log('Code = ' + code);
		});
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
			<Button
				onClick={() => setVisible(true)}
				className="addfriend-btn"
				size={'small'}
				// loading={loadings[0]}
			>
				<span>Chuyển tiền</span>
			</Button>
			<TransferMoneyModal
				userInfo={props.data}
				visible={visible}
				onCreate={onCreate}
				onCancel={() => {
					setVisible(false);
				}}
			/>
		</div>
	);
}

export default WalletTransferListItem;
