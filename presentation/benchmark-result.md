# Benchmark gRPC

## 1. Thông tin chung

### 1.1 Cấu hình OS

| **Loại** | **Thông số** |
|----------|----------|
| OS      | Ubuntu 64 bits (20.04.1 LTS)    |
| RAM      | 8 GB     |
| CPU      | 4 Core  |
| Disk     | SSD      |

### 1.2. Versions:

| **Application** | **Version**                               |
|-----------------|-------------------------------------------|
| Foo-backend            | 1.0-SNAPSHOT                                    |
| MySQL        | 8.0.21 |
| Redis        | 6.0.8 |
| Vertx        | 3.7.0 |
| gRPC        | 1.31.1 |

## 2. Công cụ benchmark

- Sử dụng `ghz` để benchmark gRPC service

## 3. Kịch bản

- Tạo 1000 users mẫu trong bảng users có id lần lượt từ 1 -> 1000,  với số dư ban đầu là 20.000.000 đ phục vụ cho benchmark.

- Sử dụng `ghz` lần lượt call các hàm chức năng trong gRPC service bao gồm: TransferMoney, GetBalance và GetHistory.

### 3.1 Transfer money

#### a. Trường hợp các user đều có đủ số dư thực hiện giao dịch

- Random ngẫu nhiên cặp userId của người chuyển và người nhận (1 <= userId <= 1000) cho mỗi request.

- Thực hiện lần lượt 500 -> 1000 request chuyển tiền với số tiền chuyển là 1.000 đ/giao dịch.

#### b. Trường hợp 500/1000 users ngẫu nhiên không có đủ số dư thực hiện giao dịch

- Chỉnh sửa các users trong bảng users có userId là số chẵn sẽ có số dư bằng 0.
- Thực hiện tương tự như trường hợp trên a

#### c. Trường hợp tất cả user đều không đủe số dư thực hiện giao dịch

- Set tât cả số dư của tât cả users trong bảng users bằng 0.
- Thực hiện tương tự như trường hợp trên a.

### 3.2 Get balance

#### a. Từ 1 user duy nhất

- Chọn 1 userId từ 1000 users trong bảng users để test
- Sử dụng `ghz` gọi hàm GetBalance của gRPC service với số request là 2000. 

#### b. Chọn ngẫu nhiên user

- Chỉnh sửa code để phù hợp cho việc test: với mỗi request đến, userId sẽ là số random ngẫu nhiên trong khoảng (1;1000)

### 3.3 Get history

#### a. Từ 1 user duy nhất

- Chọn 1 userId bất kì để test cho toàn bộ request
- Sử dụng `ghz` gọi hàm GetHistory của gRPC service với số request là 2000.
- Kết quả test lần đầu sẽ là trường hợp transaction history chưa được cache (chỉ với request đầu tiên).
- Kết quả các lân chyạ sau thì transaction history đã được cache trên redis.

#### b. Chọn ngẫu nhiên 1 user trong 1000 users cho mỗi request.

- Thực hiện tương từ như trên nhưng chỉ thay đổi ở việc chọn random userId trong khoảng [1;1000] cho mỗi request đến.

## 4. Kết quả

### 4.1. Transfer money

- **A**: Tất cả users đều có đủ số dư thực hiện giao dịch
- **B**: 500 / 1000 users ngẫu nhiên không đủ số dư thực hiện giao dịch
- **C**: Tất cả users đều không số dư thực hiện giao dịch

| **Loại** 	| **Số request** 	| **RPS (req/s)** 	| **avg.latency (ms)** 	| **.99 latency (ms)** 	| **max.latency (ms)** 	| **Tổng thời gian (ms)** 	|
|--------	|----------------	|-----------------	|----------------------	|----------------------	|----------------------	|-------------------------	|
| A      	| 500            	| 493.97          	| 39.29                	| 68.04                	| 68.04                	| 1010                    	|
| A      	| 1000           	| 524.11          	| 37.44                	| 66.89                	| 83.21                	| 1950                    	|
| B      	| 500            	| 588.14          	| 31.57                	| 61.87                	| 75.05                	| 850.14                  	|
| B      	| 1000           	| 614.72          	| 31.32                	| 62.76                	| 89.28                	| 1630                    	|
| C      	| 500            	| 1131.41         	| 16.61                	| 36.3                 	| 44.89                	| 441.93                  	|
| C      	| 1000           	| 1117.43         	| 16.89                	| 33.56                	| 42.72                	| 894.91                  	|

### 4.2. Get balance 

- Số lượng request: 2000

#### a. Trên 1 user duy nhất

|**RPS(req/s)** | **avg.latency(ms)** | **.99.latency(ms)** | **max.latency** |**Tổng thời gian (ms)** |
|---------------------------|------------------------|----------------|---------------------|---------------------|-----------------|
|  6211.3         | 3                   | 8.2                 | 13.52           |321.99             |

#### b. Chọn ngẫu nhiên trong 1000 user cho mỗi request

|  **RPS(req/s)** | **avg.latency(ms)** | **.99.latency(ms)** | **max.latency** |**Tổg thời gian (ms)** |
|------------------------------|------------------------|----------------|---------------------|---------------------|-----------------|
|  6657.41        | 2.75                | 7.82                | 10.12           |300.42                 |

### 4.3 Get transaction history

#### a. 1 user duy nhất, chưa cache

|  **RPS(req/s)** | **avg.latency(ms)** | **.99.latency(ms)** | **max.latency** |**Tổng thời gian (ms)** |
|-------------------------------------------------|------------------------|----------------|---------------------|---------------------|-----------------|
|  5374.94        | 8.83                | 28.51               | 36.96           |372.1                  |

#### b. 1 user duy nhất, có cache

|  **RPS(req/s)** | **avg.latency(ms)** | **.99.latency(ms)** | **max.latency** |**Tổng thời gian (ms)** | 
|--------------------------------|------------------------|----------------|---------------------|---------------------|-----------------|
|  7509           | 6.21                | 14.67               | 26.99           |266.35                 |

#### c. Chọn ngẫu nhiên trong 1000 users, chưa cache

| **RPS(req/s)** | **avg.latency(ms)** | **.99.latency(ms)** | **max.latency** |**Tổng thời gian (ms)** | 
|-----------------------------------|------------------------|----------------|---------------------|---------------------|-----------------|
| 3193.58        | 14.98               | 42.02               | 46.93           |626.26                 | 

#### d. Chọn ngẫu nhiên trong 1000 users cho mỗi request, cache ngẫu nhiên

| **RPS(req/s)** | **avg.latency(ms)** | **.99.latency(ms)** | **max.latency** |**Tổng thời gian (ms)** | 
|--------------------------------|------------------------|----------------|---------------------|---------------------|-----------------|
|  5789.23        | 8.14                | 22.05               | 36.65           |345.47                 |