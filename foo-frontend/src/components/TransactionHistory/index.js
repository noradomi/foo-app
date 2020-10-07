import React from 'react';
import AddressBook from '../AddressBook';
import Toolbar from '../Toolbar';
import './TransactionHistory.css';

TransactionHistory.propTypes = {};

function TransactionHistory(props) {
	return (
		<div className="transaction-history">
			<Toolbar title="Lịch sử giao dịch" />
			<div className="transaction-list">
				<AddressBook />
			</div>
		</div>
	);
}

export default TransactionHistory;
