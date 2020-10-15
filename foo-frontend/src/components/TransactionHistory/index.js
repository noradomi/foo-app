import { HistoryOutlined } from '@ant-design/icons';
import React from 'react';
import AddressBook from '../AddressBook';
import './TransactionHistory.css';
import { Card } from 'antd';

TransactionHistory.propTypes = {};

function TransactionHistory(props) {
	return (
		<Card className="transaction-card">
			<div className="transaction-history-header">
				<HistoryOutlined className="history-icon" />Lịch sử giao dịch
			</div>
			<div className="transaction-history">
				<div className="transaction-list">
					<AddressBook />
				</div>
			</div>
		</Card>
	);
}

export default TransactionHistory;
