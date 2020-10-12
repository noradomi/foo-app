import { Button, Form } from 'antd';
import TextArea from 'antd/lib/input/TextArea';
import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import CustomAvatar from '../../CustomAvatar';
import MoneyInput from '../MoneyInput';
import './Step1.css';
import { RightOutlined } from '@ant-design/icons';

const layout = {
	labelCol: {
		span: 5
	},
	wrapperCol: {
		span: 19
	}
};

const formItemLayout = {
	labelCol: {
		span: 5
	},
	wrapperCol: {
		span: 19
	}
};

function Step1(props) {
	const stepFormData = useSelector((state) => state.stepFormData);

	const selectedUser = props.selectedUser;

	const dispatch = useDispatch();

	const [ form ] = Form.useForm();
	const onValidateForm = () => {
		form.validateFields().then((values) => {
			dispatch({
				type: 'SAVE_CURRENT_STEP',
				data: { payload: 'confirm' }
			});
			dispatch({
				type: 'SAVE_STEP_FORM_DATA',
				data: { amount: values.money.number, description: values.description }
			});
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
		<div style={{ margin: '40px auto 0' }}>
			<div style={{ margin: '0 auto', textAlign: 'center' }}>
				<CustomAvatar type="user-avatar" avatar={selectedUser.avatar} />
				<p className="transfer-user-name">{selectedUser.name}</p>
			</div>
			<Form
				{...layout}
				className="step-one-form"
				form={form}
				layout="horizontal"
				hideRequiredMark
				name="form_in_modal"
				initialValues={{
					money: {
						number: stepFormData.amount || 1000
					},
					description: stepFormData.description || `Chuyển tiền đến`
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
						autoSize={{ minRows: 4, maxRows: 6 }}
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
					<Button type="primary" onClick={onValidateForm} icon={<RightOutlined />}>
						Tiếp theo
					</Button>
				</Form.Item>
			</Form>
		</div>
	);
}

export default Step1;
