#!/bin/bash

# ==============================
# Config
# ==============================
API_URL="http://localhost:8081/api/v1/orders/index"
PAGE=0
SIZE=3

# ==============================
# Call API
# ==============================
echo "➡️  Sending request to: $API_URL?page=$PAGE&size=$SIZE"
echo "---------------------------------------------------"

response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X GET "$API_URL?page=$PAGE&size=$SIZE" \
  -H "Content-Type: application/json")

# ==============================
# Parse response
# ==============================
body=$(echo "$response" | sed -e 's/HTTP_STATUS\:.*//g')
status=$(echo "$response" | tr -d '\n' | sed -e 's/.*HTTP_STATUS://')

# ==============================
# Output
# ==============================
echo "Status Code: $status"
if [ "$status" -eq 200 ]; then
    echo "Response Body (pretty-printed):"
    echo "$body" | jq .
else
    echo "❌ Failed to fetch plates list (status: $status)"
    echo "Response Body:"
    echo "$body"
fi
