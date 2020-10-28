random = math.random( 1, 1000)

wrk.method = "POST"
wrk.body = '{"username": "user"..random, "password": "123", "name": "test"}'
wrk.headers["Content-Type"] = "application/json"
  