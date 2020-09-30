import { FintechServiceClient } from '../grpc/fintech_grpc_web_pb.js';
import { GetBalanceRequest, TransferMoneyRequest } from '../grpc/fintech_pb.js';
import { getJwtFromStorage } from '../utils/utils.js';

const URL = 'http://' + window.location.hostname + ':8080';

const client = new FintechServiceClient(URL, null, null);

export default {
	getBalance: (name, callback) => {
		const jwtToken = getJwtFromStorage();
		const metadata = { jwt: jwtToken };
		const getBalanceRequest = new GetBalanceRequest();
		client.getBalance(getBalanceRequest, metadata, callback);
	},

	transferMoney: (request, callback) => {
		const jwtToken = getJwtFromStorage();
		const metadata = { jwt: jwtToken };
		const transferMoneyRequest = new TransferMoneyRequest();
		transferMoneyRequest.setReceiver(request.receiver);
		transferMoneyRequest.setAmount(request.amount);
		transferMoneyRequest.setDescription(request.description);
		transferMoneyRequest.setConfirmPassword(request.confirmPassword);
		client.transferMoney(transferMoneyRequest, metadata, callback);
	}
};
