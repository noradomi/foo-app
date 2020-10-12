import {
	CheckOutlined,
	DollarOutlined,
	EyeInvisibleOutlined,
	EyeTwoTone,
	LeftOutlined,
	MailOutlined,
	UserOutlined
} from '@ant-design/icons';
import { Alert, Button, Descriptions, Divider, Form, Input } from 'antd';
import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { transferMoney } from '../../../services/transfer-money';
import './Step2.css';

const formItemLayout = {
	labelCol: {
		span: 9
	},
	wrapperCol: {
		span: 15
	}
};

function Step2(props) {
	const { selectedUser } = props;
	const stepFormData = useSelector((state) => state.stepFormData);
	const [ confirmLoading, setConfirmLoading ] = useState(false);
	const { amount, description } = stepFormData;
	const dispatch = useDispatch();
	const [ form ] = Form.useForm();

	const onPrev = () => {
		dispatch({
			type: 'SAVE_CURRENT_STEP',
			data: { payload: 'info' }
		});
		dispatch({
			type: 'SAVE_STEP_FORM_DATA',
			data: { amount, description }
		});
	};

	const submitTransferMoney = (confirmPassword) => {
		const request = {
			receiver: selectedUser.id,
			amount: amount,
			description: description,
			confirmPassword: confirmPassword
		};
		transferMoney(request).then((response) => {
			const code = response.getStatus().getCode();
			console.log('Transfer code: ' + code);
			dispatch({
				type: 'SAVE_TRANSFER_CODE_RESPONSE',
				data: { payload: code }
			});
			dispatch({
				type: 'SAVE_CURRENT_STEP',
				data: { payload: 'result' }
			});
		});
	};

	const onValidateForm = () => {
		form.validateFields().then((values) => {
			setConfirmLoading(true);
			setTimeout(() => {
				setConfirmLoading(false);
				submitTransferMoney(values.confirmPassword);
			}, 2000);
		});
	};

	return (
		<Form
			{...formItemLayout}
			form={form}
			layout="horizontal"
			className="step-form"
			initialValues={{ password: '123456' }}
		>
			<Alert
				type="info"
				closable
				message="Vui lòng xác nhận các thông tin đã nhập"
				style={{ marginBottom: 24 }}
				showIcon
			/>
			<Descriptions column={1}>
				<Descriptions.Item
					label={
						<span>
							<UserOutlined style={{ fontSize: '17px', marginRight: '5px', color: '#1890ff' }} /> Người
							nhận
						</span>
					}
				>
					{' '}
					<span style={{ fontWeight: 600 }}>{selectedUser.name}</span>
				</Descriptions.Item>

				<Descriptions.Item
					label={
						<span>
							<DollarOutlined style={{ fontSize: '17px', marginRight: '5px', color: '#1890ff' }} /> Số
							tiền chuyển
						</span>
					}
				>
					<span style={{ color: 'blue', fontWeight: 600 }}>
						{`${amount}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')} VND
					</span>
				</Descriptions.Item>
				<Descriptions.Item
					label={
						<span>
							<MailOutlined style={{ fontSize: '17px', marginRight: '5px', color: '#1890ff' }} /> Lời nhắn
						</span>
					}
				>
					{' '}
					<i>{`"${description}"`}</i>
				</Descriptions.Item>
			</Descriptions>
			<Divider style={{ margin: '24px 0' }} />
			<Form.Item
				style={{ marginBottom: 8 }}
				label="Xác nhận mật khẩu"
				name="confirmPassword"
				required={false}
				rules={[ { required: true, message: 'Vui lòng nhập mật khẩu xác nhận' } ]}
			>
				<Input.Password
					placeholder="Nhập mật khẩu"
					style={{ width: '80%' }}
					iconRender={(visible) => (visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />)}
				/>
			</Form.Item>
			<Form.Item
				style={{ marginBottom: 8 }}
				wrapperCol={{
					xs: { span: 24, offset: 0 },
					sm: {
						span: formItemLayout.wrapperCol.span,
						offset: formItemLayout.labelCol.span
					}
				}}
			>
				<Button onClick={onPrev} style={{ marginRight: 8 }} icon={<LeftOutlined />}>
					Quay lại
				</Button>
				<Button type="primary" onClick={onValidateForm} loading={confirmLoading} icon={<CheckOutlined />}>
					Xác nhận
				</Button>
			</Form.Item>
		</Form>
	);
}

export default Step2;
