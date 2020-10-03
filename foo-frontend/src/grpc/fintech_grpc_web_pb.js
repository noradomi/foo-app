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
proto.fintech.FintechServiceClient =
    function(hostname, credentials, options) {
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
proto.fintech.FintechServicePromiseClient =
    function(hostname, credentials, options) {
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
proto.fintech.FintechServiceClient.prototype.getBalance =
    function(request, metadata, callback) {
  return this.client_.rpcCall(this.hostname_ +
      '/fintech.FintechService/GetBalance',
      request,
      metadata || {},
      methodDescriptor_FintechService_GetBalance,
      callback);
};


/**
 * @param {!proto.fintech.GetBalanceRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.fintech.GetBalanceResponse>}
 *     Promise that resolves to the response
 */
proto.fintech.FintechServicePromiseClient.prototype.getBalance =
    function(request, metadata) {
  return this.client_.unaryCall(this.hostname_ +
      '/fintech.FintechService/GetBalance',
      request,
      metadata || {},
      methodDescriptor_FintechService_GetBalance);
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
proto.fintech.FintechServiceClient.prototype.transferMoney =
    function(request, metadata, callback) {
  return this.client_.rpcCall(this.hostname_ +
      '/fintech.FintechService/TransferMoney',
      request,
      metadata || {},
      methodDescriptor_FintechService_TransferMoney,
      callback);
};


/**
 * @param {!proto.fintech.TransferMoneyRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.fintech.TransferMoneyResponse>}
 *     Promise that resolves to the response
 */
proto.fintech.FintechServicePromiseClient.prototype.transferMoney =
    function(request, metadata) {
  return this.client_.unaryCall(this.hostname_ +
      '/fintech.FintechService/TransferMoney',
      request,
      metadata || {},
      methodDescriptor_FintechService_TransferMoney);
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
proto.fintech.FintechServiceClient.prototype.getHistory =
    function(request, metadata, callback) {
  return this.client_.rpcCall(this.hostname_ +
      '/fintech.FintechService/GetHistory',
      request,
      metadata || {},
      methodDescriptor_FintechService_GetHistory,
      callback);
};


/**
 * @param {!proto.fintech.GetHistoryRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.fintech.GetHistoryResponse>}
 *     Promise that resolves to the response
 */
proto.fintech.FintechServicePromiseClient.prototype.getHistory =
    function(request, metadata) {
  return this.client_.unaryCall(this.hostname_ +
      '/fintech.FintechService/GetHistory',
      request,
      metadata || {},
      methodDescriptor_FintechService_GetHistory);
};


/**
 * @const
 * @type {!grpc.web.MethodDescriptor<
 *   !proto.fintech.GetNotificationsRequest,
 *   !proto.fintech.GetNotificationsResponse>}
 */
const methodDescriptor_FintechService_GetNotifications = new grpc.web.MethodDescriptor(
  '/fintech.FintechService/GetNotifications',
  grpc.web.MethodType.UNARY,
  proto.fintech.GetNotificationsRequest,
  proto.fintech.GetNotificationsResponse,
  /**
   * @param {!proto.fintech.GetNotificationsRequest} request
   * @return {!Uint8Array}
   */
  function(request) {
    return request.serializeBinary();
  },
  proto.fintech.GetNotificationsResponse.deserializeBinary
);


/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.fintech.GetNotificationsRequest,
 *   !proto.fintech.GetNotificationsResponse>}
 */
const methodInfo_FintechService_GetNotifications = new grpc.web.AbstractClientBase.MethodInfo(
  proto.fintech.GetNotificationsResponse,
  /**
   * @param {!proto.fintech.GetNotificationsRequest} request
   * @return {!Uint8Array}
   */
  function(request) {
    return request.serializeBinary();
  },
  proto.fintech.GetNotificationsResponse.deserializeBinary
);


/**
 * @param {!proto.fintech.GetNotificationsRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @param {function(?grpc.web.Error, ?proto.fintech.GetNotificationsResponse)}
 *     callback The callback function(error, response)
 * @return {!grpc.web.ClientReadableStream<!proto.fintech.GetNotificationsResponse>|undefined}
 *     The XHR Node Readable Stream
 */
proto.fintech.FintechServiceClient.prototype.getNotifications =
    function(request, metadata, callback) {
  return this.client_.rpcCall(this.hostname_ +
      '/fintech.FintechService/GetNotifications',
      request,
      metadata || {},
      methodDescriptor_FintechService_GetNotifications,
      callback);
};


/**
 * @param {!proto.fintech.GetNotificationsRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.fintech.GetNotificationsResponse>}
 *     Promise that resolves to the response
 */
proto.fintech.FintechServicePromiseClient.prototype.getNotifications =
    function(request, metadata) {
  return this.client_.unaryCall(this.hostname_ +
      '/fintech.FintechService/GetNotifications',
      request,
      metadata || {},
      methodDescriptor_FintechService_GetNotifications);
};


/**
 * @const
 * @type {!grpc.web.MethodDescriptor<
 *   !proto.fintech.GetStrangerListRequest,
 *   !proto.fintech.GetStrangerListResponse>}
 */
const methodDescriptor_FintechService_GetStrangerList = new grpc.web.MethodDescriptor(
  '/fintech.FintechService/GetStrangerList',
  grpc.web.MethodType.UNARY,
  proto.fintech.GetStrangerListRequest,
  proto.fintech.GetStrangerListResponse,
  /**
   * @param {!proto.fintech.GetStrangerListRequest} request
   * @return {!Uint8Array}
   */
  function(request) {
    return request.serializeBinary();
  },
  proto.fintech.GetStrangerListResponse.deserializeBinary
);


/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.fintech.GetStrangerListRequest,
 *   !proto.fintech.GetStrangerListResponse>}
 */
const methodInfo_FintechService_GetStrangerList = new grpc.web.AbstractClientBase.MethodInfo(
  proto.fintech.GetStrangerListResponse,
  /**
   * @param {!proto.fintech.GetStrangerListRequest} request
   * @return {!Uint8Array}
   */
  function(request) {
    return request.serializeBinary();
  },
  proto.fintech.GetStrangerListResponse.deserializeBinary
);


/**
 * @param {!proto.fintech.GetStrangerListRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @param {function(?grpc.web.Error, ?proto.fintech.GetStrangerListResponse)}
 *     callback The callback function(error, response)
 * @return {!grpc.web.ClientReadableStream<!proto.fintech.GetStrangerListResponse>|undefined}
 *     The XHR Node Readable Stream
 */
proto.fintech.FintechServiceClient.prototype.getStrangerList =
    function(request, metadata, callback) {
  return this.client_.rpcCall(this.hostname_ +
      '/fintech.FintechService/GetStrangerList',
      request,
      metadata || {},
      methodDescriptor_FintechService_GetStrangerList,
      callback);
};


/**
 * @param {!proto.fintech.GetStrangerListRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.fintech.GetStrangerListResponse>}
 *     Promise that resolves to the response
 */
proto.fintech.FintechServicePromiseClient.prototype.getStrangerList =
    function(request, metadata) {
  return this.client_.unaryCall(this.hostname_ +
      '/fintech.FintechService/GetStrangerList',
      request,
      metadata || {},
      methodDescriptor_FintechService_GetStrangerList);
};


/**
 * @const
 * @type {!grpc.web.MethodDescriptor<
 *   !proto.fintech.AddFriendRequest,
 *   !proto.fintech.AddFriendResponse>}
 */
const methodDescriptor_FintechService_AddFriend = new grpc.web.MethodDescriptor(
  '/fintech.FintechService/AddFriend',
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
const methodInfo_FintechService_AddFriend = new grpc.web.AbstractClientBase.MethodInfo(
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
proto.fintech.FintechServiceClient.prototype.addFriend =
    function(request, metadata, callback) {
  return this.client_.rpcCall(this.hostname_ +
      '/fintech.FintechService/AddFriend',
      request,
      metadata || {},
      methodDescriptor_FintechService_AddFriend,
      callback);
};


/**
 * @param {!proto.fintech.AddFriendRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.fintech.AddFriendResponse>}
 *     Promise that resolves to the response
 */
proto.fintech.FintechServicePromiseClient.prototype.addFriend =
    function(request, metadata) {
  return this.client_.unaryCall(this.hostname_ +
      '/fintech.FintechService/AddFriend',
      request,
      metadata || {},
      methodDescriptor_FintechService_AddFriend);
};


/**
 * @const
 * @type {!grpc.web.MethodDescriptor<
 *   !proto.fintech.ResetUnseenRequest,
 *   !proto.fintech.ResetUnseenResponse>}
 */
const methodDescriptor_FintechService_ResetUnseen = new grpc.web.MethodDescriptor(
  '/fintech.FintechService/ResetUnseen',
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
const methodInfo_FintechService_ResetUnseen = new grpc.web.AbstractClientBase.MethodInfo(
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
proto.fintech.FintechServiceClient.prototype.resetUnseen =
    function(request, metadata, callback) {
  return this.client_.rpcCall(this.hostname_ +
      '/fintech.FintechService/ResetUnseen',
      request,
      metadata || {},
      methodDescriptor_FintechService_ResetUnseen,
      callback);
};


/**
 * @param {!proto.fintech.ResetUnseenRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.fintech.ResetUnseenResponse>}
 *     Promise that resolves to the response
 */
proto.fintech.FintechServicePromiseClient.prototype.resetUnseen =
    function(request, metadata) {
  return this.client_.unaryCall(this.hostname_ +
      '/fintech.FintechService/ResetUnseen',
      request,
      metadata || {},
      methodDescriptor_FintechService_ResetUnseen);
};


/**
 * @const
 * @type {!grpc.web.MethodDescriptor<
 *   !proto.fintech.GetUserInfoRequest,
 *   !proto.fintech.GetUserInfoResponse>}
 */
const methodDescriptor_FintechService_GetUserInfo = new grpc.web.MethodDescriptor(
  '/fintech.FintechService/GetUserInfo',
  grpc.web.MethodType.UNARY,
  proto.fintech.GetUserInfoRequest,
  proto.fintech.GetUserInfoResponse,
  /**
   * @param {!proto.fintech.GetUserInfoRequest} request
   * @return {!Uint8Array}
   */
  function(request) {
    return request.serializeBinary();
  },
  proto.fintech.GetUserInfoResponse.deserializeBinary
);


/**
 * @const
 * @type {!grpc.web.AbstractClientBase.MethodInfo<
 *   !proto.fintech.GetUserInfoRequest,
 *   !proto.fintech.GetUserInfoResponse>}
 */
const methodInfo_FintechService_GetUserInfo = new grpc.web.AbstractClientBase.MethodInfo(
  proto.fintech.GetUserInfoResponse,
  /**
   * @param {!proto.fintech.GetUserInfoRequest} request
   * @return {!Uint8Array}
   */
  function(request) {
    return request.serializeBinary();
  },
  proto.fintech.GetUserInfoResponse.deserializeBinary
);


/**
 * @param {!proto.fintech.GetUserInfoRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @param {function(?grpc.web.Error, ?proto.fintech.GetUserInfoResponse)}
 *     callback The callback function(error, response)
 * @return {!grpc.web.ClientReadableStream<!proto.fintech.GetUserInfoResponse>|undefined}
 *     The XHR Node Readable Stream
 */
proto.fintech.FintechServiceClient.prototype.getUserInfo =
    function(request, metadata, callback) {
  return this.client_.rpcCall(this.hostname_ +
      '/fintech.FintechService/GetUserInfo',
      request,
      metadata || {},
      methodDescriptor_FintechService_GetUserInfo,
      callback);
};


/**
 * @param {!proto.fintech.GetUserInfoRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.fintech.GetUserInfoResponse>}
 *     Promise that resolves to the response
 */
proto.fintech.FintechServicePromiseClient.prototype.getUserInfo =
    function(request, metadata) {
  return this.client_.unaryCall(this.hostname_ +
      '/fintech.FintechService/GetUserInfo',
      request,
      metadata || {},
      methodDescriptor_FintechService_GetUserInfo);
};


/**
 * @const
 * @type {!grpc.web.MethodDescriptor<
 *   !proto.fintech.GetFriendListRequest,
 *   !proto.fintech.GetFriendListResponse>}
 */
const methodDescriptor_FintechService_GetFriendList = new grpc.web.MethodDescriptor(
  '/fintech.FintechService/GetFriendList',
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
const methodInfo_FintechService_GetFriendList = new grpc.web.AbstractClientBase.MethodInfo(
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
proto.fintech.FintechServiceClient.prototype.getFriendList =
    function(request, metadata, callback) {
  return this.client_.rpcCall(this.hostname_ +
      '/fintech.FintechService/GetFriendList',
      request,
      metadata || {},
      methodDescriptor_FintechService_GetFriendList,
      callback);
};


/**
 * @param {!proto.fintech.GetFriendListRequest} request The
 *     request proto
 * @param {?Object<string, string>} metadata User defined
 *     call metadata
 * @return {!Promise<!proto.fintech.GetFriendListResponse>}
 *     Promise that resolves to the response
 */
proto.fintech.FintechServicePromiseClient.prototype.getFriendList =
    function(request, metadata) {
  return this.client_.unaryCall(this.hostname_ +
      '/fintech.FintechService/GetFriendList',
      request,
      metadata || {},
      methodDescriptor_FintechService_GetFriendList);
};


module.exports = proto.fintech;

