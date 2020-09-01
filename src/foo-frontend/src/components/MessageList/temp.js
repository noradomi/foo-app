import React, {useEffect, useState} from 'react';
import Compose from '../Compose';
import Toolbar from '../Toolbar';
import ToolbarButton from '../ToolbarButton';
import Message from '../Message';
import {Switch, Route} from 'react-router-dom';
import moment from 'moment';
import logout from "../../service/logout";
import {connect} from 'react-redux';

import './MessageList.css';

let MessageList = (props) => {
  // MyId
  let senderId = Number(localStorage.getItem("userId"));

  // Check if userMap is loaded
  if (props.userMapHolder.userMap.size === 0 || props.chatMessagesHolder.chatMessages.size === 0) {
    return <div></div>;
  }

  let receiverId = Number(props.match.params.receiverId);
  let receiver = props.userMapHolder.userMap.get(receiverId);
  let messageList = props.chatMessagesHolder.chatMessages.get(receiverId);
  let webSocket = props.webSocket;

  // Messages list
  let messages = messageList.map(x => {
    return {
      id: x.timestamp,
      message: x.message,
      author: x.senderId,
      timestamp: x.timestamp * 1000,
      isMine: x.isMine
    };
  });

  // Display message
  const renderMessages = () => {
    let i = 0;
    let messageCount = messages.length;
    let tempMessages = [];

    while (i < messageCount) {
      let previous = messages[i - 1];
      let current = messages[i];
      let next = messages[i + 1];
      console.log(current.author);
      let isMine = current.isMine
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
  }

  let onChangeText = (event) => {
    if (event.keyCode === 13) {
      console.log(event.target.value);
      webSocket.send(receiverId, event.target.value);
      event.target.value = '';
    }
  }

  return (
    <div className="message-list">
      <Toolbar
        title={receiver.name}
        rightItems={[
          <ToolbarButton key="signout" icon="ion-ios-log-out" onClick={() => logout()}/>,
          <ToolbarButton key="info" icon="ion-ios-information-circle-outline"/>,
          <ToolbarButton key="video" icon="ion-ios-videocam"/>,
          <ToolbarButton key="phone" icon="ion-ios-call"/>,
        ]}
      />

      <div className="message-list-container">{renderMessages()}</div>

      <Comopose rightItems={[
        <ToolbarButton key="photo" icon="ion-ios-camera"/>,
        <ToolbarButton key="image" icon="ion-ios-image"/>,
        <ToolbarButton key="audio" icon="ion-ios-mic"/>,
        <ToolbarButton key="money" icon="ion-ios-card"/>,
        <ToolbarButton key="games" icon="ion-logo-game-controller-b"/>,
        <ToolbarButton key="emoji" icon="ion-ios-happy"/>
      ]}
               onKeyUp={onChangeText}/>
    </div>
  );
}

let mapStateToPropsMessageList = (state) => {
  return {
    userMapHolder: state.userMapHolder,
    chatMessagesHolder: state.chatMessagesHolder,
    webSocket: state.webSocket
  }
}

MessageList = connect(mapStateToPropsMessageList, null)(MessageList);

export default function MessageListWrapper() {
  return (
    <Switch>
      <Route path="/t/:receiverId" component={MessageList}/>
      <Route exact path="/">
        <div></div>
      </Route>
    </Switch>
  );
}