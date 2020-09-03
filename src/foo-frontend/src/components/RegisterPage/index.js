import React, {useState} from 'react';
import {Form, Input, Button, Row, Col, Card, Alert} from "antd";
import {handleRegister} from "../../services/register";
import {useHistory} from 'react-router-dom';
import bg from './backgroung.jpg';

const layout = {
	labelCol: { span: 10 },
	wrapperCol: { span: 14 }
};

const tailLayout = {
	wrapperCol: { sm: { offset: 10, span: 14 } }
};

const rowStyle = {
	minHeight: '100vh',
	backgroundImage: `url(${bg})`,
	backgroundSize: 'cover'
};

const colStyle = {
	border: '1px solid #e2dede',
	padding: '12px',
	borderRadius: '4px',
	marginTop: '10vh',
	marginBottom: '10vh',
	backgroundColor: 'white'
};

function RegisterPage() {
	let history = useHistory();
	let [ form ] = Form.useForm();
	let [ error, setError ] = useState(false);

    let msg = null;
    
	let handleSubmit = (data) => {
		handleRegister(data.username, data.fullname, data.password)
			.then((value) => {
            
                // msg = <Alert message="Register successfully." type="success" showIcon />;
                // console.log("shown msg");
				history.push('/login');
			})
			.catch((error) => {
				form.resetFields();
				setError(true);
			});
	};

	let clearMsg = (event) => {
		setError(false);
	};

	
	if (error) {
		msg = <Alert message="Have an error when registering." type="error" showIcon />;
    }
    

	return (
		<Row style={rowStyle} align="top">
			<Col
				xs={{ span: 22, offset: 1 }}
				sm={{ span: 16, offset: 4 }}
				md={{ span: 12, offset: 6 }}
				lg={{ span: 10, offset: 7 }}
				style={colStyle}
			>
				<h1 style={{ textAlign: 'center' }}>Register</h1>
				<Card bordered={false}>
					<Form
						{...layout}
						name="basic"
						initialValues={{ remember: true }}
						onFinish={handleSubmit}
						onFieldsChange={clearMsg}
						form={form}
					>
						<Form.Item
							label="Username"
							name="username"
							rules={[ { required: true, message: 'Please input your username!' } ]}
						>
							<Input />
						</Form.Item>

						<Form.Item
							label="Full name"
							name="fullname"
							rules={[ { required: true, message: 'Please input your full name!' } ]}
						>
							<Input />
						</Form.Item>

						<Form.Item
							label="Password"
							name="password"
							rules={[ { required: true, message: 'Please input your password!' } ]}
						>
							<Input.Password />
						</Form.Item>

						<Form.Item
							label="Validate password"
							name="vpassword"
							rules={[ { required: true, message: 'Please input your password!' } ]}
						>
							<Input.Password />
						</Form.Item>

						<Form.Item {...tailLayout}>
							<Button
								type="primary"
								htmlType="submit"
								className="register-submit-btn"
								style={{ width: '100%' }}
							>
								Register
							</Button>
                            Or <a href="/login">Login now!</a>
						</Form.Item>
					</Form>
					<Row>
						<Col xs={{ span: 24 }} sm={{ span: 14, offset: 10 }}>
							{msg}
						</Col>
					</Row>
				</Card>
			</Col>
		</Row>
	);
}

export default RegisterPage;
