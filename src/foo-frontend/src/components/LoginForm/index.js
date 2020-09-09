import React, { useState } from 'react';
import { Button, Form, Input, Alert } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { hanleLogin } from '../../services/login';
import { useHistory } from 'react-router-dom';
import './LoginForm.css';

export default function LoginForm(props) {
	const FormItem = Form.Item;
	let [ form ] = Form.useForm();
	let history = useHistory();

	let [ message, setMessage ] = useState(false);

	let handleSubmit = (data) => {
		hanleLogin(data.username, data.password)
			.then(() => {
				history.push('/');
			})
			.catch(() => {
				setMessage(true);
				// form.resetFields();
			});
	};

	let cleanMessage = (event) => {
		setMessage(false);
	};

	let alert = null;
	if (message) {
		alert = (
			<FormItem>
				<Alert message="Invalid username or password" type="error" showIcon />
			</FormItem>
		);
	}

	return (
		<Form onFinish={handleSubmit} className="login-form" onFieldsChange={cleanMessage} form={form}>
			<FormItem name="username" rules={[ { required: true, message: 'Please input your username!' } ]}>
				<Input prefix={<UserOutlined style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Username" />
			</FormItem>

			<FormItem name="password" rules={[ { required: true, message: 'Please input your password!' } ]}>
				<Input
					prefix={<LockOutlined style={{ color: 'rgba(0,0,0,.25)' }} />}
					type="password"
					placeholder="Password"
				/>
			</FormItem>

			{alert}

			<FormItem>
				<Button type="primary" htmlType="submit" className="login-form-button">
					Log in
				</Button>
			</FormItem>
		</Form>
	);
}
