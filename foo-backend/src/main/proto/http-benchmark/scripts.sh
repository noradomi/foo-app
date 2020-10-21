#!/bin/bash

# Login
wrk -t 2 -c 2 -d 5s -s login.lua --latency http://localhost:8055/api/public/login