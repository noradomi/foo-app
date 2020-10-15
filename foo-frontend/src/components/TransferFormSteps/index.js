import { Card, Steps } from 'antd';
import React, { useEffect, useState } from 'react';
import Step3 from './Step3';
import Step1 from './Step1';
import Step2 from './Step2';
import { useSelector } from 'react-redux';

import './TransferFormSteps.css'

const { Step } = Steps;

export function TransferFormSteps(props){
  const selectedUser = props.selectedUser;
  const [stepComponent, setStepComponent] = useState(<Step1 selectedUser={selectedUser}/>);
  const [currentStep, setCurrentStep] = useState(0);
  
  const current = useSelector(state => state.currentStep);

  const getCurrentStepAndComponent = (current) => {
    switch (current) {
      case 'confirm':
        return { step: 1, component: <Step2 selectedUser={selectedUser}/> };
      case 'result':
        return { step: 2, component: <Step3 selectedUser={selectedUser}/> };
      case 'info':
      default:
        return { step: 0, component: <Step1 selectedUser={selectedUser}/> };
    }
  };

  useEffect(() => {
    const { step, component } = getCurrentStepAndComponent(current);
    setCurrentStep(step);
    setStepComponent(component);
  }, [current]);

  return (
    <Card bordered={false} style={{width: "100%"}}>
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
