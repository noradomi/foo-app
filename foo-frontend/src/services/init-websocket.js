import Sockette from 'sockette';
import {
	addNewFriendAction,
	appendTranctionHistoryAction,
	receiveMessageAction,
	sendMessageAction,
	setHavingUnseenTransferAction,
	setNewUnseenChatUserAction,
	setUnseenMessages,
	setUserStatusAction,
	setWalletAction,
	updateLastMessage
} from '../actions/fooAction';
import store from '../store/fooStore';
import { getJwtFromStorage, getUserIdFromStorage, processUsernameForAvatar } from '../utils/utils';
import { wsHost } from './api';
import { resetUnseen } from './reset-unseen';
/*  */
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

					if (selectedId === jsonMessage.senderId) {
						resetUnseen(jsonMessage.senderId);
					} else {
						store.dispatch(setUnseenMessages(jsonMessage.senderId, 1));
						store.dispatch(setNewUnseenChatUserAction(jsonMessage.senderId));
					}

					store.dispatch(receiveMessageAction(jsonMessage));
					store.dispatch(updateLastMessage(jsonMessage.senderId, jsonMessage.message));
					break;
				}
				case 'FETCH': {
					if (jsonMessage.receiverId !== senderId) {
						store.dispatch(sendMessageAction(jsonMessage));
						store.dispatch(updateLastMessage(jsonMessage.receiverId, 'Bạn: ' + jsonMessage.message));
					}
					break;
				}

				case 'ADD_FRIEND': {
					const newFriend = jsonMessage.userInfo;
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
					const data = jsonMessage.transferMoneyData;
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

					const transferMessage = {
						message: item.amount,
						receiverId: getUserIdFromStorage(),
						senderId: transacion.userId_,
						createTime: item.recordedTime,
						messageType: 1
					};

					const selectedId = store.getState().selectedUser.id;

					if (selectedId === transacion.userId_) {
						resetUnseen(transacion.userId_);
					} else {
						store.dispatch(setUnseenMessages(transacion.userId_, 1));
						store.dispatch(setNewUnseenChatUserAction(transacion.userId_));
					}

					store.dispatch(receiveMessageAction(transferMessage));
					store.dispatch(setWalletAction(newBalance, lastUpdated));
					store.dispatch(appendTranctionHistoryAction(item));
					store.dispatch(
						updateLastMessage(transacion.userId_, '[Nhận tiền] Nhận ' + transacion.amount_ + ' VND')
					);
					const activeTabKey = store.getState().activeTabKey;
					if (activeTabKey !== '2') store.dispatch(setHavingUnseenTransferAction(true));
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
