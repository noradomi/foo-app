import { CommentOutlined, DollarCircleOutlined, NotificationOutlined } from '@ant-design/icons';
import { Layout, Menu, Tooltip } from 'antd';
import React, { useState } from 'react';
import ConversationList from '../ConversationList';
import CustomAvatar from '../CustomAvatar';
import MessageList from '../MessageList';
import './Messenger.css';
import { connect } from 'react-redux';

const { Sider } = Layout;

function Messenger(props) {
	const [ sideBarKey, setSideBarKey ] = useState(1);

	const handleSideBarChange = (e) => {
		setSideBarKey(parseInt(e.key));
	};
	return (
		<div style={{ height: 100 + 'vh' }}>
			<Layout>
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
						theme="light"
						id="side-menu"
						defaultSelectedKeys={[ '1' ]}
						mode="vertical"
						onSelect={handleSideBarChange}
					>
						<Menu.Item
							key="1"
							icon={
								<Tooltip placement="topLeft" title="Chat">
									<CommentOutlined style={{ fontSize: 25 }} />
								</Tooltip>
							}
						/>
						<Menu.Item
							key="2"
							icon={
								<Tooltip placement="topLeft" title="Transactions">
									<DollarCircleOutlined style={{ fontSize: 25 }} />
								</Tooltip>
							}
						/>
						<Menu.Item
							key="3"
							icon={
								<Tooltip placement="topLeft" title="Notifications">
									<NotificationOutlined style={{ fontSize: 25 }} />
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
					{sideBarKey === 1 ? <ConversationList /> : <div>Transactions</div>}
				</Sider>
				{sideBarKey === 1 ? <MessageList style={{ padding: 0 }} /> : <div>Transactions</div>}
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
