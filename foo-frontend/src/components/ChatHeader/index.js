import React from 'react';
import PropTypes from 'prop-types';
import CustomAvatar from '../CustomAvatar';
import './ChatHeader.css';

ChatHeader.propTypes = {
	user: PropTypes.object
};

ChatHeader.defaultProps = {
	user: null
};

function ChatHeader(props) {
	const { user } = props;

	return (
		<div className="chat-header">
			<div style={{ width: 40 }}>
				<CustomAvatar type="panel-avatar" avatar={user.avatar} />
			</div>
			<div style={{ overflow: 'hidden', paddingTop: 5 }}>
				<div className="panel-message">{user.name}</div>
			</div>
		</div>
	);
}

export default ChatHeader;
