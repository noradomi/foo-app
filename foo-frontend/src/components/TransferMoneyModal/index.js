import { CheckOutlined, CloseCircleOutlined, CloseOutlined, EyeInvisibleOutlined, EyeTwoTone, SwapOutlined } from '@ant-design/icons';
import { Form, Input, Modal, Result, Typography } from 'antd';
import 'antd/dist/antd.css';
import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { transferMoney } from '../../services/transfer-money';
import CustomAvatar from '../CustomAvatar';
import './TransferMoneyModal.css';
import { TransferFormSteps } from '../TransferFormSteps';
const { TextArea } = Input;
const { Paragraph } = Typography;

const MoneyInput = ({ value = {}, onChange }) => {
	const [ number, setNumber ] = useState(1000);

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
		const newNumber = parseInt(e.target.value.replace(/\$\s?|(,*)/g, '') || 0, 10);

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
				value={`${value.number}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',') || number}
				onChange={onNumberChange}
				placeholder="Input money"
				suffix="VND"
			/>
		</span>
	);
};

const TransferMoneyModal = ({ userInfo, visible, onCancel }) => {
  const [confirmLoading, setConfirmLoading] = useState(false);
	let selectedUser = null;
	if (userInfo !== null) {
		selectedUser = userInfo;
	} else {
		selectedUser = useSelector((state) => state.selectedUser);
	}

	const [ form ] = Form.useForm();
	const checkPrice = (rule, value) => {
		if (value.number >= 1000) {
			if (value.number % 1000 === 0) {
				if(value.number <= 20000000){
          return Promise.resolve();
        }
        else{
          return Promise.reject('Số tiền gửi tối đa là 20,000,000 VND')
        }
			} else {
				return Promise.reject('Số tiền gửi phải là bội số của 1,000!');
			}
		} else {
			return Promise.reject('Số tiền gửi tối thiểu là 1,000 VND !');
		}
	};

	const responseSuccess = (amount, balance, receiverName) => {
    amount = `${amount}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    balance = `${balance}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    const title = `- ${amount} VND`;
		Modal.info({
			title: '',
			content: (
				<Result
          status="success"
          title={title}
          subTitle={
            <p>{receiverName}</p>
          }
          extra={[
            <p >Số dư còn lại: <span className="response-success-balance">{balance} VND</span> </p>,
          ]}
        />
			),
			centered: true,
			okText: 'Đóng',
			onOk() {}
		});
  };
  
  const responseServerError = () => {
		Modal.info({
			title: '',
			content: (
				<Result
          status="500"
          title="500"
          subTitle="Lỗi hệ thống, vui lòng thử lại."
        />
			),
			centered: true,
			okText: 'Đóng',
			onOk() {}
		});
	};

	const responseSubmissionFailed = (code) => {
    let messageError = null;
    switch (code) {
      case 1:
        messageError = "Số dư hiện tại không đủ để thực hiện giao dịch. Thử lại !";
        break;
      case 2:
        messageError = "Mật khẩu xác nhận không đúng. Thử lại !";
        break;
      default:
        break;
    }
		Modal.info({
			title: '',
			content: (
        <Result
          status="error"
          title="Giao dịch thất bại"
          subTitle={
            <Paragraph>
              <CloseCircleOutlined style={{color: "red"}}/> {messageError}
            </Paragraph>
        
          }
        >
        </Result>
          
			),
			centered: true,
			okText: 'Đóng',
			onOk() {Modal.destroyAll();}
		});
  };
  
  const submitTransferMoney = (values) => {
    const request = {
			receiver: values.receiver,
			amount: values.money.number,
			description: values.description,
			confirmPassword: values.confirmPassword
		};
		transferMoney(request).then((response) => {
      const code = response.getStatus().getCode();
      console.log("Transfer code: "+code);
      if(code === 0){
        const balance = response.getData().getBalance();
        responseSuccess(values.money.number,balance, selectedUser.name);
      }
      else if(code === 4){
        responseServerError();
      }
      else{
        responseSubmissionFailed(code);
      }
    });
  }

	const handleOk = () => {
    form
			.validateFields()
			.then((values) => {
        // form.resetFields();
        setConfirmLoading(true);
         setTimeout(() => {
            setConfirmLoading(false);
            values.receiver = selectedUser.id;
        submitTransferMoney(values);
          }, 2000);
        
				
        // onCreate(values);
			})
			.catch((info) => {
				console.log('Validate Failed:', info);
			});
	};
	return (
		<Modal
			visible={visible}
			destroyOnClose={true}
			title={
        <>
          <div style={{textAlign: "center"}}>
            <span style={{fontSize: "23px"}}> <SwapOutlined style={{fontSize: "30px", color: "rgb(103, 174, 63)"}} /> Chuyển tiền đến</span>
          </div>
        </>
      
      }
      headStyle={{ backgroundColor: 'red' }}
			okText={<span><CheckOutlined /> Chuyển tiền</span>}
			cancelText={<span><CloseOutlined /> Hủy</span>}
			onCancel={onCancel}
      onOk={handleOk}
      confirmLoading={confirmLoading}
		>
			{/* <div style={{ margin: '0 auto', textAlign: 'center' }}>
				<CustomAvatar type="user-avatar" avatar={selectedUser.avatar} />
				<p className="transfer-user-name">{selectedUser.name}</p>
			</div>
			<Form
        className="transfer-money-form"
				form={form}
				layout="vertical"
				name="form_in_modal"
				initialValues={{
					money: {
						number: 1000
					},
					description: `Chuyển tiền đến ${selectedUser.name}`
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
				<Form.Item name="confirmPassword" label="Xác nhận mật khẩu" rules={[
              {
                required: true,
                message: 'Vui lòng nhập mật khẩu xác nhận',
              }
            ]}>
					<Input.Password
						placeholder="Nhập mật khẩu"
            iconRender={(visible) => (visible ? <EyeTwoTone /> : <EyeInvisibleOutlined />)}
            
					/>
				</Form.Item>
			</Form> */}
      <TransferFormSteps/>
		</Modal>
	);
};

export default TransferMoneyModal;
