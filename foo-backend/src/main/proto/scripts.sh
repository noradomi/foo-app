#!/bin/bash
# shellcheck disable=SC2016

# Transfer money
./ghz --insecure \
  --proto fintech/fintech.proto \
  --call fintech.FintechService.TransferMoney \
  -d '{"confirm_password": "123", "receiver_id": "8bf394d7-dd8f-4901-9c46-c877324e320f","amount": 1000,"description": "Foo"}' \
  -i . \
  -n 50  \
  -m '{"jwt":
  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiJkYjY0MGIzOC1lNGViLTQ4NjAtYjVmMS03ZTQ0ZTQ0ZTlkYWQiLCJpYXQiOjE2MDMwMzU2MDQsImV4cCI6MTYwMzIxMDIwNH0._GqxIvIgwlqLMfRo_MbQbnrZkCcEWYmDxbIJwoodtYA"}' \
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
