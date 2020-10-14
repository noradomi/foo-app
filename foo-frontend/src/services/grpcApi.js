import { FintechServiceClient } from '../grpc/fintech_grpc_web_pb.js';
import { ChatServiceClient } from '../grpc/chat_grpc_web_pb.js';
import { GetBalanceRequest, TransferMoneyRequest, GetHistoryRequest } from '../grpc/fintech_pb.js';
import { GetFriendListRequest, ResetUnseenRequest, AddFriendRequest } from '../grpc/chat_pb.js';
import { getJwtFromStorage } from '../utils/utils.js';

const URL = 'http://' + window.location.hostname + ':8080';

const fintechClient = new FintechServiceClient(URL, null, null);
const chatClient = new ChatServiceClient(URL, null, null);

export default {
	getBalance: (callback) => {
		const getBalanceRequest = new GetBalanceRequest();
		fintechClient.getBalance(getBalanceRequest, prepareMetadata(), callback);
	},

	transferMoney: (request, callback) => {
		const transferMoneyRequest = new TransferMoneyRequest();
		transferMoneyRequest.setReceiver(request.receiver);
		transferMoneyRequest.setAmount(request.amount);
		transferMoneyRequest.setDescription(request.description);
		transferMoneyRequest.setConfirmPassword(request.confirmPassword);
		fintechClient.transferMoney(transferMoneyRequest, prepareMetadata(), callback);
	},

	getHistory: (pageSize, pageToken, callback) => {
		const getHistoryRequest = new GetHistoryRequest();
		getHistoryRequest.setPageSize(pageSize);
		getHistoryRequest.setPageToken(pageToken);
		fintechClient.getHistory(getHistoryRequest, prepareMetadata(), callback);
	},

	getFriendList: (callback) => {
		const getFriendListRequest = new GetFriendListRequest();
		chatClient.getFriendList(getFriendListRequest, prepareMetadata(), callback);
	},

	resetUnseen: (userId, callback) => {
		const resetUnseenRequest = new ResetUnseenRequest();
		resetUnseenRequest.setUserId(userId);
		chatClient.resetUnseen(resetUnseenRequest, prepareMetadata(), callback);
	},
	addFriend: (userId, callback) => {
		const addFriendRequest = new AddFriendRequest();
		addFriendRequest.setUserId(userId);
		chatClient.addFriend(addFriendRequest, prepareMetadata(), callback);
	}
};

function prepareMetadata() {
	const jwtToken = getJwtFromStorage();
	const metadata = { jwt: jwtToken };
	return metadata;
}
