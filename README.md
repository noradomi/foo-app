# Foo App

---------------

- [Foo App](#foo-app)
  - [1. Tổng quan](#1-tổng-quan)
  - [2. Demo](#2-demo)
  - [3. Hướng dẫn chạy](#3-hướng-dẫn-chạy)
  - [4. Document](#4-document)
  - [5. Tác giả](#5-tác-giả)
  - [6.Acknowledgments](#6acknowledgments)

## 1. Tổng quan

**Foo App** là một training project  thuộc chương trình  ZaloPay Fresher 2020 sử dụng React JS, Vert.x để xây dụng một ứng dựng chat real-time đơn giản kết hợp với các chức năng mô phỏng một ví điện tử bao gồm:

- Đăng nhâp / Đăng xuất (có sử dụng JWT)
- Đăng kí tài khoản
- Xem danh sách tất cả bạn bè trong hệ thống
- Chat 1 - 1
- Kết bạn
- User status: online/offline
- Thông báo tin nhắn mới, giao dịch mới
- Responsive trên các nhiều thiết bị: laptop, mobile phone.

Các công nghệ sử dụng:

- `React`, `Redux` cho xây dựng client.
- `Ant.design` cho thiết kế UI.
- `Java Vert.x` cho việc xây dựng API, Websocket server.
- `WebSocket` cho việc gửi/nhận tin nhắn, thông báo real time.
- `MySQL` làm database chính
- `Redis` cho caching data.
- `gRPC` cho việc xây dựng các chức năng fintech.
- `Prometheus`, `Grafana` cho monitoring.

## 2. Demo

![demo](media/demo-fooapp.gif)

## 3. Hướng dẫn chạy

- Mọi quá trình để build và thực thi project đã được viết trong file docker-compose.yml. Tại thư mục gốc của repo, chạy lệnh :

```bash
docker-compose up --build
```

- Quá trình chạy lần đầu có thể sẽ tốn nhiều thời gian cho việc build service backend.

- Ứng dụng client sẽ chạy trên port `3006`. Truy cập trình duyệt tại `localhost:3006`.

## 4. Document

https://github.com/NoRaDoMi/foo-app/wiki

## 5. Tác giả

- PhucVT - ZaloPay Fresher

## 6.Acknowledgments

- Mr Anh Duc Anh - Principal Software Engineer at ZaloPay
- Mr Anh Thieu - Senior Software EngineerZaloPay Core Backend
- Mr Anh Tai - Software EngineerZaloPay Core Backend
- Mr Anh Toan - Software EngineerZaloPay Core Backend
