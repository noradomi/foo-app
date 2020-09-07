import React, { useEffect, useRef } from 'react';
import Compose from '../Compose';
import Toolbar from '../Toolbar';
import ToolbarButton from '../ToolbarButton';
import SignOutButton from '../SignOutButton';
import Message from '../Message';
import moment from 'moment';
import './MessageList.css';
import { connect } from 'react-redux';
import { getUserIdFromStorage } from '../../utils/utils';
import { fetchMessageList } from '../../redux/fooAction';
import { getMessageList } from '../../services/chat-single';
import { hanldeLogout } from '../../services/logout';
import { useHistory } from 'react-router-dom';

function MessageList(props) {
	let senderId = getUserIdFromStorage();

	// Check if userMap is loaded
	if (
		props.userMapHolder.userMap.size === 0 ||
		props.chatMessagesHolder.chatMessages.size === 0 ||
		props.currentSessionId === null
	) {
		return <div />;
	}

	let history = useHistory(); 
	let receiverId = props.currentSessionId;
	let receiver = props.userMapHolder.userMap.get(receiverId);

	let messagesState = props.chatMessagesHolder.chatMessages.get(receiverId);

	let messages = [];

	messages = messagesState.map((item) => {
		return {
			id: item.createTime,
			message: item.message,
			author: item.senderId,
			timestamp: item.createTime	 * 1000
		};
	});

	const renderMessages = () => {
		let i = 0;
		let messageCount = messages.length;
		let tempMessages = [];

		while (i < messageCount) {
			let previous = messages[i - 1];
			let current = messages[i];
			let next = messages[i + 1];
			let isMine = current.author === senderId;
			let currentMoment = moment(current.timestamp);
			let prevBySameAuthor = false;
			let nextBySameAuthor = false;
			let startsSequence = true;
			let endsSequence = true;
			let showTimestamp = true;

			if (previous) {
				let previousMoment = moment(previous.timestamp);
				let previousDuration = moment.duration(currentMoment.diff(previousMoment));
				prevBySameAuthor = previous.author === current.author;

				if (prevBySameAuthor && previousDuration.as('hours') < 1) {
					startsSequence = false;
				}

				if (previousDuration.as('hours') < 1) {
					showTimestamp = false;
				}
			}

			if (next) {
				let nextMoment = moment(next.timestamp);
				let nextDuration = moment.duration(nextMoment.diff(currentMoment));
				nextBySameAuthor = next.author === current.author;

				if (nextBySameAuthor && nextDuration.as('hours') < 1) {
					endsSequence = false;
				}
			}

			tempMessages.push(
				<Message
					key={i}
					isMine={isMine}
					startsSequence={startsSequence}
					endsSequence={endsSequence}
					showTimestamp={showTimestamp}
					data={current}
				/>
			);

			// Proceed to the next message.
			i += 1;
		}

		return tempMessages;
	};

	// >>>
	let endOfMsgList = useRef(null);
	let msgList = useRef(null);

	// Init current message fetch from api.
	useEffect(
		() => {
			if (messagesState.length === 0) {
				getMessageList(receiverId, 0).then((data) => {
					props.updateMsgListOnStore(data, receiverId);
					endOfMsgList.current.scrollIntoView({ behavior: 'smooth' });
				});
			}
		},
		[ receiverId ]
	);

	useEffect(
		() => {
			endOfMsgList.current.scrollIntoView({ behavior: 'smooth' });
		},
		[ props.scrollFlag, props.currentSessionId ]
	);

	// Load more message
	let msgScrollHandle = (event) => {
		let msgList = event.target;
		let offset = msgList.scrollTop;
		if (offset === 0) {
			getMessageList(receiverId, messagesState.length).then((data) => {
				let oldHeight = msgList.scrollHeight;
				props.updateMsgListOnStore(data, receiverId);
				let newHeight = msgList.scrollHeight;
				msgList.scrollTo(0, newHeight - oldHeight);
			});
		}
	};

	let onChangeText = (e) => {
		if (e.keyCode === 13 && e.target.value !== '') {
			// Click ENTER
			let msg = e.target.value;

			props.webSocket.send(receiverId, msg);

			e.target.value = '';
		}
	};

	return (
		<div className="message-list">
			<Toolbar
				title={receiver.name}
				rightItems={[
					<SignOutButton
						key="add"
						icon="ion-ios-log-out"
						onClick={() => {
							hanldeLogout().then(() => history.push('/login'));
						}}
					/>,
					<ToolbarButton key="video" icon="ion-ios-videocam" />,
					<ToolbarButton key="phone" icon="ion-ios-call" />
				]}
			/>

			<div className="message-list-container" ref={msgList} onScroll={msgScrollHandle}>
				{renderMessages()}
				<div ref={endOfMsgList} style={{ height: '0px' }} />
			</div>

			<Compose
				rightItems={[
					<ToolbarButton key="photo" icon="ion-ios-happy" />,
					<ToolbarButton key="image" icon="ion-ios-image" />,
					<ToolbarButton key="audio" icon="ion-ios-paper-plane" />
				]}
				onKeyUp={onChangeText}
			/>
		</div>
	);
}

let mapStateToProps = (state) => {
	return {
		userMapHolder: state.userMapHolder,
		chatMessagesHolder: state.chatMessagesHolder,
		webSocket: state.webSocket,
		scrollFlag: state.scrollFlag,
		currentSessionId: state.currentSessionId
	};
};

let mapDispatchToProps = (dispatch) => {
	return {
		updateMsgListOnStore: (data, friendId) => {
			dispatch(fetchMessageList(data, friendId));
		}
	};
};

export default connect(mapStateToProps, mapDispatchToProps)(MessageList);
