
import store from '../redux/fooStore';
import { webSocketConnected} from '../redux/fooAction';
import {initialWebSocket} from './init-websocket'
import {api} from './api';

export function wsConnect() {
    let webSocket = initialWebSocket();

    let send = function(receiverId, message) {
        let sentMessage = {
            receiverId: receiverId,
            message: message,
            type: 'SEND'
        };
        webSocket.json(sentMessage);
    };

    store.dispatch(webSocketConnected(webSocket, send));
}

export function getMessageList(friendId,offset){
    return new Promise((resolve) => {
        api
        .authGet(`/api/protected/messages/${friendId}/${offset}`)
        .then((response) => {
            let data = response.data.data;
            resolve(data);
        })
    });
}