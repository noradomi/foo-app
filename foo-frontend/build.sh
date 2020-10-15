#!/bin/bash

export WORKING_DIR=$(pwd)

# Start backend
cd ./src/protos/fintech
protoc --proto_path=$WORKING_DIR/src/protos  -I=. fintech.proto \
  --js_out=import_style=commonjs:$WORKING_DIR//src/grpc \
  --grpc-web_out=import_style=commonjs,mode=grpcwebtext:$WORKING_DIR/src/grpc