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
  
  const current = useSelector(state => state.currentStep);

  const getCurrentStepAndComponent = (current) => {
    switch (current) {
      case 'confirm':
        return { step: 1, component: <ValidateStep selectedUser={selectedUser}/> };
      case 'result':
        return { step: 2, component: <ResultStep selectedUser={selectedUser}/> };
      case 'info':
      default:
        return { step: 0, component: <InputStep selectedUser={selectedUser}/> };
    }
  };

  useEffect(() => {
    const { step, component } = getCurrentStepAndComponent(current);
    setCurrentStep(step);
    setStepComponent(component);
  }, [current]);

  return (
    <Card bordered={false} className="transfer-form-steps-card">
        <>
          <Steps current={currentStep} className="transfer-step">
            <Step title="Nhập thông tin" />
            <Step title="Xác nhận" />
            <Step title="Kết quả" />
          </Steps>
          {stepComponent}
      
        </>
      </Card>
  );
};
