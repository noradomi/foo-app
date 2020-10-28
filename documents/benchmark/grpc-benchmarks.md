# GRPC Benchmark

## 1. Transfer money (Random ngẫu nhiên sender và receiver trong 1000 user)

### a. Các giao dịch đều đủ số dư
- 500 requests 

```
Summary:
  Count:        500
  Total:        1.01 s
  Slowest:      79.58 ms
  Fastest:      7.84 ms
  Average:      39.29 ms
  Requests/sec: 493.97

Response time histogram:
  7.836 [1]     |
  15.010 [6]    |∎
  22.184 [5]    |∎
  29.358 [43]   |∎∎∎∎∎∎∎∎∎∎
  36.532 [149]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  43.706 [165]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  50.880 [82]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  58.054 [26]   |∎∎∎∎∎∎
  65.228 [15]   |∎∎∎∎
  72.402 [7]    |∎∎
  79.577 [1]    |

Latency distribution:
  10 % in 28.77 ms 
  25 % in 33.45 ms 
  50 % in 38.71 ms 
  75 % in 43.91 ms 
  90 % in 50.46 ms 
  95 % in 57.04 ms 
  99 % in 68.04 ms 

Status code distribution:
  [OK]   500 responses
```
- 1000 requests

```
Summary:
  Count:        1000
  Total:        1.95 s
  Slowest:      83.21 ms
  Fastest:      9.83 ms
  Average:      37.44 ms
  Requests/sec: 514.11

Response time histogram:
  9.831 [1]     |
  17.170 [11]   |∎
  24.508 [25]   |∎∎∎
  31.846 [228]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  39.184 [366]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  46.522 [233]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  53.860 [87]   |∎∎∎∎∎∎∎∎∎∎
  61.198 [27]   |∎∎∎
  68.537 [16]   |∎∎
  75.875 [3]    |
  83.213 [3]    |

Latency distribution:
  10 % in 27.77 ms 
  25 % in 31.46 ms 
  50 % in 36.11 ms 
  75 % in 41.93 ms 
  90 % in 48.84 ms 
  95 % in 53.74 ms 
  99 % in 66.89 ms 

Status code distribution:
  [OK]   1000 responses
```

### b. 500 users ngẫu nhiên 0 đ / 1000 users

- 500 request
  
```
Summary:
  Count:        500
  Total:        850.14 ms
  Slowest:      75.05 ms
  Fastest:      5.94 ms
  Average:      31.57 ms
  Requests/sec: 588.14

Response time histogram:
  5.941 [1]     |
  12.852 [14]   |∎∎∎∎∎
  19.763 [71]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  26.673 [101]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  33.584 [101]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  40.495 [104]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  47.406 [56]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  54.317 [37]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  61.227 [9]    |∎∎∎
  68.138 [3]    |∎
  75.049 [3]    |∎

Latency distribution:
  10 % in 17.46 ms 
  25 % in 22.11 ms 
  50 % in 30.98 ms 
  75 % in 38.91 ms 
  90 % in 47.51 ms 
  95 % in 51.98 ms 
  99 % in 61.87 ms 

Status code distribution:
  [OK]   500 responses
```
- 1000 request

```
Summary:
  Count:        1000
  Total:        1.63 s
  Slowest:      89.28 ms
  Fastest:      5.45 ms
  Average:      31.32 ms
  Requests/sec: 614.72

Response time histogram:
  5.447 [1]     |
  13.830 [43]   |∎∎∎∎∎∎∎
  22.214 [201]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  30.597 [262]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  38.981 [242]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  47.364 [163]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  55.748 [55]   |∎∎∎∎∎∎∎∎
  64.131 [24]   |∎∎∎∎
  72.514 [5]    |∎
  80.898 [3]    |
  89.281 [1]    |

Latency distribution:
  10 % in 16.78 ms 
  25 % in 22.29 ms 
  50 % in 30.29 ms 
  75 % in 39.01 ms 
  90 % in 46.25 ms 
  95 % in 52.16 ms 
  99 % in 62.76 ms 

Status code distribution:
  [OK]   1000 responses
```

### c. 1000 users đều có balance = 0

- 500 request

```
Summary:
  Count:        500
  Total:        441.93 ms
  Slowest:      44.89 ms
  Fastest:      4.98 ms
  Average:      16.61 ms
  Requests/sec: 1131.41

Response time histogram:
  4.975 [1]     |
  8.967 [28]    |∎∎∎∎∎∎∎
  12.959 [120]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  16.951 [154]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  20.943 [99]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  24.935 [48]   |∎∎∎∎∎∎∎∎∎∎∎∎
  28.927 [24]   |∎∎∎∎∎∎
  32.918 [12]   |∎∎∎
  36.910 [12]   |∎∎∎
  40.902 [1]    |
  44.894 [1]    |

Latency distribution:
  10 % in 9.93 ms 
  25 % in 12.19 ms 
  50 % in 15.30 ms 
  75 % in 19.39 ms 
  90 % in 24.63 ms 
  95 % in 29.21 ms 
  99 % in 36.30 ms 

Status code distribution:
  [OK]   500 responses
```

- 1000 request

```
Summary:
  Count:        1000
  Total:        894.91 ms
  Slowest:      42.72 ms
  Fastest:      4.22 ms
  Average:      16.89 ms
  Requests/sec: 1117.43

Response time histogram:
  4.220 [1]     |
  8.070 [36]    |∎∎∎∎∎
  11.919 [164]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  15.769 [298]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  19.618 [225]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  23.468 [124]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  27.318 [74]   |∎∎∎∎∎∎∎∎∎∎
  31.167 [57]   |∎∎∎∎∎∎∎∎
  35.017 [13]   |∎∎
  38.866 [6]    |∎
  42.716 [2]    |

Latency distribution:
  10 % in 9.98 ms 
  25 % in 12.58 ms 
  50 % in 15.79 ms 
  75 % in 20.23 ms 
  90 % in 25.50 ms 
  95 % in 29.00 ms 
  99 % in 33.56 ms 

Status code distribution:
  [OK]   1000 responses
```

## 2. Get balance

- Của 1 user duy nhất

```
Summary:
  Count:        2000
  Total:        321.99 ms
  Slowest:      13.52 ms
  Fastest:      0.63 ms
  Average:      3.00 ms
  Requests/sec: 6211.30

Response time histogram:
  0.627 [1]     |
  1.917 [262]   |∎∎∎∎∎∎∎∎∎
  3.207 [1126]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  4.496 [460]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  5.786 [78]    |∎∎∎
  7.076 [22]    |∎
  8.366 [34]    |∎
  9.655 [6]     |
  10.945 [1]    |
  12.235 [2]    |
  13.525 [8]    |

Latency distribution:
  10 % in 1.81 ms 
  25 % in 2.25 ms 
  50 % in 2.73 ms 
  75 % in 3.39 ms 
  90 % in 4.20 ms 
  95 % in 5.02 ms 
  99 % in 8.20 ms 

Status code distribution:
  [OK]   2000 responses
```

- Chọn ngẫu nhiên user

```
Summary:
  Count:        2000
  Total:        300.42 ms
  Slowest:      10.12 ms
  Fastest:      0.66 ms
  Average:      2.75 ms
  Requests/sec: 6657.41

Response time histogram:
  0.659 [1]     |
  1.605 [145]   |∎∎∎∎∎∎∎
  2.552 [891]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  3.498 [669]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  4.444 [157]   |∎∎∎∎∎∎∎
  5.390 [51]    |∎∎
  6.336 [38]    |∎∎
  7.282 [13]    |∎
  8.228 [22]    |∎
  9.174 [7]     |
  10.120 [6]    |

Latency distribution:
  10 % in 1.72 ms 
  25 % in 2.08 ms 
  50 % in 2.52 ms 
  75 % in 3.05 ms 
  90 % in 3.87 ms 
  95 % in 4.95 ms 
  99 % in 7.82 ms   

Status code distribution:
  [OK]   2000 responses
```

## 3. Get history 

- Của 1 user duy nhất (chưa cache 20 request đầu)
  
```
Summary:
  Count:        2000
  Total:        372.10 ms
  Slowest:      36.96 ms
  Fastest:      0.61 ms
  Average:      8.83 ms
  Requests/sec: 5374.94

Response time histogram:
  0.610 [1]     |
  4.245 [297]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  7.879 [867]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  11.514 [378]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  15.148 [203]  |∎∎∎∎∎∎∎∎∎
  18.783 [100]  |∎∎∎∎∎
  22.417 [87]   |∎∎∎∎
  26.052 [37]   |∎∎
  29.686 [13]   |∎
  33.321 [9]    |
  36.955 [8]    |

Latency distribution:
  10 % in 3.38 ms 
  25 % in 5.31 ms 
  50 % in 7.18 ms 
  75 % in 10.85 ms 
  90 % in 16.97 ms 
  95 % in 20.56 ms 
  99 % in 28.51 ms 

Status code distribution:
  [OK]   2000 responses 
```

- Của 1 user duy nhất (Cached)

```
Summary:
  Count:        2000
  Total:        266.35 ms
  Slowest:      26.99 ms
  Fastest:      0.57 ms
  Average:      6.21 ms
  Requests/sec: 7509.00

Response time histogram:
  0.571 [1]     |
  3.213 [208]   |∎∎∎∎∎∎∎∎∎∎
  5.855 [794]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  8.497 [669]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  11.139 [237]  |∎∎∎∎∎∎∎∎∎∎∎∎
  13.782 [65]   |∎∎∎
  16.424 [15]   |∎
  19.066 [7]    |
  21.708 [2]    |
  24.350 [0]    |
  26.993 [2]    |

Latency distribution:
  10 % in 3.16 ms 
  25 % in 4.42 ms 
  50 % in 5.84 ms 
  75 % in 7.61 ms 
  90 % in 9.56 ms 
  95 % in 10.98 ms 
  99 % in 14.67 ms 

Status code distribution:
  [OK]   2000 responses 
```

- Chọn user ngẫu nhiên (Chưa cached)

```
Summary:
  Count:        2000
  Total:        626.26 ms
  Slowest:      46.93 ms
  Fastest:      0.70 ms
  Average:      14.98 ms
  Requests/sec: 3193.58

Response time histogram:
  0.701 [1]     |
  5.323 [204]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  9.946 [531]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  14.568 [399]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  19.191 [318]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  23.813 [222]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  28.436 [137]  |∎∎∎∎∎∎∎∎∎∎
  33.058 [65]   |∎∎∎∎∎
  37.681 [70]   |∎∎∎∎∎
  42.303 [33]   |∎∎
  46.926 [20]   |∎∎

Latency distribution:
  10 % in 5.23 ms 
  25 % in 8.27 ms 
  50 % in 12.75 ms 
  75 % in 19.87 ms 
  90 % in 27.25 ms 
  95 % in 34.55 ms 
  99 % in 42.20 ms 

Status code distribution:
  [OK]   2000 responses
```

- Chọn user ngẫu nhiên (Cached ngẫu nhiên)

```
Summary:
  Count:        2000
  Total:        345.47 ms
  Slowest:      36.65 ms
  Fastest:      0.59 ms
  Average:      8.14 ms
  Requests/sec: 5789.23

Response time histogram:
  0.586 [1]     |
  4.193 [330]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  7.800 [756]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  11.406 [537]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  15.013 [241]  |∎∎∎∎∎∎∎∎∎∎∎∎∎
  18.620 [79]   |∎∎∎∎
  22.227 [36]   |∎∎
  25.834 [11]   |∎
  29.441 [3]    |
  33.047 [5]    |
  36.654 [1]    |

Latency distribution:
  10 % in 3.35 ms 
  25 % in 5.21 ms 
  50 % in 7.37 ms 
  75 % in 10.27 ms 
  90 % in 13.69 ms 
  95 % in 16.19 ms 
  99 % in 22.05 ms 

Status code distribution:
  [OK]   2000 responses
```