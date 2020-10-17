#!/bin/bash
# shellcheck disable=SC2016

# Transfer money
./ghz --insecure \
  --proto fintech/fintech.proto \
  --call fintech.FintechService.TransferMoney \
  -d '{"confirm_password": "123", "receiver_id": "eb26abc2-4a06-401f-84e2-c52dff1b4688","amount": 1000,"description": "Foo"}' \
  -i . \
  -n 50  \
  -m '{"jwt":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJjNzQ2Mjg4Zi1iY2Q3LTRkMGUtOGUyMS0yYWU3MDFjM2IzODMiLCJpYXQiOjE2MDI5MTc3NjEsImV4cCI6MTYwMzA5MjM2MX0.7NafxldOtHPZy2_M1uYCTOcJP5kaN8VjE7Ny8pQW6fo"}' \
  0.0.0.0:5001


# Get balance
#./ghz --insecure \
#  --proto fintech/fintech.proto \
#  --call fintech.FintechService.GetBalance \
#  -i . \
#  -n 2000 \
#  -m '{"jwt":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI2ZjQ4OTcxZC1jMzk5LTQ5ODItODQyMC1iNWE1YzQ0MGVlMWMiLCJpYXQiOjE2MDI4NjA4NTYsImV4cCI6MTYwMzAzNTQ1Nn0.U0cDXjwxODPJF5URaLdnMJeziybx-f72sVvBw0hgLOI"}' \
#  0.0.0.0:5001

# Get transaction history
#./ghz --insecure \
#  --proto fintech/fintech.proto \
#  --call fintech.FintechService.GetHistory \
#  -d '{"page_size": 20, "page_token": 0}' \
#  -i . \
#  -n 2000 \
#  -m '{"jwt":"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiI2ZjQ4OTcxZC1jMzk5LTQ5ODItODQyMC1iNWE1YzQ0MGVlMWMiLCJpYXQiOjE2MDI4NjA4NTYsImV4cCI6MTYwMzAzNTQ1Nn0.U0cDXjwxODPJF5URaLdnMJeziybx-f72sVvBw0hgLOI"}' \
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
