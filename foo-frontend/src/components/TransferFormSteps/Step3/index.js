import { Button, Result, Descriptions, Statistic } from 'antd';
import React from 'react';
import styles from './index.less';
import { useSelector } from 'react-redux';

function Step3(props){

  const stepFormData = useSelector((state) => state.stepFormData);
  const { amount, description, receiver } = stepFormData;

  const onFinish = () => {
    
  };
  const information = (
    <div className={styles.information}>
      <Descriptions column={1}>
        <Descriptions.Item label="收款账户"> {receiver}</Descriptions.Item>
        <Descriptions.Item label="收款人姓名"> {description}</Descriptions.Item>
        <Descriptions.Item label="转账金额">
          <Statistic value={amount} suffix="元" />
        </Descriptions.Item>
      </Descriptions>
    </div>
  );
  const extra = (
    <>
      <Button type="primary" onClick={onFinish}>
        再转一笔
      </Button>
      <Button>查看账单</Button>
    </>
  );
  return (
    <Result
      status="success"
      title="Thành công"
      subTitle=""
      extra={extra}
      className={styles.result}
    >
      {information}
    </Result>
  );
};

export default Step3;