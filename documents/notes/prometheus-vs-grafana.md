# Prometheus vs Grafana

-Prometheus is an open-source systems monitoring and alerting toolkit originally built at SoundCloud.

- Most Prometheus components are written in `Go`.

## Time series database

- Những database chuyên dụng, được tối ưu để lưu trữ dữ liệu theo các mốc thời gian.

- Metrics (thông số): tình trạng server như lượng RAM, CPU đã dùng của mỗi service, số lượng request tới server,...

## Prometheus hoạt động như thế nào ?

- Prometheus sẽ chủ động pull các `metrics` về qua HTTP mỗi 10s hay 30s do ta thiết lập.
- Thứ giúp export ra các metrics cho prometheus pull là các `instructmentation/exporter`.
  - `Exporter`: là những app/tool dùng để export các metrics thu thập được.
  - `Instrumentation`: là những client-libraries được cung cấp bởi Prometheus hoặc bên thứ 3 để ta cài vào app của mình, giúp tùy biến các metrics riêng như số lượng người đã login vào website...

- PromQL: là một ngôn ngữ truy vấn dữ liệu được Prometheus tạo ra.