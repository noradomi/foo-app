import {
	appendTranctionHistoryAction,
	sendMessageAction,
	setWalletAction,
	updateLastMessage
} from '../actions/fooAction';
import store from '../store/fooStore';
import { getUserIdFromStorage } from '../utils/utils';
import grpcApi from './grpcApi';

export function transferMoney(request) {
	return new Promise((resolve, reject) => {
		grpcApi.transferMoney(request, (err, response) => {
			const code = response.getStatus().getCode();
			if (code === 0) {
				const data = response.getData();
				const transacion = data.getTransaction();
				const newBalance = data.getBalance();
				const lastUpdated = data.getLastUpdated();
				const friendList = store.getState().friendList;
				const index = friendList.findIndex((friend) => friend.userId === transacion.getUserId());
				const userName = friendList[index].name;
				const item = {
					userName: userName,
					description: transacion.getDescription(),
					amount: transacion.getAmount(),
					recordedTime: lastUpdated,
					transferType: transacion.getTransferType()
				};

				store.dispatch(setWalletAction(newBalance, lastUpdated));
				store.dispatch(appendTranctionHistoryAction(item));
				const transferMessage = {
					message: item.amount,
					senderId: getUserIdFromStorage(),
					receiverId: transacion.getUserId(),
					createTime: item.recordedTime,
					messageType: 1
				};
				store.dispatch(sendMessageAction(transferMessage));
				store.dispatch(
					updateLastMessage(
						transacion.getUserId(),
						'Bạn: [Chuyển tiền] Chuyển ' + transacion.getAmount() + ' VND'
					)
				);
			}
			resolve(response);
		});
	});
}
