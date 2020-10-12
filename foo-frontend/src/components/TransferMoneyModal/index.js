import { CloseOutlined, SwapOutlined } from '@ant-design/icons';
import { Form, Input, Modal, Typography } from 'antd';
import 'antd/dist/antd.css';
import React, { useState } from 'react';
import { useSelector } from 'react-redux';
import { TransferFormSteps } from '../TransferFormSteps';
import './TransferMoneyModal.css';
const { TextArea } = Input;
const { Paragraph } = Typography;

const TransferMoneyModal = ({ userInfo, visible,onCancel }) => {
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
  }
  
	return (
		<Modal
			visible={visible}
			destroyOnClose={true}
			title={
        <>
          <div style={{textAlign: "center"}}>
            <span style={{fontSize: "23px"}}> <SwapOutlined style={{fontSize: "30px", color: "rgb(103, 174, 63)"}} /> Chuyển tiền</span>
          </div>
        </>
      
      }
      footer=''
			cancelText={<span><CloseOutlined /> Đóng</span>}
			onCancel={onCancel}
      confirmLoading={confirmLoading}
      width={600}
      
		>
      <TransferFormSteps selectedUser={selectedUser}/>
		</Modal>
	);
};

export default TransferMoneyModal;
