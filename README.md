# Foo-App

- [Foo-App](#foo-app)
  - [Overview](#overview)
  - [Architecture model](#architecture-model)
  - [Features](#features)
  - [How to run](#how-to-run)
  - [Demo](#demo)
  - [Technical stack](#technical-stack)
  - [Sequence diagrams for API](#sequence-diagrams-for-api)
    - [1. Login](#1-login)
    - [2. Logout](#2-logout)
    - [3. Register](#3-register)
    - [4. View list users/friend](#4-view-list-usersfriend)
    - [5. Chat single](#5-chat-single)
    - [6. Chat group](#6-chat-group)
    - [7. Add friends](#7-add-friends)
    - [8. Remove friends](#8-remove-friends)
    - [9. Notification](#9-notification)
  - [Database Diagram](#database-diagram)
  - [Redis Cache Data Specifications](#redis-cache-data-specifications)

## Overview

## Architecture model

## Features

## How to run

## Demo

## Technical stack

## Sequence diagrams for API

### 1. Login

![login](img/seq-digarams/login.png)

### 2. Logout

![logout](img/seq-digarams/logout.png)

### 3. Register

![register](img/seq-digarams/register.png)

### 4. View list users/friend

![viewlistusers](img/seq-digarams/viewlistusers.png)

### 5. Chat single

![chatsingle](img/seq-digarams/chatsingle.png)

### 6. Chat group

![chatgroup](img/seq-digarams/chatgroup.png)

### 7. Add friends

- Type 1:

```plantuml
@startuml
    hide footbox
  
    actor "User A"
    "User A" -> "App Server" : Sent a POST request (with JWT + username) \nto add new friend
    activate "App Server"
    "App Server" -> "Redis Server": Check JWT BlackList
    actor "User B"
    "App Server" -> "MySql Server": Validate JWT then store request add friend to MySql
    "App Server" -> "User B": notify
    activate "User B"
    "User B" --> "App Server": accept
    deactivate "User B"
    "App Server" -> "MySql Server": store to MySql
    "App Server" --> "User A": notify
    deactivate "App Server"
@enduml
```

- Type 2:

```plantuml
@startuml
hide footbox

hide footbox

actor "User A"

"User A" -> "App Server": Sent a POST request (with JWT + username) \nto add a friend
"App Server" -> "Redis Server": Check JWT BlackList
"App Server" -> "App Server": Validate JWT
"App Server" -> "Redis Server": Check username exist
"Redis Server" -> "App Server": True
"App Server" -> "MySql Server": Store in friend list
"App Server" -> "User A": Return result to user A

@enduml
```

### 8. Remove friends

![removefriend](img/seq-digarams/removeafriend.png)

### 9. Notification

![notification](img/seq-digarams/notification.png)

## Database Diagram

![db-diagram](img/db.png)

## Redis Cache Data Specifications

| Key                 |              Value             |        Type | Description |
|---------------------|--------------------------------|-------------|----|
| foochat:user:{user_name}    | user_id,user_full_name, user_hashed_pwd | Hash        | Store info of user |
|foochat:blackblist           |jwt1,jwt2, ...                  |Set          | Store list invalid/expired JWT token |
|foochat:user_status:{user_id}|status                          |String       | Store status of a user |
|foochat:{user_id}:friends    |user_id1, user_id2,..           |Set          | Store list friends of a user |
|foochat:user:online    |user_id1, user_id2,..           |Set          | Store list online users |
|foochat:{user_id}:{conversation_id}:recentchat | msg_id1,msg_id2,... | Set | Store 10 messages of a conversations of a user |
