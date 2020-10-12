import { CommentOutlined, FireFilled, MessageFilled, WalletFilled } from '@ant-design/icons';
import { Badge, Layout, Menu, Tooltip } from 'antd';
import React from 'react';
import { connect, useDispatch, useSelector } from 'react-redux';
import { setActiveTabKeyAction, setHavingUnseenTransferAction } from '../../actions/fooAction';
import ConversationList from '../ConversationList';
import CustomAvatar from '../CustomAvatar';
import MessageList from '../MessageList';
import TransactionHistory from '../TransactionHistory';
import UserWallet from '../UserWallet';
import './Messenger.css';

const { Sider } = Layout;

function Messenger(props) {
	const activeTabKey = useSelector((state) => state.activeTabKey);
	const unseenChat = useSelector((state) => state.unseenChat);
	const unseenTransfer = useSelector((state) => state.unseenTransfer);
	console.log('Transfer status: ', unseenTransfer);
	const dispatch = useDispatch();

	const handleSideBarChange = (e) => {
		dispatch(setActiveTabKeyAction(e.key));
		if (e.key === '2') dispatch(setHavingUnseenTransferAction(false));
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
									{unseenChat ? (
										<Badge count={<FireFilled style={{ color: '#f5222d', fontSize: '15px' }} />}>
											<MessageFilled style={{ fontSize: 25 }} />
										</Badge>
									) : (
										<CommentOutlined style={{ fontSize: 25 }} />
									)}
								</Tooltip>
							}
						/>
						<Menu.Item
							key="2"
							icon={
								<Tooltip placement="topLeft" title="Ví">
									{unseenTransfer === 'true' ? (
										<Badge count={<FireFilled style={{ color: '#f5222d', fontSize: '15px' }} />}>
											<WalletFilled style={{ fontSize: 25 }} />
										</Badge>
									) : (
										<WalletFilled style={{ fontSize: 25 }} />
									)}
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
