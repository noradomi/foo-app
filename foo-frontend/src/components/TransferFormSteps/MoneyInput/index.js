import { Input } from 'antd';
import 'antd/dist/antd.css';
import React, { useState } from 'react';

const MoneyInput = ({ value = {}, onChange }) => {
	const [ number, setNumber ] = useState(1000);

	const triggerChange = (changedValue) => {
		if (onChange) {
			onChange({
				number,
				...value,
				...changedValue
			});
		}
	};

	const onNumberChange = (e) => {
		const newNumber = parseInt(e.target.value.replace(/\$\s?|(,*)/g, '') || 0, 10);

		if (Number.isNaN(number)) {
			return;
		}

		if (!('number' in value)) {
			setNumber(newNumber);
		}

		triggerChange({
			number: newNumber
		});
	};
	return (
		<span>
			<Input
				type="text"
				value={`${value.number}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',') || number}
				onChange={onNumberChange}
				placeholder="Input money"
				suffix="VND"
			/>
		</span>
	);
};

export default MoneyInput;
