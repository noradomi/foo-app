#!/bin/bash

# Transfer money
./ghz --insecure \
  --proto fintech/fintech.proto \
  --call fintech.FintechService.TransferMoney \
  -d '{"confirm_password": "123", "receiver_id": "db03f19f-143f-49bc-9820-7685a4681575","amount": 1000,"description": "Foo"}' \
  -i . \
  -n 100   \
  -m '{"jwt":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJiZGIzOWYxMy00ZTYxLTQ0N2UtYjVhNS00YjE3NjA5ZmY2MjQiLCJpYXQiOjE2MDI4NDM4MjEsImV4cCI6MTYwMzAxODQyMX0.XZU_Koi9nkgA59c7wq2fOlvBrpIsJsIWAZb5C_a7Bl4"}' \
  0.0.0.0:5001

# Get balance
#./ghz --insecure \
#  --proto fintech/fintech.proto \
#  --call fintech.FintechService.GetBalance \
#  -i . \
#  -n 2000 \
#  -m '{"jwt":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJiZGIzOWYxMy00ZTYxLTQ0N2UtYjVhNS00YjE3NjA5ZmY2MjQiLCJpYXQiOjE2MDI4NDIxMjQsImV4cCI6MTYwMzAxNjcyNH0.NbKt1EiZCqlebfhOhQIRhgwPR0_MtLWESszlpFsxyOU"}' \
#  0.0.0.0:5001

# Get transaction history
#./ghz --insecure \
#  --proto fintech/fintech.proto \
#  --call fintech.FintechService.GetHistory \
#  -d '{"page_size": 20, "page_token": 0}' \
#  -i . \
#  -n 2000 \
#  -m '{"jwt":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI3YTU1NDI1Ni05ZGFlLTRjOTktYTZjZS0yNTAyYzk3ZTNkYjgiLCJpYXQiOjE2MDI4Mjk5NzgsImV4cCI6MTYwMzAwNDU3OH0.bChWSdecmNM7EHTIGe5hmZAzf4Sme1elLX85ZVLVn88"}' \
#  0.0.0.0:5001


