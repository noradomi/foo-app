import React from 'react';
import { handleRegister } from '../../services/register';
import { useHistory } from 'react-router-dom';
import { Button, Form, Input, Tooltip,Alert } from 'antd';
import { UserOutlined, LockOutlined,QuestionCircleOutlined } from '@ant-design/icons';
import './RegisterForm.css';

export default function RegisterForm(props) {
	const FormItem = Form.Item;
	let [ form ] = Form.useForm();
	let history = useHistory();

	let handleSubmit = (data) => {
		handleRegister(data.username, data.fullname, data.password)
			.then((value) => {
				// history.push('/login');
				alert = <Alert
				message="Very long warning text warning text text text text text text text"
				banner
				closable
			  />
			})
			.catch((error) => {
				form.resetFields();
			});
	};

	return (
		<Form onFinish={handleSubmit} className="login-form" form={form}>
			<Form.Item
				name="fullname"
				label={
					<span>
						Nickname&nbsp;
						<Tooltip title="Your fullname">
							<QuestionCircleOutlined />
						</Tooltip>
					</span>
				}
				rules={[
					{
						required: true,
						message: 'Please input your fullname!',
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
					placeholder="Fullname"
				/>
			</Form.Item>
			<Form.Item
				name="username"
				rules={[
					{
						required: true,
						message: 'Please input your username!',
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
					placeholder="Username"
				/>
			</Form.Item>
			<Form.Item
				name="password"
				rules={[
					{
						required: true,
						message: 'Please input your password!'
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
				/>
			</Form.Item>
			<Form.Item
				name="confirm"
				dependencies={[ 'password' ]}
				hasFeedback
				rules={[
					{
						required: true,
						message: 'Please confirm your password!'
					},
					({ getFieldValue }) => ({
						validator(rule, value) {
							if (!value || getFieldValue('password') === value) {
								return Promise.resolve();
							}

							return Promise.reject('The two passwords that you entered do not match!');
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
				/>
			</Form.Item>
			<FormItem>
				<Button type="primary" htmlType="submit" className="login-form-button">
					Register
				</Button>
			</FormItem>
			{alert}
		</Form>
	);
}
