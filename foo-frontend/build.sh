#!/bin/bash

export WORKING_DIR=$(pwd)

# Start backend
cd ./src/protos
protoc -I=. fintech.proto \
  --js_out=import_style=commonjs:$WORKING_DIR//src/grpc \
  --grpc-web_out=import_style=commonjs,mode=grpcwebtext:$WORKING_DIR/src/grpc