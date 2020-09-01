import React, { useEffect } from 'react';
import shave from 'shave';
import CustomAvatar from '../CustomAvatar/index';
import { updateCurrSessionId } from '../../redux/fooAction';
import './ConversationListItem.css';
import { useHistory } from 'react-router-dom';
import { connect } from 'react-redux';

function ConversationListItem(props) {
	useEffect(() => {
		shave('.conversation-snippet', 20);
	});

	let history = useHistory();

	let selectedColor = {};

	const { name, text, avatar, id , online} = props.data;

	if (props.currentSessionId === id) {
		selectedColor.backgroundColor = 'rgb(218, 233, 255)';
	  }

	let clickHandle = () => {
		history.push('/t/' + id);
		props.setActiveItem(id);
	};

	return (
		<div className="conversation-list-item" onClick={clickHandle} style={selectedColor}>
			{/* <img className="conversation-photo" src={photo} alt="conversation" /> */}
			<div className="conversation-avatar" style={{ width: 60 }}>
				<CustomAvatar type="user-avatar" avatar={avatar} />
			</div>
			{online ?
                  <div className="status-point online"/>
                  :
                  <div className="status-point offline"/>
                }
			<div className="conversation-info" style={{ paddingLeft: '10px', overflow: 'hidden', paddingTop: 5 }}>
				<div className="user-name">{name}</div>
				<div className="history-message">{text}</div>
			</div>
		</div>
	);
}

function mapStateToProps(state) {
	return {
		currentSessionId: state.currentSessionId
	};
}

function mapDispathToProps(dispatch) {
	return {
		setActiveItem: (id) => dispatch(updateCurrSessionId(id))
	};
}

export default connect(mapStateToProps, mapDispathToProps)(ConversationListItem);
