# Foo App

---------------

- [Foo App](#foo-app)
  - [1. Tổng quan](#1-tổng-quan)
  - [2. Hướng dẫn chạy](#2-hướng-dẫn-chạy)
  - [3. Demo (developing ...)](#3-demo-developing-)
  - [4. References](#4-references)

## 1. Tổng quan

**Foo App** là một training project  thuộc chương trình  ZaloPay Fresher 2020 sử dụng React JS, Vert.x để xây dụng một ứng dựng chat real-time đơn giản bao gồm các chức năng cơ bản:

- Đăng nhâp / Đăng xuất (có sử dụng JWT)
- Đăng kí tài khoản
- Xem danh sách tất cả người dùng của hệ thống
- Chat 1 - 1
- **Chuyển tiền cho một người trong danh sách user**
- **Xem số dư hiện tại**
- **Xem lịch sử giao dịch**
- **Nhắc nợ cá nhân**

Các công nghệ sử dụng:

- `React`, `Redux` cho phía client.
- `Java Vert.x` cho việc xây dựng API cho server.
- `WebSocket` cho việc gửi/nhận tin nhắn real time.
- `MySQL` làm database chính và `Redis` cho cache.

## 2. Hướng dẫn chạy

- Mọi quá trình để build và thực thi project đã được viết trong file docker-compose.yml. Tại thư mục gốc của repo, chạy lệnh :

```bash
docker-compose up --build
```

- Quá trình chạy lần đầu có thể sẽ tốn nhiều thời gian cho việc build service backend.

- Ứng dụng client sẽ chạy trên port `3000`. Test trên trình duyệt tại `localhost:3000`.

## 3. Demo (developing ...)

## 4. References

- [Sequence diagrams & Database model](documents/sequence-diagrams.md)

- [Database model](documents/database-model.md)

- [Database DDL script](documents/scripts.sql)

- [gRPC API](documents/grpc-api/fintech.proto)

- [Mockups](https://balsamiq.cloud/smos4qm/phtej9t)

- [Redis cache specifications](documents/cache-specifications.md)

- [API & Web-socket Specifications](https://app.swaggerhub.com/apis/NoRaDoMi/Foo_Chat_Application/1.0.0)
