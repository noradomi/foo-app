
### Transfer money (Company)
```
Summary:
  Count:        2000
  Total:        50.43 s
  Slowest:      1.47 s
  Fastest:      169.23 ms
  Average:      1.25 s
  Requests/sec: 39.66

Response time histogram:
  169.232 [1]   |
  299.330 [6]   |
  429.428 [5]   |
  559.526 [5]   |
  689.625 [5]   |
  819.723 [5]   |
  949.821 [5]   |
  1079.919 [5]  |
  1210.018 [255]        |∎∎∎∎∎∎
  1340.116 [1604]       |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  1470.214 [104]        |∎∎∎

Latency distribution:
  10 % in 1.20 s 
  25 % in 1.23 s 
  50 % in 1.25 s 
  75 % in 1.28 s 
  90 % in 1.32 s 
  95 % in 1.34 s 
  99 % in 1.38 s 

Status code distribution:
  [OK]   2000 responses  
```

### Transfer money (Laptop)
```
Summary:
  Count:        2000
  Total:        43.60 s
  Slowest:      2.05 s
  Fastest:      25.58 ms
  Average:      1.08 s
  Requests/sec: 45.88

Response time histogram:
  25.583 [1]    |
  227.559 [11]  |∎
  429.535 [13]  |∎
  631.511 [13]  |∎
  833.487 [372] |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  1035.463 [673]        |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  1237.439 [517]        |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  1439.415 [172]        |∎∎∎∎∎∎∎∎∎∎
  1641.391 [91] |∎∎∎∎∎
  1843.367 [103]        |∎∎∎∎∎∎
  2045.343 [34] |∎∎

Latency distribution:
  10 % in 775.08 ms 
  25 % in 881.81 ms 
  50 % in 1.01 s 
  75 % in 1.17 s 
  90 % in 1.52 s 
  95 % in 1.74 s 
  99 % in 1.94 s 

Status code distribution:
  [OK]   2000 responses 
```

```
Summary:
  Count:        500
  Total:        6.66 s
  Slowest:      1.18 s
  Fastest:      458.90 ms
  Average:      643.62 ms
  Requests/sec: 75.06

Response time histogram:
  458.898 [1]   |
  530.978 [61]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  603.058 [157] |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  675.139 [163] |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  747.219 [37]  |∎∎∎∎∎∎∎∎∎
  819.299 [26]  |∎∎∎∎∎∎
  891.380 [31]  |∎∎∎∎∎∎∎∎
  963.460 [5]   |∎
  1035.540 [5]  |∎
  1107.621 [7]  |∎∎
  1179.701 [7]  |∎∎

Latency distribution:
  10 % in 508.60 ms 
  25 % in 557.95 ms 
  50 % in 627.34 ms 
  75 % in 669.48 ms 
  90 % in 828.39 ms 
  95 % in 880.80 ms 
  99 % in 1.13 s 

Status code distribution:
  [OK]   500 responses  
```

```
Summary:
  Count:        500
  Total:        3.13 s
  Slowest:      483.30 ms
  Fastest:      74.26 ms
  Average:      299.91 ms
  Requests/sec: 159.50

Response time histogram:
  74.259 [1]    |
  115.163 [6]   |∎
  156.066 [5]   |∎
  196.970 [7]   |∎
  237.874 [6]   |∎
  278.777 [241] |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  319.681 [72]  |∎∎∎∎∎∎∎∎∎∎∎∎
  360.585 [70]  |∎∎∎∎∎∎∎∎∎∎∎∎
  401.488 [42]  |∎∎∎∎∎∎∎
  442.392 [20]  |∎∎∎
  483.296 [30]  |∎∎∎∎∎

Latency distribution:
  10 % in 246.61 ms 
  25 % in 257.98 ms 
  50 % in 273.90 ms 
  75 % in 345.74 ms 
  90 % in 400.78 ms 
  95 % in 449.66 ms 
  99 % in 478.09 ms 

Status code distribution:
  [OK]   500 responses 
```
### Get balance
```$xslt
Summary:
  Count:        2000
  Total:        355.14 ms
  Slowest:      34.83 ms
  Fastest:      0.69 ms
  Average:      8.26 ms
  Requests/sec: 5631.61

Response time histogram:
  0.686 [1]     |
  4.101 [202]   |∎∎∎∎∎∎∎∎∎
  7.515 [873]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  10.930 [520]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  14.344 [218]  |∎∎∎∎∎∎∎∎∎∎
  17.758 [94]   |∎∎∎∎
  21.173 [50]   |∎∎
  24.587 [22]   |∎
  28.001 [10]   |
  31.416 [8]    |
  34.830 [2]    |

Latency distribution:
  10 % in 4.07 ms 
  25 % in 5.33 ms 
  50 % in 7.17 ms 
  75 % in 9.95 ms 
  90 % in 14.04 ms 
  95 % in 17.42 ms 
  99 % in 24.32 ms 

Status code distribution:
  [OK]   2000 responses 
```

### Get History

#### No cache for first 20 requests
```$xslt
Summary:
  Count:        2000
  Total:        445.51 ms
  Slowest:      45.75 ms
  Fastest:      0.91 ms
  Average:      10.53 ms
  Requests/sec: 4489.22

Response time histogram:
  0.913 [1]     |
  5.397 [255]   |∎∎∎∎∎∎∎∎∎∎∎∎
  9.881 [826]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  14.365 [594]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  18.849 [199]  |∎∎∎∎∎∎∎∎∎∎
  23.333 [58]   |∎∎∎
  27.817 [15]   |∎
  32.301 [21]   |∎
  36.785 [23]   |∎
  41.269 [4]    |
  45.753 [4]    |

Latency distribution:
  10 % in 5.00 ms 
  25 % in 6.94 ms 
  50 % in 9.49 ms 
  75 % in 12.55 ms 
  90 % in 16.47 ms 
  95 % in 20.46 ms 
  99 % in 33.68 ms 

Status code distribution:
  [OK]   2000 responses  
```

#### Cached
```
Summary:
  Count:        2000
  Total:        330.19 ms
  Slowest:      25.58 ms
  Fastest:      0.82 ms
  Average:      7.82 ms
  Requests/sec: 6057.12

Response time histogram:
  0.823 [1]     |
  3.299 [122]   |∎∎∎∎∎∎
  5.774 [360]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  8.249 [805]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  10.724 [407]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  13.200 [136]  |∎∎∎∎∎∎∎
  15.675 [99]   |∎∎∎∎∎
  18.150 [54]   |∎∎∎
  20.625 [12]   |∎
  23.101 [2]    |
  25.576 [2]    |

Latency distribution:
  10 % in 4.01 ms 
  25 % in 5.84 ms 
  50 % in 7.20 ms 
  75 % in 9.46 ms 
  90 % in 12.24 ms 
  95 % in 14.61 ms 
  99 % in 17.68 ms 

Status code distribution:
  [OK]   2000 responses
```

### Reset unseen messages

```
Summary:
  Count:        2000
  Total:        493.43 ms
  Slowest:      34.34 ms
  Fastest:      1.07 ms
  Average:      11.79 ms
  Requests/sec: 4053.27

Response time histogram:
  1.067 [1]     |
  4.394 [51]    |∎∎∎
  7.722 [260]   |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  11.049 [696]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  14.376 [521]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  17.704 [230]  |∎∎∎∎∎∎∎∎∎∎∎∎∎
  21.031 [155]  |∎∎∎∎∎∎∎∎∎
  24.358 [68]   |∎∎∎∎
  27.686 [13]   |∎
  31.013 [4]    |
  34.340 [1]    |

Latency distribution:
  10 % in 6.97 ms 
  25 % in 8.67 ms 
  50 % in 11.00 ms 
  75 % in 14.17 ms 
  90 % in 18.77 ms 
  95 % in 20.79 ms 
  99 % in 23.91 ms 

Status code distribution:
  [OK]   2000 responses  
```
