import React from 'react';
import './ConversationSearch.css';
import { Input } from 'antd';
import { SearchOutlined } from '@ant-design/icons';

export default function ConversationSearch() {
	return (
		<div className="conversation-search">
			<Input
				size="large"
				className="conversation-search-input"
				placeholder="Tìm kiếm theo tên"
				prefix={<SearchOutlined />}
				// style={{
				// 	border: 'none',
				// 	backgroundColor: 'rgb(245, 245, 245)',
				// 	borderRadius: 6,
				// 	color: 'rgb(123, 128, 155)'
				// }}
			/>
		</div>
	);
}
