import { LockOutlined, UserAddOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Form, Input, message } from 'antd';
import React from 'react';
import { handleRegister } from '../../services/register';
import './RegisterForm.css';

export default function RegisterForm(props) {
	const FormItem = Form.Item;
	let [ form ] = Form.useForm();

	let handleSubmit = (data) => {
		console.log(data.username + ' ' + data.fullname);
		handleRegister(data.username, data.fullname, data.password)
			.then((value) => {
				message.success('Đăng kí thành công. Bạn có thể đăng nhập ngay bây giờ !');
			})
			.catch((error) => {
				form.resetFields();
			});
	};

	return (
		<Form onFinish={handleSubmit} className="login-form" form={form}>
			<Form.Item
				name="username"
				rules={[
					{
						required: true,
						message: 'Vui lòng nhập tên đăng nhập!',
						whitespace: false
					}
				]}
			>
				<Input
					prefix={
						<UserOutlined
							style={{
								color: 'rgba(0,0,0,.25)'
							}}
						/>
					}
					placeholder="Tên đăng nhập"
				/>
			</Form.Item>
			<Form.Item
				name="fullname"
				rules={[
					{
						required: true,
						message: 'Vui lòng nhập tên đầy đủ của bạn!',
						whitespace: true
					}
				]}
			>
				<Input
					prefix={
						<UserOutlined
							style={{
								color: 'rgba(0,0,0,.25)'
							}}
						/>
					}
					placeholder="Tên của bạn"
				/>
			</Form.Item>
			<Form.Item
				name="password"
				rules={[
					{
						required: true,
						message: 'Vui lòng nhập mật khẩu!'
					}
				]}
				hasFeedback
			>
				<Input.Password
					prefix={
						<LockOutlined
							style={{
								color: 'rgba(0,0,0,.25)'
							}}
						/>
					}
					placeholder="Mật khẩu"
				/>
			</Form.Item>
			<Form.Item
				name="confirm"
				dependencies={[ 'password' ]}
				hasFeedback
				rules={[
					{
						required: true,
						message: 'Vui lòng nhập mật khẩu xácnhậnn!'
					},
					({ getFieldValue }) => ({
						validator(rule, value) {
							if (!value || getFieldValue('password') === value) {
								return Promise.resolve();
							}

							return Promise.reject('Mật khẩu xác nhận chưa khớp');
						}
					})
				]}
			>
				<Input.Password
					prefix={
						<LockOutlined
							style={{
								color: 'rgba(0,0,0,.25)'
							}}
						/>
					}
					placeholder="Mật khẩu xác nhận"
				/>
			</Form.Item>
			<FormItem>
				<Button type="primary" htmlType="submit" className="login-form-button" icon={<UserAddOutlined />}>
					Đăng kí
				</Button>
			</FormItem>
			{alert}
		</Form>
	);
}
