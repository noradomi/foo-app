import { ws_host } from './api';
import store from '../redux/fooStore';
import { webSocketConnected, login} from '../redux/fooAction';
import { getUserIdFromStorage, getJwtFromStorage } from '../utils/utils';

let ws = null;

export function wsConnect(msg) {
	// Current user id
	let senderId = Number(getUserIdFromStorage());

	// Return websocket promise
	if (ws === null) {
		let token = getJwtFromStorage();
		let webSocket = new WebSocket(ws_host +"chat"+`?token=${token}`);

		// When connect successful
		webSocket.onopen = (event) => {
            console.log("Created new web socket connection");

			ws = webSocket;

			// Function to send chat message
			let send = function(receiverId, message) {
				let sentMessage = {
					receiver_id: receiverId,
					msg: message,
					type: 'SEND'
				};
				webSocket.send(JSON.stringify(sentMessage));
			};

			// Test message
			send(senderId, msg);

			// Notify to redux
			store.dispatch(webSocketConnected(webSocket, send));

			// // Handle chat message
			// webSocket.onmessage = (messageEvent) => {
			// 	let jsonMessage = JSON.parse(messageEvent.data);
			// 	if (jsonMessage.type === 'CHAT') {
			// 		jsonMessage.isMine = jsonMessage.senderId === senderId;
			// 		store.dispatch(receiveMessage(jsonMessage));
			// 	} else if (jsonMessage.type === 'SEND_BACK') {
			// 		if (jsonMessage.receiverId === senderId) {
			// 			return;
			// 		}
			// 		jsonMessage.isMine = true;
			// 		store.dispatch(sendbackMessage(jsonMessage));
			// 	}
			// };
		};
	}
}
