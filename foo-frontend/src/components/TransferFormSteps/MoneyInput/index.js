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
		if (Number.isNaN(parseInt(e.target.value))) {
			return;
		}
		const newNumber = parseInt(e.target.value.replace(/\$\s?|(,*)/g, '') || 0, 10);

		if (!('number' in value)) {
			setNumber(0);
		}

		triggerChange({
			number: newNumber
		});
	};
	return (
		<Input
			type="text"
			value={`${value.number}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',') || number}
			onChange={onNumberChange}
			placeholder="Input money"
			suffix="VND"
		/>
	);
};

export default MoneyInput;
