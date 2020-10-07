import Sockette from 'sockette';
import { wsHost } from './api';
import { getJwtFromStorage, getUserIdFromStorage, processUsernameForAvatar } from '../utils/utils';
import store from '../store/fooStore';
import {
	receiveMessageAction,
	sendMessageAction,
	setUserStatusAction,
	updateLastMessage,
	setUnseenMessages,
	addNewFriendAction,
	setWalletAction,
	appendTranctionHistoryAction
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

				case 'ADD_FRIEND': {
					const newFriend = jsonMessage.userInfo;
					console.log(newFriend);
					let userItem = {
						userId: newFriend.userId_,
						name: newFriend.name_,
						avatar: processUsernameForAvatar(newFriend.name_),
						unreadMessages: newFriend.unreadMessages_,
						lastMessage: newFriend.lastMessage_,
						online: newFriend.isOnline_
					};
					store.dispatch(addNewFriendAction(userItem));
					break;
				}

				case 'TRANSFER_MONEY': {
					console.log('Receive 1 transfer money');

					const data = jsonMessage.transferMoneyData;
					console.log(data);
					const transacion = data.transaction_;
					const newBalance = data.balance_;
					const lastUpdated = data.lastUpdated_;
					const item = {
						userId: transacion.userId_,
						description: transacion.description_,
						amount: transacion.amount_,
						recordedTime: lastUpdated,
						transferType: transacion.transferType_
					};

					store.dispatch(setWalletAction(newBalance, lastUpdated));
					store.dispatch(appendTranctionHistoryAction(item));
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
