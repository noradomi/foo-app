import { Card, Steps } from 'antd';
import React, { useEffect, useState } from 'react';
import ResultStep from './ResultStep';
import InputStep from './InputStep';
import ValidateStep from './ValidateStep';
import { useSelector } from 'react-redux';

import './TransferFormSteps.css'

const { Step } = Steps;

export function TransferFormSteps(props){
  const selectedUser = props.selectedUser;
  const [stepComponent, setStepComponent] = useState(<InputStep selectedUser={selectedUser}/>);
  const [currentStep, setCurrentStep] = useState(0);
  const [status, setStatus] = useState("process");
  
  const current = useSelector(state => state.currentStep);

  const getCurrentStepAndComponent = (current) => {
    switch (current) {
      case 'confirm':
        return { step: 1, component: <ValidateStep selectedUser={selectedUser}/>, status: "process" };
      case 'result-failed':
        return { step: 2, component: <ResultStep selectedUser={selectedUser}/>, status: "error"};
      case 'result-success':
      return { step: 3, component: <ResultStep selectedUser={selectedUser}/>, status: "finish" };
      case 'info':
      default:
        return { step: 0, component: <InputStep selectedUser={selectedUser}/>, status: "process" };
    }
  };

  useEffect(() => {
    const { step, component, status } = getCurrentStepAndComponent(current);
    setCurrentStep(step);
    setStepComponent(component);
    setStatus(status);
  }, [current]);

  return (
    <Card bordered={false} className="transfer-form-steps-card">
        <>
          <Steps current={currentStep} status={status}  className="transfer-step">
            <Step title="Nhập thông tin" />
            <Step title="Xác nhận" />
            <Step title="Kết quả" />
          </Steps>
          {stepComponent}
      
        </>
      </Card>
  );
};
