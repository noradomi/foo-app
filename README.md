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
    - [5. Chat 1-1](#5-chat-1-1)
    - [6. Chat group](#6-chat-group)
    - [7. Add friends](#7-add-friends)
    - [8. Remove friends](#8-remove-friends)
    - [9. Notification](#9-notification)
  - [Database Diagram](#database-diagram)

## Overview

## Architecture model

## Features

## How to run

## Demo

## Technical stack

## Sequence diagrams for API

### 1. Login

```plantuml
@startuml

    hide footbox
    actor User
    "User" -> "App Server": login with username and password
    activate "App Server"
    "App Server" -> "MySql Server": Validate user with hash password
    activate "MySql Server"
    "MySql Server" --> "App Server": OK
    deactivate "MySql Server"
    "App Server" --> "App Server": Create JWT
    "App Server" -> "Redis Server": Store userId in cache
    activate "Redis Server"
    "Redis Server" --> "App Server": OK
    deactivate "Redis Server"
    "App Server" --> "User": Return JWT to User
    deactivate "App Server"
    "User" -> "App Server": Attach JWT when making request
@enduml
```

### 2. Logout

```plantuml
@startuml
    hide footbox

    actor "User"

    "User" -> "App Server": User send JWT to logout
    activate "App Server"
    "App Server" -> "Redis Server": Add JWT to token Blacklist
    "App Server" -> "App Server": Logging result
    "App Server" --> "User": Return result to user
    deactivate "App Server"

@enduml
```

### 3. Register

```plantuml
@startuml
    hide footbox

    actor "User"

    "User" -> "App Server": Send info to register
    activate "App Server"
    "App Server" -> "MySql Server": Store user with hashed password
    "App Server" -> "Redis Server": Store user name and hashed password
    "App Server" -> "App Server": Logging result
    "App Server" --> "User": Return result to user
    deactivate "App Server"

@enduml
```

### 4. View list users/friend

```plantuml
@startuml
    hide footbox

    actor "User"

    "User" -> "App Server": Sent a GET with JWT request to get friend list
    "App Server" -> "Redis Server": Check JWT BlackList
    "App Server" -> "MySql Server": Validate JWT then get friend list

    "App Server" -> "User": Return result to User

@enduml
```

### 5. Chat 1-1

```plantuml
    skinparam backgroundColor #EEEBDC
    skinparam handwritten true
    @startuml
    hide footbox

    actor "User A"
    actor "User B"

    note over "User A": User A was handshaked and subscribed
    "User A" -> "App Server": Send a message to user B
    "App Server" --> "User B": Send message to topic <user_id B> \nwhich user B was subscribed
    "App Server" -> "MySql Server": Store this message

    @enduml
```

### 6. Chat group

```plantuml
@startuml
    hide footbox

    actor "User A"
    actor "Other Users"

    note over "User A": User A was handshaked and subscribed
    "User A" -> "App Server": Send a message to other users in group chat
    "App Server" --> "Other Users": Send message to topics \nwhich other users was subscribed base on <user_id>
    "App Server" -> "MySql Server": Store this message

@enduml
```

### 7. Add friends

```plantuml
@startuml
    hide footbox
  
    actor "User A"
    "User A" -> "App Server" : Sent a POST request (with JWT + username) \nto add new friend
    activate "App Server"
    "App Server" -> "Redis Server": Check JWT BlackList
    "App Server" -> "MySql Server": Validate JWT then notify to User B
    actor "User B"

    "App Server" -> "User B": notify
    activate "User B"
    "User B" --> "App Server": accept
    deactivate "User B"
    "App Server" -> "MySql Server": store to database
    "App Server" --> "User A": notify
    deactivate "App Server"
@enduml
```

### 8. Remove friends

```plantuml
@startuml
hide footbox

hide footbox

actor "User A"

"User A" -> "App Server": Sent a POST request (with JWT + username) \nto remove a friend
"App Server" -> "MySql Server": Check JWT BlackList
"App Server" -> "MySql Server": Validate JWT then remove in friend list
"App Server" -> "User A": Return result to user A

@enduml
```

### 9. Notification

```plantuml
@startuml
    hide footbox

    actor "User A"
    note over "User A": User A was handshaked and subscribed
    "App Server" --> "User A": When a friend of user A online or new message come to user A,\nServer will push notify to topic <user id A>

@enduml
```

## Database Diagram

![db-diagram](img/db.png)
