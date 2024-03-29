import { LockOutlined, LoginOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Form, Input, message } from 'antd';
import React from 'react';
import { useHistory } from 'react-router-dom';
import { hanleLogin } from '../../services/login';
import './LoginForm.css';

export default function LoginForm(props) {
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
			<Form.Item name="username" rules={[ { required: true, message: '(*) Vui lòng nhập tên đăng nhập !' } ]}>
				<Input prefix={<UserOutlined style={{ color: 'rgba(0,0,0,.25)' }} />} placeholder="Tên đăng nhập" />
			</Form.Item>

			<Form.Item name="password" rules={[ { required: true, message: '(*) Vui lòng nhập mật khẩu !' } ]}>
				<Input
					prefix={<LockOutlined style={{ color: 'rgba(0,0,0,.25)' }} />}
					type="password"
					placeholder="Mật khẩu"
				/>
			</Form.Item>

			<Form.Item className="portal-button">
				<Button type="primary" htmlType="submit" className="login-form-button" icon={<LoginOutlined />}>
					Đăng nhập
				</Button>
			</Form.Item>
		</Form>
	);
}
