import Sockette from 'sockette';
import { wsHost } from './api';
import { getJwtFromStorage, getUserIdFromStorage } from '../utils/utils';
import store from '../store/fooStore';
import {
	receiveMessageAction,
	sendMessageAction,
	setUserStatusAction,
	updateLastMessage,
	setUnseenMessages
} from '../actions/fooAction';
import { resetUnseen } from './reset-unseen';

export function initialWebSocket() {
	const jwt = getJwtFromStorage();
	const senderId = getUserIdFromStorage();
	const webSocket = new Sockette(`${wsHost}/chat?jwt=${jwt}`, {
		timeout: 5e3,
		maxAttempts: 10,
		onopen: (e) => {},
		onmessage: (messageEvent) => {
			let jsonMessage = JSON.parse(messageEvent.data);
			switch (jsonMessage.type) {
				case 'ONLINE': {
					if (senderId !== jsonMessage.senderid) {
						store.dispatch(setUserStatusAction(jsonMessage.senderId, true));
					}
					break;
				}
				case 'OFFLINE': {
					if (senderId !== jsonMessage.senderId) {
						store.dispatch(setUserStatusAction(jsonMessage.senderId, false));
					}
					break;
				}

				case 'SEND': {
					const selectedId = store.getState().selectedUser.id;
					console.log('SEND: ' + jsonMessage.senderId + '===' + selectedId);

					if (selectedId === jsonMessage.senderId) {
						resetUnseen(jsonMessage.senderId);
					} else {
						console.log('Inscrease unseen messages');
						store.dispatch(setUnseenMessages(jsonMessage.senderId, 1));
					}
					store.dispatch(receiveMessageAction(jsonMessage));
					store.dispatch(updateLastMessage(jsonMessage.senderId, jsonMessage.message));
					break;
				}
				case 'FETCH': {
					if (jsonMessage.receiverId !== senderId) {
						console.log('FETCH: ' + jsonMessage.receiverId);
						store.dispatch(sendMessageAction(jsonMessage));
						store.dispatch(updateLastMessage(jsonMessage.receiverId, jsonMessage.message));
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
