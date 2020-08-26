# Redis Cache Data Specifications

| Key                                                       | Value                               | Type   | Description                                                                   |
|-----------------------------------------------------------|-------------------------------------|--------|-------------------------------------------------------------------------------|
| fooapp:user:{userId}:friends                              | userId1,userId2,userId3,...         | SET    | Store list friends of a user                                                  |
| fooapp:user:{userId}:info                                 | fullName,status,online              | HASH   | Store information of a user                                                   |
| fooapp:user:{userId}:conversations                        | conversationId1,conversationId2,... | LIST   | Store n recent conversations of a user (n <= 10).                             |
| fooapp:convesation:{conversationId}:info                  | name,lastMsg,groupchat,updateDate   | HASH   | Store information of a conversation for build address book.                   |
| fooapp:conversation:{conversationId}:recent_msg           | msgId1,msgId2,msgId3,...            | LIST   | Store n recent massage of a conversation (n <= 100) ( Using LPUSH and LTRIM ) |
| fooapp:message:{msgId}                                    | message,createdate,userId           | HASH   | Store a message include:                                                      |
| fooapp:conversation:{conversationId}:users                | user1Id,user2Id,user3Id,...         | SET    | Store list users of a conversation.                                           |
| fooapp:user:{username}                                    | userId                              | STRING | Support for check for a username is existed and friend of userId.             |
| fooapp:conversation:{conversationId}:{userId}:unseen_msgs | num                                 | STRING | Store number of unseen of a conversation of a user                            |
| fooapp:blacklist                                          | JWT1,JWT2,JWT3,...                  | SET    | Store log out JWTs of users.                                                  ||
