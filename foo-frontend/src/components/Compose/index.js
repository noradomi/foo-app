import { MoneyCollectOutlined, NotificationOutlined, SendOutlined, DollarOutlined } from '@ant-design/icons';
import { Button, Input, Tooltip } from 'antd';
import React, { useState } from 'react';
import TransferMoneyModal from '../TransferMoneyModal';
import './Compose.css';
import { useDispatch } from 'react-redux';

const { TextArea } = Input;

function Compose(props) {
	const [ text, setText ] = useState('');
	const dispatch = useDispatch();

	const handleOnChange = (event) => {
		const text = event.target.value;
		setText(text);
	};

	const handleKeyPress = (event) => {
		if (event.key === 'Enter') {
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
		setVisible(false);
	};

	return (
		<div className="compose">
			<TextArea
				value={text}
				placeholder="Nhập @, tin nhắn ..."
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
			<Tooltip placement="topLeft" title={'Chuyển tiền'}>
				<Button
					className="compose-button"
					type="primary"
					shape="circle"
					icon={<DollarOutlined />}
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
				userInfo={null}
				visible={visible}
				onCreate={onCreate}
				onCancel={() => {
					dispatch({ type: 'RESET_STEP_FORM', data: {} });
					setVisible(false);
				}}
			/>

			{/* {props.rightItems} */}
		</div>
	);
}

export default Compose;
