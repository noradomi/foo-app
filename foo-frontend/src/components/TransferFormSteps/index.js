import { Card, Steps } from 'antd';
import React, { useEffect, useState } from 'react';
import styles from './style.less';
import Step3 from './Step3';
import Step1 from './Step1';
import Step2 from './Step2';
import { useSelector } from 'react-redux';

const { Step } = Steps;

const getCurrentStepAndComponent = (current) => {
  switch (current) {
    case 'confirm':
      return { step: 1, component: <Step2 /> };
    case 'result':
      return { step: 2, component: <Step3 /> };
    case 'info':
    default:
      return { step: 0, component: <Step1 /> };
  }
};

export function TransferFormSteps(props){
  const [stepComponent, setStepComponent] = useState(<Step1 />);
  const [currentStep, setCurrentStep] = useState(0);
  const current = useSelector(state => state.currentStep);

  useEffect(() => {
    const { step, component } = getCurrentStepAndComponent(current);
    setCurrentStep(step);
    setStepComponent(component);
  }, [current]);

  return (
    <Card bordered={false} style={{width: "100%"}}>
        <>
          <Steps current={currentStep} className={styles.steps}>
            <Step title="Bước 1" />
            <Step title="Bước 2" />
            <Step title="Bước 3" />
          </Steps>
          {stepComponent}
      
        </>
      </Card>
  );
};
