import React from 'react';
import Messenger from '../Messenger';
import { isAuthenticated } from '../../utils/utils';
import store from '../../store/fooStore';
import Portal from '../Portal/index';
import { Provider } from 'react-redux';
import 'antd/dist/antd.css';
import { BrowserRouter as Router, Switch, Route, Redirect } from 'react-router-dom';

let AuthFragment = (props) => {
	if (!isAuthenticated()) {
		return <Messenger />;
	}
	return <Redirect to={'/login'} />;
};

export default function App() {
	return (
		<Provider store={store}>
			<div className="App">
				<Router>
					<Switch>
						{/* <Route path="/login" component={LoginPage}/>
            <Route path="/signup" component={RegisterPage}/> */}
						<Route path="/login" component={Portal} />
						<Route path="/" component={AuthFragment} />
					</Switch>
				</Router>
			</div>
		</Provider>
	);
}
