import { createStore } from 'redux';
import { composeWithDevTools } from 'redux-devtools-extension';
import appReducer from '../reducers/fooReducer';

const rootReducer = (state, action) => {
	if (action.type === 'USER_LOGOUT') {
		state = undefined;
	}
	return appReducer(state, action);
};

export default createStore(rootReducer, composeWithDevTools());
