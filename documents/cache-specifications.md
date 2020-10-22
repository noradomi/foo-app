# Redis Cache Data Specifications

| Key                                                       | Value                               | Type   | Description                                                                   |
|-----------------------------------------------------------|-------------------------------------|--------|-------------------------------------------------------------------------------|
| ZALOPAY:FOOAPP:BLACKLIST:{JWT}                                          | 1                  | STRING    | Indicate a JWT of user when log out                       ||
| ZALOPAY:FOOAPP:MESSAGES:{USERID1}:{USERID2}                                          | A list n recent messages                 | STRING    | Store n recent messages between 2 user.                                                  ||
| ZALOPAY:FOOAPP:USERS                                          | A list of users                | LIST    | Store user list of system                   ||
| ZALOPAY:FOOAPP:USER:{USERID}:INFO                                          | fullname, username                | HASH    | Store info of user                  ||
| ZALOPAY:FOOAPP:USER:{USERID}:TRANSACTIONHISTORY                                          | A list of user's transaction history               | LIST                | Store 20 most recent transaction history of user     |                  ||
| ZALOPAY:FOOAPP:USER:{USERID}:FRIENDS                                          | A list of user's friend              | LIST                | Store friend list of user                   ||
