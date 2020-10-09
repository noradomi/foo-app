import { CommentOutlined, DollarCircleOutlined, NotificationOutlined } from '@ant-design/icons';
import { Layout, Menu, Tooltip, Badge } from 'antd';
import React, { useState } from 'react';
import ConversationList from '../ConversationList';
import CustomAvatar from '../CustomAvatar';
import MessageList from '../MessageList';
import './Messenger.css';
import { connect, useDispatch, useSelector } from 'react-redux';
import UserWallet from '../UserWallet';
import TransactionHistory from '../TransactionHistory';
import AddressBook from '../AddressBook';
import { setActiveTabKeyAction } from '../../actions/fooAction';

const { Sider } = Layout;

function Messenger(props) {
	const activeTabKey = useSelector((state) => state.activeTabKey);
	const dispatch = useDispatch();

	const handleSideBarChange = (e) => {
		dispatch(setActiveTabKeyAction(e.key));
	};
	return (
		<div style={{ height: 100 + 'vh' }}>
			<Layout style={{ height: 100 + 'vh' }}>
				<Sider
					breakpoint="lg"
					collapsedWidth="0"
					onBreakpoint={(broken) => {}}
					onCollapse={(collapsed, type) => {}}
					width="75"
					id="main-side-menu"
				>
					<CustomAvatar type="main-avatar" avatar={props.user.name ? props.user.name : 'Noradomi'} />
					<div className="menu-separation" />
					<Menu
						theme="dark"
						id="side-menu"
						defaultSelectedKeys={[ '1' ]}
						mode="vertical"
						onSelect={handleSideBarChange}
					>
						<Menu.Item
							key="1"
							icon={
								<Tooltip placement="topLeft" title="Nhắn tin">
									<Badge dot>
										<CommentOutlined style={{ fontSize: 25 }} />
									</Badge>
								</Tooltip>
							}
						/>
						<Menu.Item
							key="2"
							icon={
								<Tooltip placement="topLeft" title="Ví">
									<DollarCircleOutlined style={{ fontSize: 25 }} />
								</Tooltip>
							}
						/>
					</Menu>
				</Sider>
				<Sider
					breakpoint="lg"
					collapsedWidth="0"
					theme="light"
					onBreakpoint={(broken) => {}}
					onCollapse={(collapsed, type) => {}}
					width="350"
					id="sub-side-menu"
				>
					{activeTabKey === '1' ? <ConversationList /> : <UserWallet />}
				</Sider>
				{activeTabKey === '1' ? <MessageList /> : <TransactionHistory />}
			</Layout>
		</div>
	);
}

let mapStateToProps = (state) => {
	return {
		user: state.user
	};
};

export default connect(mapStateToProps, null)(Messenger);
