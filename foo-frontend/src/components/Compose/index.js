import { DollarCircleOutlined, SendOutlined } from '@ant-design/icons';
import { Button, Input, Tooltip } from 'antd';
import React, { useState } from 'react';
import { useDispatch } from 'react-redux';
import TransferMoneyModal from '../TransferMoneyModal';
import './Compose.css';

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

	const handleClickSend = () => {
		const message = text;
		if (message.trim().length !== 0) {
			props.onSendMessage(message);
		}
		setText('');
	};

	const [ visible, setVisible ] = useState(false);

	const onCreate = (values) => {
		setVisible(false);
	};

	return (
		<div className="compose">
			<TextArea
				value={text}
				placeholder={`Nhập tin nhắn tới ${props.selectedUserName}`}
				onKeyPress={handleKeyPress}
				onChange={handleOnChange}
				autoSize={{ minRows: 1, maxRows: 3 }}
				style={{ borderRadius: '10px', marginRight: '10px' }}
			/>
			<Tooltip placement="topLeft" title={'Chuyển tiền'}>
				<Button
					className="compose-button-transfer"
					type="primary"
					shape="circle"
					icon={<DollarCircleOutlined />}
					size={'large'}
					onClick={() => {
						setVisible(true);
					}}
				/>
			</Tooltip>
			<Tooltip placement="topLeft" title={'Gửi'}>
				<Button
					className="compose-button-send"
					type="primary"
					shape="circle"
					size={'large'}
					onClick={handleClickSend}
				>
					<SendOutlined />
				</Button>
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
		</div>
	);
}

export default Compose;
