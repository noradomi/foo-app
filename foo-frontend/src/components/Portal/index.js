import React from 'react';
import { Tabs } from 'antd';
import LoginForm from '../LoginForm/index';
import RegisterForm from '../RegisterForm/index';
import { isAuthenticated } from '../../utils/utils';
import { Redirect } from 'react-router-dom';
import './Portal.css';

export default function Portal(props) {
	const TabPane = Tabs.TabPane;

	if (!isAuthenticated()) {
		return <Redirect to="/" />;
	}
	return (
		<div id="portal-container" style={{ backgroundImage: 'url(wallpaper.svg)', backgroundSize: 'cover' }}>
			<div className="logo">
				<img src="logo.gif" alt="logo" />
			</div>
			<div id="authen-panel">
				<Tabs className="portal-content" size={'default'} tabBarStyle={{}}>
					<TabPane tab="ĐĂNG NHẬP" key="1">
						<LoginForm />
					</TabPane>
					<TabPane tab="ĐĂNG KÍ" key="2">
						<RegisterForm />
					</TabPane>
				</Tabs>
			</div>
		</div>
	);
}
