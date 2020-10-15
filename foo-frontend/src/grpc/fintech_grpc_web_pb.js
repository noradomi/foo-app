/**
 * @fileoverview gRPC-Web generated client stub for fintech
 * @enhanceable
 * @public
 */

// GENERATED CODE -- DO NOT EDIT!

/* eslint-disable */
// @ts-nocheck

const grpc = {};
grpc.web = require('grpc-web');

var fintech_status_pb = require('./status_pb.js');
const proto = {};
proto.fintech = require('./fintech_pb.js');

/**
 * @param {string} hostname
 * @param {?Object} credentials
 * @param {?Object} options
 * @constructor
 * @struct
 * @final
 */
proto.fintech.FintechServiceClient = function(hostname, credentials, options) {
	if (!options) options = {};
	options['format'] = 'text';

	/**
   * @private @const {!grpc.web.GrpcWebClientBase} The client
   */
	this.client_ = new grpc.web.GrpcWebClientBase(options);

	/**
   * @private @const {string} The hostname
   */
	this.hostname_ = hostname;
};

/**
 * @param {string} hostname
 * @param {?Object} credentials
 * @param {?Object} options
 * @constructor
 * @struct
 * @final
 */
proto.fintech.FintechServicePromiseClient = function(hostname, credentials, options) {
	if (!options) options = {};
	options['format'] = 'text';

	/**
   * @private @const {!grpc.web.GrpcWebClientBase} The client
   */
	this.client_ = new grpc.web.GrpcWebClientBase(options);

	/**
   * @private @const {string} The hostname
   */
	this.hostname_ = hostname;
};

/**
 * @const
 * @type {!grpc.web.MethodDescriptor<
 *   !proto.fintech.GetBalanceRequest,
 *   !proto.fintech.GetBalanceResponse>}
 */
const methodDescriptor_FintechService_GetBalance = new grpc.web.MethodDescriptor(
	'/fintech.FintechService/GetBalance',
	grpc.web.MethodType.UNARY,
	proto.fintech.GetBalanceRequest,
	proto.fintech.GetBalanceResponse,
	/**
   * @param {!proto.fintech.GetBalanceRequest} request
   * @return {!Uint8Array}
   */
	function(request) {
		return request.serializeBinary();
	},
	proto.fintech.GetBalanceResponse.deserializeBinary
);

/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.fintech.GetBalanceRequest,
 *   !proto.fintech.GetBalanceResponse>}
 */
const methodInfo_FintechService_GetBalance = new grpc.web.AbstractClientBase.MethodInfo(
	proto.fintech.GetBalanceResponse,
	/**
   * @param {!proto.fintech.GetBalanceRequest} request
   * @return {!Uint8Array}
   */
	function(request) {
		return request.serializeBinary();
	},
	proto.fintech.GetBalanceResponse.deserializeBinary
);

/**
 * @param {!proto.fintech.GetBalanceRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @param {function(?grpc.web.Error, ?proto.fintech.GetBalanceResponse)}
 *     callback The callback function(error, response)
 * @return {!grpc.web.ClientReadableStream<!proto.fintech.GetBalanceResponse>|undefined}
 *     The XHR Node Readable Stream
 */
proto.fintech.FintechServiceClient.prototype.getBalance = function(request, metadata, callback) {
	return this.client_.rpcCall(
		this.hostname_ + '/fintech.FintechService/GetBalance',
		request,
		metadata || {},
		methodDescriptor_FintechService_GetBalance,
		callback
	);
};

/**
 * @param {!proto.fintech.GetBalanceRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.fintech.GetBalanceResponse>}
 *     Promise that resolves to the response
 */
proto.fintech.FintechServicePromiseClient.prototype.getBalance = function(request, metadata) {
	return this.client_.unaryCall(
		this.hostname_ + '/fintech.FintechService/GetBalance',
		request,
		metadata || {},
		methodDescriptor_FintechService_GetBalance
	);
};

/**
 * @const
 * @type {!grpc.web.MethodDescriptor<
 *   !proto.fintech.TransferMoneyRequest,
 *   !proto.fintech.TransferMoneyResponse>}
 */
const methodDescriptor_FintechService_TransferMoney = new grpc.web.MethodDescriptor(
	'/fintech.FintechService/TransferMoney',
	grpc.web.MethodType.UNARY,
	proto.fintech.TransferMoneyRequest,
	proto.fintech.TransferMoneyResponse,
	/**
   * @param {!proto.fintech.TransferMoneyRequest} request
   * @return {!Uint8Array}
   */
	function(request) {
		return request.serializeBinary();
	},
	proto.fintech.TransferMoneyResponse.deserializeBinary
);

/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.fintech.TransferMoneyRequest,
 *   !proto.fintech.TransferMoneyResponse>}
 */
const methodInfo_FintechService_TransferMoney = new grpc.web.AbstractClientBase.MethodInfo(
	proto.fintech.TransferMoneyResponse,
	/**
   * @param {!proto.fintech.TransferMoneyRequest} request
   * @return {!Uint8Array}
   */
	function(request) {
		return request.serializeBinary();
	},
	proto.fintech.TransferMoneyResponse.deserializeBinary
);

/**
 * @param {!proto.fintech.TransferMoneyRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @param {function(?grpc.web.Error, ?proto.fintech.TransferMoneyResponse)}
 *     callback The callback function(error, response)
 * @return {!grpc.web.ClientReadableStream<!proto.fintech.TransferMoneyResponse>|undefined}
 *     The XHR Node Readable Stream
 */
proto.fintech.FintechServiceClient.prototype.transferMoney = function(request, metadata, callback) {
	return this.client_.rpcCall(
		this.hostname_ + '/fintech.FintechService/TransferMoney',
		request,
		metadata || {},
		methodDescriptor_FintechService_TransferMoney,
		callback
	);
};

/**
 * @param {!proto.fintech.TransferMoneyRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.fintech.TransferMoneyResponse>}
 *     Promise that resolves to the response
 */
proto.fintech.FintechServicePromiseClient.prototype.transferMoney = function(request, metadata) {
	return this.client_.unaryCall(
		this.hostname_ + '/fintech.FintechService/TransferMoney',
		request,
		metadata || {},
		methodDescriptor_FintechService_TransferMoney
	);
};

/**
 * @const
 * @type {!grpc.web.MethodDescriptor<
 *   !proto.fintech.GetHistoryRequest,
 *   !proto.fintech.GetHistoryResponse>}
 */
const methodDescriptor_FintechService_GetHistory = new grpc.web.MethodDescriptor(
	'/fintech.FintechService/GetHistory',
	grpc.web.MethodType.UNARY,
	proto.fintech.GetHistoryRequest,
	proto.fintech.GetHistoryResponse,
	/**
   * @param {!proto.fintech.GetHistoryRequest} request
   * @return {!Uint8Array}
   */
	function(request) {
		return request.serializeBinary();
	},
	proto.fintech.GetHistoryResponse.deserializeBinary
);

/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.fintech.GetHistoryRequest,
 *   !proto.fintech.GetHistoryResponse>}
 */
const methodInfo_FintechService_GetHistory = new grpc.web.AbstractClientBase.MethodInfo(
	proto.fintech.GetHistoryResponse,
	/**
   * @param {!proto.fintech.GetHistoryRequest} request
   * @return {!Uint8Array}
   */
	function(request) {
		return request.serializeBinary();
	},
	proto.fintech.GetHistoryResponse.deserializeBinary
);

/**
 * @param {!proto.fintech.GetHistoryRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @param {function(?grpc.web.Error, ?proto.fintech.GetHistoryResponse)}
 *     callback The callback function(error, response)
 * @return {!grpc.web.ClientReadableStream<!proto.fintech.GetHistoryResponse>|undefined}
 *     The XHR Node Readable Stream
 */
proto.fintech.FintechServiceClient.prototype.getHistory = function(request, metadata, callback) {
	return this.client_.rpcCall(
		this.hostname_ + '/fintech.FintechService/GetHistory',
		request,
		metadata || {},
		methodDescriptor_FintechService_GetHistory,
		callback
	);
};

/**
 * @param {!proto.fintech.GetHistoryRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.fintech.GetHistoryResponse>}
 *     Promise that resolves to the response
 */
proto.fintech.FintechServicePromiseClient.prototype.getHistory = function(request, metadata) {
	return this.client_.unaryCall(
		this.hostname_ + '/fintech.FintechService/GetHistory',
		request,
		metadata || {},
		methodDescriptor_FintechService_GetHistory
	);
};

module.exports = proto.fintech;
