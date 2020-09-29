import { MoneyCollectOutlined, NotificationOutlined, SendOutlined } from '@ant-design/icons';
import { Button, Input, Tooltip } from 'antd';
import React, { useState } from 'react';
import greeterApi from '../../services/greeterApi';
import grpcApi from '../../services/grpcApi';
import TransferMoneyModal from '../TransferMoneyModal';
import './Compose.css';
import { getUserFullNameToStorage } from '../../utils/utils';
const { TextArea } = Input;

function Compose(props) {
	const [ text, setText ] = useState('');

	const handleOnChange = (event) => {
		const text = event.target.value;
		setText(text);
	};

	const handleKeyPress = (event) => {
		if (event.key === 'Enter') {
			// greeterApi.sayHello(getUserFullNameToStorage(), (err, response) => {
			// 	console.log(response.getResult());
			// });
			grpcApi.getBalance('Phuc', (err, response) => {
				console.log(response.getData().getBalance());
			});
			event.preventDefault();
			const message = event.target.value;
			if (message.trim().length !== 0) {
				props.onSendMessage(message);
			}
			setText('');
		}
	};

	const [ visible, setVisible ] = useState(false);

	const onCreate = (values) => {
		console.log('Received values of form: ', values);
		setVisible(false);
	};

	return (
		<div className="compose">
			<TextArea
				value={text}
				placeholder="Write a message ..."
				onKeyPress={handleKeyPress}
				onChange={handleOnChange}
				autoSize={{ minRows: 1, maxRows: 4 }}
				style={{ borderRadius: '10px' }}
			/>
			<Tooltip placement="topLeft" title={'Remind transfer'}>
				<Button
					className="compose-button"
					type="primary"
					shape="circle"
					icon={<NotificationOutlined />}
					size={'large'}
				/>
			</Tooltip>
			<Tooltip placement="topLeft" title={'Transfer money'}>
				<Button
					className="compose-button"
					type="primary"
					shape="circle"
					icon={<MoneyCollectOutlined />}
					size={'large'}
					onClick={() => {
						setVisible(true);
					}}
				/>
			</Tooltip>
			<Tooltip placement="topLeft" title={'Send'}>
				<Button
					className="compose-button"
					type="primary"
					shape="circle"
					icon={<SendOutlined />}
					size={'large'}
				/>
			</Tooltip>
			<TransferMoneyModal
				visible={visible}
				onCreate={onCreate}
				onCancel={() => {
					setVisible(false);
				}}
			/>

			{/* {props.rightItems} */}
		</div>
	);
}

export default Compose;
