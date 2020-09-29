import { MoreOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Dropdown, Menu } from 'antd';
import React from 'react';
import { useHistory } from 'react-router-dom';
import { hanldeLogout } from '../../services/logout';
import SignOutButton from '../SignOutButton';
import './MenuInfo.css';

function MenuInfo(props) {
	let history = useHistory();
	const menu = (
		<Menu style={{ width: 150 }}>
			<Menu.Item
				key="1"
				className="menu-info-item"
				style={{ fontSize: '15px' }}
				icon={<UserOutlined style={{ fontSize: '15px' }} />}
			>
				Profile
			</Menu.Item>
			<Menu.Item className="menu-info-item" key="2" style={{ fontSize: '15px' }}>
				<SignOutButton
					key="add"
					icon="ion-ios-log-out"
					onClick={() => {
						hanldeLogout().then(() => history.push('/login'));
					}}
				/>{' '}
				Sign Out
			</Menu.Item>
		</Menu>
	);

	return (
		<div id="components-dropdown-demo-dropdown-button">
			<Dropdown trigger={[ 'click' ]} overlay={menu} placement="bottomRight">
				<Button className="menu-info-button" style={{ border: '0px' }}>
					<MoreOutlined style={{ fontSize: '30px', fontWeight: '600' }} />
				</Button>
			</Dropdown>
		</div>
	);
}

export default MenuInfo;
