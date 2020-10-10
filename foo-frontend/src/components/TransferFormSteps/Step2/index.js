import React from 'react';
import { Form, Alert, Button, Descriptions, Divider, Statistic, Input } from 'antd';
import styles from './index.less';
import { useDispatch, useSelector } from 'react-redux';

const formItemLayout = {
	labelCol: {
		span: 5
	},
	wrapperCol: {
		span: 19
	}
};

function Step2(props) {
	const [ form ] = Form.useForm();
	const stepFormData = useSelector((state) => state.stepFormData);
	const dispatch = useDispatch();

	const onPrev = () => {
		console.log('Step 2 : back');
		dispatch({
			type: 'SAVE_CURRENT_STEP',
			data: { payload: 'info' }
		});
		dispatch({
			type: 'SAVE_STEP_FORM_DATA',
			data: { amount: 10000, description: 'Test', receiver: 'Noradomi' }
		});
	};

	const onValidateForm = async () => {
		if (dispatch) {
			dispatch({
				type: 'SAVE_CURRENT_STEP',
				data: { payload: 'result' }
			});
		}
	};

	const { amount, description, receiver } = stepFormData;
	return (
		<Form
			{...formItemLayout}
			form={form}
			layout="horizontal"
			className={styles.stepForm}
			initialValues={{ password: '123456' }}
		>
			<Alert closable showIcon message="确认转账后，资金将直接打入对方账户，无法退回。" style={{ marginBottom: 24 }} />
			<Descriptions column={1}>
				<Descriptions.Item label="Người nhận"> {receiver}</Descriptions.Item>
				<Descriptions.Item label="Lời nhắn"> {description}</Descriptions.Item>
				<Descriptions.Item label="Số tiền">
					<Statistic value={amount} suffix="VND" />
				</Descriptions.Item>
			</Descriptions>
			<Divider style={{ margin: '24px 0' }} />
			<Form.Item
				label="支付密码"
				name="password"
				required={false}
				rules={[ { required: true, message: '需要支付密码才能进行支付' } ]}
			>
				<Input type="password" autoComplete="off" style={{ width: '80%' }} />
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
				<Button type="primary" onClick={onValidateForm}>
					Gửi
				</Button>
				<Button onClick={onPrev} style={{ marginLeft: 8 }}>
					Quay lại
				</Button>
			</Form.Item>
		</Form>
	);
}

export default Step2;
