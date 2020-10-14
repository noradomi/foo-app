import { FireFilled, WalletFilled } from '@ant-design/icons';
import { Badge, Tooltip } from 'antd';
import React from 'react';
import { useSelector } from 'react-redux';

WalletTab.propTypes = {};

function WalletTab(props) {
	const unseenTransfer = useSelector((state) => state.unseenTransfer);
	return (
		<Tooltip placement="topLeft" title="Ví">
			{unseenTransfer === true ? (
				<Badge count={<FireFilled style={{ color: '#f5222d', fontSize: '15px' }} />}>
					<WalletFilled style={{ fontSize: 25 }} />
				</Badge>
			) : (
				<WalletFilled style={{ fontSize: 25 }} />
			)}
		</Tooltip>
	);
}

export default WalletTab;
