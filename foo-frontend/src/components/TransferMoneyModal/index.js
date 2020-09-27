import { UserOutlined } from '@ant-design/icons';
import { Avatar, Form, Input, Modal } from 'antd';
import 'antd/dist/antd.css';
import React, { useState } from 'react';
import './TransferMoneyModal.css';
const { TextArea } = Input;

const MoneyInput = ({ value = {}, onChange }) => {
	const [ number, setNumber ] = useState(0);

	const triggerChange = (changedValue) => {
		if (onChange) {
			onChange({
				number,
				...value,
				...changedValue
			});
		}
	};

	const onNumberChange = (e) => {
		const newNumber = parseInt(e.target.value || 0, 10);

		if (Number.isNaN(number)) {
			return;
		}

		if (!('number' in value)) {
			setNumber(newNumber);
		}

		triggerChange({
			number: newNumber
		});
	};
	return (
		<span>
			<Input
				type="text"
				value={value.number || number}
				onChange={onNumberChange}
				placeholder="Input money"
				suffix="VND"
			/>
		</span>
	);
};

const TransferMoneyModal = ({ visible, onCreate, onCancel }) => {
	const [ form ] = Form.useForm();
	const checkPrice = (rule, value) => {
		if (value.number > 0) {
			return Promise.resolve();
		}

		return Promise.reject('Money transfer must be greater than zero!');
	};
	return (
		<Modal
			visible={visible}
			destroyOnClose={true}
			title="Transfer Money"
			okText="Submit"
			cancelText="Cancel"
			onCancel={onCancel}
			onOk={() => {
				form
					.validateFields()
					.then((values) => {
						console.log(values);

						form.resetFields();
						onCreate(values);
					})
					.catch((info) => {
						console.log('Validate Failed:', info);
					});
			}}
		>
			<div style={{ margin: '0 auto', textAlign: 'center' }}>
				<Avatar size={64} icon={<UserOutlined />} />
				<p>Noradomi</p>
			</div>
			<Form
				form={form}
				layout="vertical"
				name="form_in_modal"
				initialValues={{
					money: {
						number: 0
					},
					description: ''
				}}
			>
				<Form.Item
					name="money"
					label="Money"
					rules={[
						{
							validator: checkPrice
						}
					]}
				>
					<MoneyInput />
				</Form.Item>
				<Form.Item name="description" label="Message">
					<TextArea
						className="transfer-modal-description"
						placeholder="Less equal than 120 characters"
						allowClear
						maxLength="120"
						autoSize={{ minRows: 2, maxRows: 4 }}
					/>
				</Form.Item>
			</Form>
		</Modal>
	);
};

export default TransferMoneyModal;
