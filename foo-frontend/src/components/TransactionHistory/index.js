import { HistoryOutlined } from '@ant-design/icons';
import React from 'react';
import AddressBook from '../AddressBook';
import './TransactionHistory.css';

TransactionHistory.propTypes = {};

function TransactionHistory(props) {
	return (
		<div className="transaction-history">
			<div className="transaction-history-header">
				<HistoryOutlined className="history-icon" /> Lịch sử giao dịch
			</div>
			<div className="transaction-list">
				<AddressBook />
			</div>
		</div>
	);
}

export default TransactionHistory;
