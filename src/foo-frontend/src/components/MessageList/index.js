import React, { useEffect, useState } from 'react';
import Compose from '../Compose';
import Toolbar from '../Toolbar';
import ToolbarButton from '../ToolbarButton';
import Message from '../Message';
import moment from 'moment';
import './MessageList.css';
import {wsConnect} from '../../services/chat-single';
import { connect } from 'react-redux';
import {getUserIdFromStorage} from '../../utils/utils'

const MY_USER_ID = 'apple';

wsConnect(); // <<<
function MessageList(props) {

  useEffect(() => {

  },[]);

  let senderId = getUserIdFromStorage();
  let receiverId = Number(props.match.params.receiverId); // >>>
  let receiver = props.userMapHolder.userMap.get(receiverdId);

  console.log(">>> Chatting with ",receiver.name);

  let messagesState = props.chatMessagesHolder.chatMessages.get(receiverId); //>>>

  let ws = props.webSocket;

  let messages = messagesState.map(msg => {
    return {
      id: 
    }
  })
  
	const renderMessages = () => {
		let i = 0;
		let messageCount = messages.length;
		let tempMessages = [];

		while (i < messageCount) {
			let previous = messages[i - 1];
			let current = messages[i];
			let next = messages[i + 1];
			let isMine = current.author === MY_USER_ID;
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
	let onChangeText = (e) => {
    if (e.keyCode == 13) // Enter
    {
      let msg = e.target.value;
      
      props.webSocket.send(123,msg);

      console.log("Sent to server : "+msg);
			e.target.value = '';
		}
	};

	return (
		<div className="message-list">
			<Toolbar
				
				title="Conversation Title"
				rightItems={[
					<ToolbarButton key="info" icon="ion-ios-information-circle-outline" />,
					<ToolbarButton key="video" icon="ion-ios-videocam" />,
					<ToolbarButton key="phone" icon="ion-ios-call" />
				]}
			/>

			<div className="message-list-container">{renderMessages()}</div>

			<Compose
				rightItems={[
					// <ToolbarButton key="photo" icon="ion-ios-camera" />,
					<ToolbarButton key="image" icon="ion-ios-image" />,
					// <ToolbarButton key="audio" icon="ion-ios-mic" />,
					// <ToolbarButton key="money" icon="ion-ios-card" />,

					<ToolbarButton key="emoji" icon="ion-ios-happy" />,
					<ToolbarButton key="games" icon="ion-ios-send" />
				]}
				onKeyUp={onChangeText}
			/>
		</div>
	);
}

let mapStateToProps = (state) => {
  return {
    webSocket: state.webSocket
  }
}

export default connect(mapStateToProps,null)(MessageList);