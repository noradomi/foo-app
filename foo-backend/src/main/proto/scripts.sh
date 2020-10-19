#!/bin/bash
# shellcheck disable=SC2016

JWT="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI2MmE4N2JlYi0yZDY0LTQxMmItOWI0MC1iZDFlZDc2M2ZhY2QiLCJpYXQiOjE2MDMxMDI0MjgsImV4cCI6MTYwMzI3NzAyOH0.Qj0889qC1KAPRSzCAn25blU4c5Vlv8p3LRg9WSsB0nY"

# Transfer money
#./ghz --insecure \
#  --proto fintech/fintech.proto \
#  --call fintech.FintechService.TransferMoney \
#  -d '{"confirm_password": "123", "receiver_id": "3baabc50-b58c-4ff6-8635-392676888e93","amount": 1000,"description": "Foo"}' \
#  -i . \
#  -n 2000 \
#  -m '{"jwt":"'"$JWT"'"}' \
#  0.0.0.0:5001

# Get balance
./ghz --insecure \
  --proto fintech/fintech.proto \
  --call fintech.FintechService.GetBalance \
  -i . \
  -n 2000 \
  -m '{"jwt":"'"$JWT"'"}' \
  0.0.0.0:5001

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
