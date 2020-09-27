#!/bin/bash

export WORKING_DIR=$(pwd)

# Start backend
cd ./protos
protoc -I=. greet.proto \
  --js_out=import_style=commonjs:$WORKING_DIR/grpc \
  --grpc-web_out=import_style=commonjs,mode=grpcwebtext:$WORKING_DIR/grpc
