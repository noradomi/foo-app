import { Tooltip, Badge } from 'antd';
import React from 'react';
import { useSelector } from 'react-redux';
import { MessageFilled, CommentOutlined } from '@ant-design/icons';

ChatTab.propTypes = {};

function ChatTab(props) {
	const unseenChat = useSelector((state) => state.unseenChat);
	return (
		<Tooltip placement="topLeft" title="Nhắn tin">
			{unseenChat.size > 0 ? (
				<Badge count={unseenChat.size}>
					<MessageFilled style={{ fontSize: 25 }} />
				</Badge>
			) : (
				<CommentOutlined style={{ fontSize: 25 }} />
			)}
		</Tooltip>
	);
}

export default ChatTab;
