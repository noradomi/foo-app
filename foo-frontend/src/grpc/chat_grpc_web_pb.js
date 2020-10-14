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

var fintech_code_pb = require('./code_pb.js');

var fintech_status_pb = require('./status_pb.js');
const proto = {};
proto.fintech = require('./chat_pb.js');

/**
 * @param {string} hostname
 * @param {?Object} credentials
 * @param {?Object} options
 * @constructor
 * @struct
 * @final
 */
proto.fintech.ChatServiceClient = function(hostname, credentials, options) {
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
proto.fintech.ChatServicePromiseClient = function(hostname, credentials, options) {
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
 *   !proto.fintech.AddFriendRequest,
 *   !proto.fintech.AddFriendResponse>}
 */
const methodDescriptor_ChatService_AddFriend = new grpc.web.MethodDescriptor(
	'/fintech.ChatService/AddFriend',
	grpc.web.MethodType.UNARY,
	proto.fintech.AddFriendRequest,
	proto.fintech.AddFriendResponse,
	/**
   * @param {!proto.fintech.AddFriendRequest} request
   * @return {!Uint8Array}
   */
	function(request) {
		return request.serializeBinary();
	},
	proto.fintech.AddFriendResponse.deserializeBinary
);

/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.fintech.AddFriendRequest,
 *   !proto.fintech.AddFriendResponse>}
 */
const methodInfo_ChatService_AddFriend = new grpc.web.AbstractClientBase.MethodInfo(
	proto.fintech.AddFriendResponse,
	/**
   * @param {!proto.fintech.AddFriendRequest} request
   * @return {!Uint8Array}
   */
	function(request) {
		return request.serializeBinary();
	},
	proto.fintech.AddFriendResponse.deserializeBinary
);

/**
 * @param {!proto.fintech.AddFriendRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @param {function(?grpc.web.Error, ?proto.fintech.AddFriendResponse)}
 *     callback The callback function(error, response)
 * @return {!grpc.web.ClientReadableStream<!proto.fintech.AddFriendResponse>|undefined}
 *     The XHR Node Readable Stream
 */
proto.fintech.ChatServiceClient.prototype.addFriend = function(request, metadata, callback) {
	return this.client_.rpcCall(
		this.hostname_ + '/fintech.ChatService/AddFriend',
		request,
		metadata || {},
		methodDescriptor_ChatService_AddFriend,
		callback
	);
};

/**
 * @param {!proto.fintech.AddFriendRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.fintech.AddFriendResponse>}
 *     Promise that resolves to the response
 */
proto.fintech.ChatServicePromiseClient.prototype.addFriend = function(request, metadata) {
	return this.client_.unaryCall(
		this.hostname_ + '/fintech.ChatService/AddFriend',
		request,
		metadata || {},
		methodDescriptor_ChatService_AddFriend
	);
};

/**
 * @const
 * @type {!grpc.web.MethodDescriptor<
 *   !proto.fintech.ResetUnseenRequest,
 *   !proto.fintech.ResetUnseenResponse>}
 */
const methodDescriptor_ChatService_ResetUnseen = new grpc.web.MethodDescriptor(
	'/fintech.ChatService/ResetUnseen',
	grpc.web.MethodType.UNARY,
	proto.fintech.ResetUnseenRequest,
	proto.fintech.ResetUnseenResponse,
	/**
   * @param {!proto.fintech.ResetUnseenRequest} request
   * @return {!Uint8Array}
   */
	function(request) {
		return request.serializeBinary();
	},
	proto.fintech.ResetUnseenResponse.deserializeBinary
);

/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.fintech.ResetUnseenRequest,
 *   !proto.fintech.ResetUnseenResponse>}
 */
const methodInfo_ChatService_ResetUnseen = new grpc.web.AbstractClientBase.MethodInfo(
	proto.fintech.ResetUnseenResponse,
	/**
   * @param {!proto.fintech.ResetUnseenRequest} request
   * @return {!Uint8Array}
   */
	function(request) {
		return request.serializeBinary();
	},
	proto.fintech.ResetUnseenResponse.deserializeBinary
);

/**
 * @param {!proto.fintech.ResetUnseenRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @param {function(?grpc.web.Error, ?proto.fintech.ResetUnseenResponse)}
 *     callback The callback function(error, response)
 * @return {!grpc.web.ClientReadableStream<!proto.fintech.ResetUnseenResponse>|undefined}
 *     The XHR Node Readable Stream
 */
proto.fintech.ChatServiceClient.prototype.resetUnseen = function(request, metadata, callback) {
	return this.client_.rpcCall(
		this.hostname_ + '/fintech.ChatService/ResetUnseen',
		request,
		metadata || {},
		methodDescriptor_ChatService_ResetUnseen,
		callback
	);
};

/**
 * @param {!proto.fintech.ResetUnseenRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.fintech.ResetUnseenResponse>}
 *     Promise that resolves to the response
 */
proto.fintech.ChatServicePromiseClient.prototype.resetUnseen = function(request, metadata) {
	return this.client_.unaryCall(
		this.hostname_ + '/fintech.ChatService/ResetUnseen',
		request,
		metadata || {},
		methodDescriptor_ChatService_ResetUnseen
	);
};

/**
 * @const
 * @type {!grpc.web.MethodDescriptor<
 *   !proto.fintech.GetFriendListRequest,
 *   !proto.fintech.GetFriendListResponse>}
 */
const methodDescriptor_ChatService_GetFriendList = new grpc.web.MethodDescriptor(
	'/fintech.ChatService/GetFriendList',
	grpc.web.MethodType.UNARY,
	proto.fintech.GetFriendListRequest,
	proto.fintech.GetFriendListResponse,
	/**
   * @param {!proto.fintech.GetFriendListRequest} request
   * @return {!Uint8Array}
   */
	function(request) {
		return request.serializeBinary();
	},
	proto.fintech.GetFriendListResponse.deserializeBinary
);

/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.fintech.GetFriendListRequest,
 *   !proto.fintech.GetFriendListResponse>}
 */
const methodInfo_ChatService_GetFriendList = new grpc.web.AbstractClientBase.MethodInfo(
	proto.fintech.GetFriendListResponse,
	/**
   * @param {!proto.fintech.GetFriendListRequest} request
   * @return {!Uint8Array}
   */
	function(request) {
		return request.serializeBinary();
	},
	proto.fintech.GetFriendListResponse.deserializeBinary
);

/**
 * @param {!proto.fintech.GetFriendListRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @param {function(?grpc.web.Error, ?proto.fintech.GetFriendListResponse)}
 *     callback The callback function(error, response)
 * @return {!grpc.web.ClientReadableStream<!proto.fintech.GetFriendListResponse>|undefined}
 *     The XHR Node Readable Stream
 */
proto.fintech.ChatServiceClient.prototype.getFriendList = function(request, metadata, callback) {
	return this.client_.rpcCall(
		this.hostname_ + '/fintech.ChatService/GetFriendList',
		request,
		metadata || {},
		methodDescriptor_ChatService_GetFriendList,
		callback
	);
};

/**
 * @param {!proto.fintech.GetFriendListRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.fintech.GetFriendListResponse>}
 *     Promise that resolves to the response
 */
proto.fintech.ChatServicePromiseClient.prototype.getFriendList = function(request, metadata) {
	return this.client_.unaryCall(
		this.hostname_ + '/fintech.ChatService/GetFriendList',
		request,
		metadata || {},
		methodDescriptor_ChatService_GetFriendList
	);
};

module.exports = proto.fintech;
