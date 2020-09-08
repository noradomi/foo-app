# Foo App

---------------

- [Foo App](#foo-app)
  - [1. Tổng quan](#1-tổng-quan)
  - [2. Hướng dẫn chạy](#2-hướng-dẫn-chạy)
  - [3. Demo](#3-demo)
    - [3.1 Đăng nhập](#31-đăng-nhập)
    - [3.2 Đăng kí](#32-đăng-kí)
    - [3.1 Xem danh sách user](#31-xem-danh-sách-user)
    - [3.4 Chat 1 - 1](#34-chat-1---1)
  - [4. References](#4-references)

## 1. Tổng quan

**Foo App** là một training project  thuộc chương trình  ZaloPay Fresher 2020 sử dụng React JS, Vert.x để xây dụng một ứng dựng chat real-time đơn giản bao gồm các chức năng cơ bản:

- Đăng nhâp / Đăng xuất (có sử dụng JWT)
- Đăng kí tài khoản
- Xem danh sách tất cả người dùng của hệ thống
- Chat 1 - 1

Các công nghệ sử dụng:

- `React`, `Redux` cho phía client.
- `Java Vert.x` cho việc xây dựng API cho server.
- `WebSocket` cho việc gửi/nhận tin nhắn real time.
- `MySQL` làm database chính và `Redis` cho cache.

## 2. Hướng dẫn chạy

- Mọi quá trình để build và thực thi project đã được viết trong filedocker-compose.yml. Tại thư mục `src` của project, chỉ cần chạy lệnh :

```bash
docker-compose up --build
```

- Quá trình chạy lần đầu có thể sẽ tốn nhiều thời gian cho việc build service backend.

- Ứng dụng client sẽ chạy trên port `3000`. Test trên trình duyệt tại `localhost:3000`.

## 3. Demo

### 3.1 Đăng nhập

![login](imgs/login.png)

### 3.2 Đăng kí

![register](imgs/register.png)

### 3.1 Xem danh sách user

![view-user-list](imgs/view-user-list.png)

### 3.4 Chat 1 - 1

![chat-1-1](imgs/chat-one-one.png)

## 4. References

- [Sequence diagrams](docs/sequence-diagrams.md)

- [Database model](docs/database-model.md)

- [Redis cache specifications](docs/cache-specifications.md)

- [API & Web-socket Specifications](https://app.swaggerhub.com/apis/NoRaDoMi/Foo_Chat_Application/1.0.0)
