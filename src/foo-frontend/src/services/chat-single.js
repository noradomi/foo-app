
import store from '../redux/fooStore';
import { webSocketConnected} from '../redux/fooAction';
import { getUserIdFromStorage } from '../utils/utils';
import {initialWebSocket} from './init-websocket'
import {api} from './api';

// let ws = null;

export function wsConnect() {
    let webSocket = initialWebSocket();

    let send = function(receiverId, message) {
        let sentMessage = {
            receiver_id: receiverId,
            msg: message,
            type: 'SEND'
        };
        // webSocket.send(JSON.stringify(sentMessage));
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
            console.log(data.items);
            resolve(data);
        })
    });
}