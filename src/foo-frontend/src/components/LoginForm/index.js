import React, { useState } from 'react';
import { Button, Form, Input } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { withRouter } from 'react-router-dom';
import { hanleLogin } from '../../services/login';
import './LoginForm.css'

export default function LoginForm(props) {
	const FormItem = Form.Item;
	// Set state
	const [ serverValidation, setServerValidation ] = useState({
		visible: false,
		validateStatus: 'error',
		errorMsg: 'Invalid username or password!'
	});

	let handleSubmit = (e) => {
		props.form.validateFields((err, data) => {
			if (!err) {
				hanleLogin(data.username, data.password)
					.then(() => {
						props.history.push('/');
					})
					.catch(() => {
						props.form.resetFields();
						// setError(true);
					});
			}
		});
	};

	// const { getFieldDecorator } = props.form;

	return (
		<Form onSubmit={handleSubmit} className="login-form">
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
			{serverValidation.visible ? (
				<FormItem validateStatus={serverValidation.validateStatus} help={serverValidation.errorMsg} />
			) : null}
			<FormItem>
				<Button type="primary" htmlType="submit" className="login-form-button">
					Log in
				</Button>
			</FormItem>
		</Form>
	);
}

// export const LoginForm = withRouter(Form.create()(NormalLoginForm));
