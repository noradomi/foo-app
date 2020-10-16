### Get balance
```$xslt
Summary:
  Count:        2000
  Total:        606.97 ms
  Slowest:      71.36 ms
  Fastest:      0.94 ms
  Average:      14.02 ms
  Requests/sec: 3295.04

Response time histogram:
  0.942 [1]     |
  7.984 [296]   |∎∎∎∎∎∎∎∎∎∎∎
  15.026 [1042] |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  22.067 [400]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  29.109 [187]  |∎∎∎∎∎∎∎
  36.151 [39]   |∎
  43.193 [25]   |∎
  50.234 [3]    |
  57.276 [0]    |
  64.318 [3]    |
  71.360 [4]    |

Latency distribution:
  10 % in 7.12 ms 
  25 % in 9.31 ms 
  50 % in 11.91 ms 
  75 % in 17.28 ms 
  90 % in 23.78 ms 
  95 % in 27.07 ms 
  99 % in 37.79 ms 

Status code distribution:
  [OK]   2000 responses  
```

### Get History

#### No cache
```$xslt
Summary:
  Count:        2000
  Total:        1.13 s
  Slowest:      130.84 ms
  Fastest:      1.15 ms
  Average:      26.24 ms
  Requests/sec: 1768.92

Response time histogram:
  1.152 [1]     |
  14.121 [556]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  27.089 [665]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  40.058 [423]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  53.027 [181]  |∎∎∎∎∎∎∎∎∎∎∎
  65.996 [89]   |∎∎∎∎∎
  78.965 [47]   |∎∎∎
  91.933 [12]   |∎
  104.902 [11]  |∎
  117.871 [8]   |
  130.840 [7]   |

Latency distribution:
  10 % in 5.82 ms 
  25 % in 12.92 ms 
  50 % in 22.04 ms 
  75 % in 34.60 ms 
  90 % in 50.79 ms 
  95 % in 61.81 ms 
  99 % in 98.33 ms 

Status code distribution:
  [OK]   2000 responses
```

#### Cached
```$xslt
Summary:
  Count:        2000
  Total:        868.87 ms
  Slowest:      96.32 ms
  Fastest:      1.07 ms
  Average:      19.85 ms
  Requests/sec: 2301.85

Response time histogram:
  1.075 [1]     |
  10.600 [526]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  20.125 [669]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  29.650 [414]  |∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  39.175 [195]  |∎∎∎∎∎∎∎∎∎∎∎∎
  48.700 [102]  |∎∎∎∎∎∎
  58.225 [55]   |∎∎∎
  67.750 [20]   |∎
  77.275 [8]    |
  86.800 [9]    |∎
  96.325 [1]    |

Latency distribution:
  10 % in 4.60 ms 
  25 % in 10.03 ms 
  50 % in 17.51 ms 
  75 % in 25.68 ms 
  90 % in 38.73 ms 
  95 % in 47.15 ms 
  99 % in 65.58 ms 

Status code distribution:
  [OK]   2000 responses  
```

