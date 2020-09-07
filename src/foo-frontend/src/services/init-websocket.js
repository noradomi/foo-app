import Sockette from 'sockette';
import { ws_host } from './api';
import { getJwtFromStorage, getUserIdFromStorage } from '../utils/utils';
import store from '../redux/fooStore';
import { receiveMessage, sendbackMessage, changeUserStatus } from '../redux/fooAction';

export function initialWebSocket() {
	const jwt = getJwtFromStorage();
	const senderId = getUserIdFromStorage();
	const webSocket = new Sockette(ws_host + '/chat' + '?jwt=' + jwt, {
		timeout: 5e3,
		maxAttempts: 10,
		onopen: (e) => {},
		onmessage: (messageEvent) => {
			let jsonMessage = JSON.parse(messageEvent.data);
			switch (jsonMessage.type) {
				case 'ONLINE': {
					if (senderId !== jsonMessage.senderid) {
						store.dispatch(changeUserStatus(jsonMessage.senderId, true));
					}
					break;
				}
				case 'OFFLINE': {
					if (senderId !== jsonMessage.senderId) {
						store.dispatch(changeUserStatus(jsonMessage.senderId, false));
					}
					break;
				}

				case 'SEND':
					store.dispatch(receiveMessage(jsonMessage));
					break;
				case 'FETCH': {
					if (jsonMessage.receiverId !== senderId) {
						store.dispatch(sendbackMessage(jsonMessage));
					}
					break;
				}
				default:
					break;
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
