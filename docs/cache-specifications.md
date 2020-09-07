# Redis Cache Data Specifications

| Key                                                       | Value                               | Type   | Description                                                                   |
|-----------------------------------------------------------|-------------------------------------|--------|-------------------------------------------------------------------------------|
| ZALOPAY:FOOAPP:BLACKLIST:{JWT}                                          | 1                  | STRING    | Indicate a JWT of user when log out                       ||
| ZALOPAY:FOOAPP:MESSAGES:{USERID1}:{USERID2}                                          | A list n recent messages                 | STRING    | Store n recent messages between 2 user.                                                  ||
| ZALOPAY:FOOAPP:USERS                                          | A list of users                | STRING    | Store user list of system                   ||
| ZALOPAY:FOOAPP:USER:{USERID}:INFO                                          | fullname, username                | HASH    | Store info of user                  ||
