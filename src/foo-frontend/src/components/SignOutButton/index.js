import React from 'react';
// import Button from 'muicss/lib/react/button';
import './ToolbarButton.css';
// import { hanldeLogout } from '../../services/logout';
// import {useHistory} from 'react-router-dom';

export default function SignOutButton(props) {
    // let history = useHistory();
	const { icon } = props;
	// function handleClick(e) {
	// 	hanldeLogout().then(() => history.push('/login'));
	// }
	return (
		// <Button variant="fab" color="primary" onClick={handleClick}>
			
		// 	Log out
		// </Button>
		<i className={`toolbar-button ${icon}`} onClick={props.onClick}/>
	);
}
