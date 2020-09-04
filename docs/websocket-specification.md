# Web socket specifications

------------

## 1. Client

- Request: client sẽ gửi tới server 1 object có giá trị message cần gửi và id của user nhận.
  - url: `ws://localhost:9009/chat?jwt=....`
  - Payload:
    ```javascript
    {
        receiver_id: receiverId,
        msg: message,
        type: 'SEND'
    };
    ```

## 2. Server

- Response: trả về client 1 object `WsMessage` vói giá trị `type` để client biết được chức năng cần xử lí

``` java
public class WsMessage {
    // ONLINE, OFFLINE, SEND, FETCH
    private String type;
   
    private String sender_id;
   
    private String receiver_id;
   
    private String msg;
   
    private long create_date;
}
```
