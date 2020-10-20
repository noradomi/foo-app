#!/bin/bash
# shellcheck disable=SC2016

JWT="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJjZDM5MGI1NC0yZGFmLTRkZjItYWU2Ny01ZWI0ZDAxNWUzZmIiLCJpYXQiOjE2MDMxODYwNDEsImV4cCI6MTYwMzM2MDY0MX0.Xgq2Mx7gMc2rXrzWjtRI_WxkwzETklfmhWck9Yd3VoI"

# Transfer money
./ghz --insecure \
  --proto fintech/fintech.proto \
  --call fintech.FintechService.TransferMoney \
  -d '{"confirm_password": "123", "receiver_id": "7249fa8a-95f4-441d-bca6-dc07c6c9f036","amount": 1000,"description": "Foo"}' \
  -i . \
  -n 500 \
  -m '{"jwt":"'"$JWT"'"}' \
  0.0.0.0:5001

# Get balance
#./ghz --insecure \
#  --proto fintech/fintech.proto \
#  --call fintech.FintechService.GetBalance \
#  -i . \
#  -n 2000 \
#  -m '{"jwt":"'"$JWT"'"}' \
#  0.0.0.0:5001

# Get transaction history
#./ghz --insecure \
#  --proto fintech/fintech.proto \
#  --call fintech.FintechService.GetHistory \
#  -d '{"page_size": 20, "page_token": 0}' \
#  -i . \
#  -n 2000 \
#  -m '{"jwt":"'"$JWT"'"}' \
#  0.0.0.0:5001

# Reset unseen messages
#./ghz --insecure \
#  --proto fintech/chat.proto \
#  --call fintech.ChatService.ResetUnseen \
#  -d '{"user_id": "9fa39906-124f-40e7-a2aa-650c833ed122"}' \
#  -i . \
#  -n 2000 \
#  -m '{"jwt":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJiY2JiYTJjMy1hZTZmLTQ4MTEtYmNlOC1mOTE3NDcyMWM2NGYiLCJpYXQiOjE2MDI4NjY3MTAsImV4cCI6MTYwMzA0MTMxMH0.ukVcPw7HUS6znZBMUu12qKLjgmrKoRzgjYN4uX3bI0c"}' \
#  0.0.0.0:5001

# Get friend list
