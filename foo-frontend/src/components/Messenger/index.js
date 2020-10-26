import { LogoutOutlined } from '@ant-design/icons';
import { GridContent } from '@ant-design/pro-layout';
import { Col, Layout, Menu, Row, Tooltip } from 'antd';
import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';
import { logOutAction, setActiveTabKeyAction, setHavingUnseenTransferAction } from '../../actions/fooAction';
import { wsConnect } from '../../services/chat-single';
import { closeWebSocket, hanldeLogout } from '../../services/logout';
import { clearStorage, processUsernameForAvatar } from '../../utils/utils';
import ChatTab from '../ChatTab';
import ConversationList from '../ConversationList';
import MessageList from '../MessageList';
import TransactionHistory from '../TransactionHistory';
import UserWallet from '../UserWallet';
import WalletTab from '../WalletTab';
import WallTransferList from '../WallTransferList';
import './Messenger.css';
import CustomAvatar from '../CustomAvatar';

const { Sider, Content } = Layout;

function Messenger(props) {
  const activeTabKey = useSelector((state) => state.activeTabKey);
  const user = useSelector(state => state.user)
  const dispatch = useDispatch();
  const history = useHistory();
  let avatar = processUsernameForAvatar(user.name);

  useEffect(() => {
    wsConnect();
  }, [])

	const handleSideBarChange = (e) => {
    if(e.key === '3') {
      hanldeLogout().then(() => {
        clearStorage();
        closeWebSocket();
        dispatch(logOutAction());
        history.push('/login')});
    } else{
      dispatch(setActiveTabKeyAction(e.key));
      if (e.key === '2') dispatch(setHavingUnseenTransferAction(false));
    }
  };

	return (
		<div style={{ height: 100 + 'vh' }}>
			<Layout style={{ height: 100 + 'vh' }}>
				<Sider
					breakpoint="md"
					collapsedWidth="0"
					onBreakpoint={(broken) => {}}
					onCollapse={(collapsed, type) => {}}
					width="75"
					id="main-side-menu"
				>
          <div className="tab-avatar">
           <CustomAvatar type="panel-avatar" avatar={ avatar || ''} />
          </div>
					
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
								<ChatTab/>
							}
						/>
						<Menu.Item
							key="2"
							icon={
								<WalletTab/>
							}
						/>
            <Menu.Item
							key="3"
							icon={
								<Tooltip placement="topLeft" title="Đăng xuất">
									<LogoutOutlined style={{fontSize: "25px"}}/>
								</Tooltip>
							}
						/>
					</Menu>
				</Sider>

				{activeTabKey !== '1' ? (
					<Layout>
						<Content>
							{' '}
							<GridContent style={{ width: '100%' }}>
								<Row gutter={24}>
									<Col xl={7} lg={7} md={24} style={{ width: '100%', paddingRight: 0 }}>
										<UserWallet />
										<WallTransferList />
									</Col>
									<Col xl={17} lg={17} md={24} style={{ width: '100%', paddingLeft: 0 }}>
										<TransactionHistory />
									</Col>
								</Row>
							</GridContent>
						</Content>
					</Layout>
				) : (
          <>
					<Sider
						breakpoint="md"
						collapsedWidth="0"
						theme="light"
						onBreakpoint={(broken) => {}}
						onCollapse={(collapsed, type) => {}}
						width="350"
						id="sub-side-menu"
					>
						<ConversationList />
					</Sider>
          <MessageList/>
          </>
				)}
			</Layout>
		</div>
	);
}

export default Messenger;
