import React, {useState} from 'react';
import { handleRegister } from '../../services/register';
import { Button, Form, Input, Alert } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import './RegisterForm.css';

export default function RegisterForm(props) {
	const FormItem = Form.Item;
	let [ form ] = Form.useForm();
	let [message, setMessage] = useState(false);

	let handleSubmit = (data) => {
		console.log(data.username + " "+data.fullname);
		handleRegister(data.username, data.fullname, data.password)
			.then((value) => {
				setMessage(true);
			})
			.catch((error) => {
				form.resetFields();
			});
	};

	let cleanMessage = (event) => {
		setMessage(false);
	};

	let alert = null;
	if(message){
		alert = <Alert message="Register successfully" banner closable showIcon/>
	}

	return (
		<Form onFinish={handleSubmit} className="login-form" form={form} onFieldsChange={cleanMessage}>
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
				name="fullname"
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
