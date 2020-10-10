import { EyeInvisibleOutlined, EyeTwoTone } from '@ant-design/icons';
import { Button, Form, Input, Select } from 'antd';
import TextArea from 'antd/lib/input/TextArea';
import React from 'react';
import MoneyInput from '../MoneyInput';
import { useDispatch } from 'react-redux';

const { Option } = Select;

const formItemLayout = {
	labelCol: {
		span: 5
	},
	wrapperCol: {
		span: 19
	}
};

function Step1(props) {
	const dispatch = useDispatch();
	const [ form ] = Form.useForm();
	const onValidateForm = () => {
		console.log('Step 1: submite');
		dispatch({
			type: 'SAVE_CURRENT_STEP',
			data: { payload: 'confirm' }
		});
		dispatch({
			type: 'SAVE_STEP_FORM_DATA',
			data: { amount: 10000, description: 'Test', receiver: 'Noradomi' }
		});
	};

	const checkPrice = (rule, value) => {
		if (value.number >= 1000) {
			if (value.number % 1000 === 0) {
				if (value.number <= 20000000) {
					return Promise.resolve();
				} else {
					return Promise.reject('Số tiền gửi tối đa là 20,000,000 VND');
				}
			} else {
				return Promise.reject('Số tiền gửi phải là bội số của 1,000!');
			}
		} else {
			return Promise.reject('Số tiền gửi tối thiểu là 1,000 VND !');
		}
	};
	return (
		<Form
			className="transfer-money-form"
			form={form}
			layout="vertical"
			name="form_in_modal"
			initialValues={{
				money: {
					number: 1000
				},
				description: `Chuyển tiền đến`
			}}
		>
			<Form.Item
				name="money"
				label="Số tiền"
				rules={[
					{
						required: true,
						validator: checkPrice
					}
				]}
			>
				<MoneyInput />
			</Form.Item>
			<Form.Item name="description" label="Lời nhắn">
				<TextArea
					className="transfer-modal-description"
					placeholder="Tối đa 120 kí tự"
					allowClear
					maxLength="120"
					autoSize={{ minRows: 2, maxRows: 4 }}
				/>
			</Form.Item>
			<Form.Item
				name="confirmPassword"
				label="Xác nhận mật khẩu"
				rules={[
					{
						required: true,
						message: 'Vui lòng nhập mật khẩu xác nhận'
					}
				]}
			>
				<Input.Password
					placeholder="Nhập mật khẩu"
					iconRender={(visible) => (visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />)}
				/>
			</Form.Item>
			<Form.Item
				wrapperCol={{
					xs: { span: 24, offset: 0 },
					sm: {
						span: formItemLayout.wrapperCol.span,
						offset: formItemLayout.labelCol.span
					}
				}}
			>
				<Button type="primary" onClick={onValidateForm}>
					下一步
				</Button>
			</Form.Item>
		</Form>
	);
}

export default Step1;
