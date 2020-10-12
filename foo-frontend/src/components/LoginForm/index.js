import React, { useState } from 'react';
import { Button, Form, Input, Alert, message } from 'antd';
import { UserOutlined, LockOutlined, LoginOutlined } from '@ant-design/icons';
import { hanleLogin } from '../../services/login';
import { useHistory } from 'react-router-dom';
import './LoginForm.css';

export default function LoginForm(props) {
	const FormItem = Form.Item;
	let [ form ] = Form.useForm();
	let history = useHistory();

	let handleSubmit = (data) => {
		hanleLogin(data.username, data.password)
			.then(() => {
				history.push('/');
			})
			.catch(() => {
				message.error('Tên đăng nhập hoặc mật khẩu không đúng !');
			});
	};

	return (
		<Form onFinish={handleSubmit} className="login-form" form={form}>
			<FormItem name="username" rules={[ { required: true, message: '(*) Vui lòng nhập tên đăng nhập !' } ]}>
				<Input prefix={<UserOutlined style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Tên đăng nhập" />
			</FormItem>

			<FormItem name="password" rules={[ { required: true, message: '(*) Vui lòng nhập mật khẩu !' } ]}>
				<Input
					prefix={<LockOutlined style={{ color: 'rgba(0,0,0,.25)' }} />}
					type="password"
					placeholder="Mật khẩu"
				/>
			</FormItem>

			<FormItem className="portal-button">
				<Button type="primary" htmlType="submit" className="login-form-button" icon={<LoginOutlined />}>
					Đăng nhập
				</Button>
			</FormItem>
		</Form>
	);
}
