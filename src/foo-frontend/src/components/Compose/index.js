import { Input } from 'antd';
import React, { useState } from 'react';
import greeterApi from '../../services/greeterApi';
import './Compose.css';
const { TextArea } = Input;

function Compose(props) {
	const [ text, setText ] = useState('');

	const handleOnChange = (event) => {
		const text = event.target.value;
		setText(text);
	};

	const handleKeyPress = (event) => {
		if (event.key === 'Enter') {
			// greeterApi.sayHello('Noradomi', (err, response) => {
			// 	console.log(response.getResult());
			// });
			event.preventDefault();
			const message = event.target.value;
			if (message.trim().length !== 0) {
				props.onSendMessage(message);
			}
			setText('');
		}
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
			{/* <TextareaAutosize
				value={text}
				className="compose-input"
				placeholder="Write a message ..."
				onKeyPress={handleKeyPress}
				maxRows="2"
				onChange={handleOnChange}
			/> */}
			{props.rightItems}
		</div>
	);
}

export default Compose;
