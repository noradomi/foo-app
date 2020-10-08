import React from 'react';
import AddressBook from '../AddressBook';
import Toolbar from '../Toolbar';
import './TransactionHistory.css';
import { HistoryOutlined } from '@ant-design/icons';

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
