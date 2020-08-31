import React from 'react';
import { Form, Input, Button, Row, Col, Card, Alert, Checkbox } from 'antd';
import { isAuthenticated } from '../../utils/utils';
import { Redirect } from 'react-router-dom';
import { api } from '../../services/api';
import { hanleLogin } from '../../services/login';
import { setJwtToStorage, setUserIdToStorage } from '../../utils/utils';

const FormItem = Form.Item;

export default function LoginPage(props) {
	let [ form ] = Form.useForm();
	let [ error, setError ] = useState(false);

	handleSubmit = (e) => {
		e.preventDefault();
		hanleLogin(e.username, e.password)
			.then(() => {
				// Login success, route to chat page.
				this.props.history.push('/');
			})
			.catch(() => {});
	};

	let cleanMsg = (event) => {
		setError(false);
	};

	let msg = null;
	if (error) {
		msg = <Alert message="Invalid username or password" type="error" showIcon />;
	}

	return (
		<Row style={rowStyle} align="top">
			<Col
				xs={{ span: 22, offset: 1 }}
				sm={{ span: 16, offset: 4 }}
				md={{ span: 10, offset: 7 }}
				lg={{ span: 8, offset: 8 }}
				style={colStyle}
			>
				<h1 style={{ textAlign: 'center' }}>Login</h1>
				<Card bordered={false}>
					<Form
						form={form}
						onFinish={hanldeLoginForm}
						className="login-form"
						name="basic"
						initialValues={{ remember: true }}
						onFieldsChange={cleanMsg}
					>
						<Form.Item
							name="username"
							rules={[ { required: true, message: 'Please input your Username!' } ]}
						>
							<Input prefix={<UserOutlined className="site-form-item-icon" />} placeholder="Username" />
						</Form.Item>
						<Form.Item
							name="password"
							rules={[ { required: true, message: 'Please input your Password!' } ]}
						>
							<Input
								prefix={<LockOutlined className="site-form-item-icon" />}
								type="password"
								placeholder="Password"
							/>
						</Form.Item>
						<Form.Item>
							<Form.Item name="remember" valuePropName="checked" noStyle>
								<Checkbox>Remember me</Checkbox>
							</Form.Item>

							<a style={{ float: 'right' }} href="">
								Forgot password
							</a>
						</Form.Item>

						<Form.Item>
							<Button type="primary" htmlType="submit" style={{ width: '100%' }}>
								Log in
							</Button>
							Or <a href="">Register now!</a>
						</Form.Item>
					</Form>
					{msg}
				</Card>
			</Col>
		</Row>
	);
}
