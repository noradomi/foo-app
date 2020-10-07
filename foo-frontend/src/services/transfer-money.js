import {
	addNewFriendAction,
	setUserStatusAction,
	setWalletAction,
	appendTranctionHistoryAction
} from '../actions/fooAction';
import store from '../store/fooStore';
import grpcApi from './grpcApi';

export function transferMoney(request) {
	return new Promise((resolve, reject) => {
		grpcApi.transferMoney(request, (err, response) => {
			const code = response.getStatus().getCode();
			if (code === 0) {
				const data = response.getData();
				const transacion = data.getTransaction();
				const item = {
					userId: transacion.getUserId(),
					description: transacion.getDescription(),
					amount: transacion.getAmount(),
					recordedTime: transacion.getRecordedTime(),
					transferType: transacion.getTransferType()
				};
				const newBalance = data.getBalance();
				const lastUpdated = data.getLastUpdated();
				store.dispatch(setWalletAction(newBalance, lastUpdated));
				store.dispatch(appendTranctionHistoryAction(item));
			}
			resolve(response);
		});
	});
}
