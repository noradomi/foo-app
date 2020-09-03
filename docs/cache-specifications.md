# Redis Cache Data Specifications

| Key                                                       | Value                               | Type   | Description                                                                   |
|-----------------------------------------------------------|-------------------------------------|--------|-------------------------------------------------------------------------------|
| fooapp:user:{userId}:friends                              | userId1,userId2,userId3,...         | SET    | Store list friends of a user                                                  |
| fooapp:user:{userId}:info                                 | fullName, username           | HASH   | Store information of a user                                                   |
| fooapp:user:{userId}:conversations                        | conversationId1,conversationId2,... | LIST   | Store n recent conversations of a user (n <= 10).                             |
| fooapp:convesation:{conversationId}:info                  | name,lastMsg,groupchat,updateDate   | HASH   | Store information of a conversation for build address book.                   |
| fooapp:conversation:{conversationId}:recent_msg           | msgId1,msgId2,msgId3,...            | LIST   | Store n recent massage of a conversation (n <= 100) ( Using LPUSH and LTRIM ) |
| fooapp:message:{msgId}                                    | message,createdate,userId           | HASH   | Store a message include:                                                      |
| fooapp:conversation:{conversationId}:users                | user1Id,user2Id,user3Id,...         | SET    | Store list users of a conversation.                                           |
| fooapp:user:{username}                                    | userId                              | STRING | Support for check for a username is existed and friend of userId.             |
| fooapp:conversation:{conversationId}:{userId}:unseen_msgs | num                                 | STRING | Store number of unseen of a conversation of a user                            |
| ZALOPAY:FOOAPP:BLACKLIST:{JWT}                                          | 1                  | STRING    | Mark a JWT of user when log out                       ||
| ZALOPAY:FOOAPP:MESSAGES:{USERID1}:{USERID2}                                          | A list 20 recent messages                 | STRING    | Store 20 recent messages between 2 user.                                                  ||
| ZALOPAY:FOOAPP:USER:{USERID}:{STATUS}                                          | 1                 | STRING    | Indicate user is online                   ||
| ZALOPAY:FOOAPP:USERS                                          | A list of users                | STRING    | Store user list of system                   ||
| ZALOPAY:FOOAPP:USER:{USERID}:INFO                                          | fullname, username                | HASH    | Store info of user                  ||
