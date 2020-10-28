#!/bin/bash

# Login
# wrk -t 5 -c 20 -d 5s -s login.lua --latency http://localhost:8055/api/public/login

# Register
wrk -t 2 -c 20 -d 2s -s register.lua --latency http://localhost:8055/api/public/signup