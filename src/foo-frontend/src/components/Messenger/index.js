import React from 'react';
import ConversationList from '../ConversationList';
import MessageList from '../MessageList';
// import Toolbar from '../Toolbar';
// import ToolbarButton from '../ToolbarButton';
import './Messenger.css';

export default function Messenger(props) {
    return (
      <div className="messenger">
        <div className="sidebar">
          <ConversationList />
        </div>

        <div className="content">
          <MessageList />
        </div>
      </div>
    );
}