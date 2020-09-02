import Sockette from 'sockette';
import { ws_host } from './api';
import { getJwtFromStorage ,getUserIdFromStorage} from '../utils/utils';
import store from '../redux/fooStore';
import { receiveMessage, sendbackMessage} from '../redux/fooAction';

export function initialWebSocket() {
    const jwt = getJwtFromStorage();
    const senderId = getUserIdFromStorage();
	const webSocket = new Sockette(ws_host + '/chat' + '?jwt=' + jwt, {
		timeout: 5e3,
		maxAttempts: 10,
		onopen: (e) => {},
		onmessage: (messageEvent) => {
            
            let jsonMessage = JSON.parse(messageEvent.data);
			if (jsonMessage.type === 'SEND') {
				store.dispatch(receiveMessage(jsonMessage));
			} else if (jsonMessage.type === 'FETCH') {
				if (jsonMessage.receiverId === senderId) {
					return;
				}
				store.dispatch(sendbackMessage(jsonMessage));
			}
		},
		onreconnect: (e) => console.log('Reconnecting...', e),
		onmaximum: (e) => console.log('Stop Attempting!', e),
		onclose: (e) => console.log('Closed!', e),
		onerror: (e) => console.log('Error:', e)
	});
	//ws.close(); // graceful shutdown
	return webSocket;
}
