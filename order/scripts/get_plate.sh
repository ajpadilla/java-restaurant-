#!/bin/bash

# ==============================
# Config
# ==============================
API_URL="http://localhost:8081/api/v1/plates"
PLATE_ID="868b5da4-b6c4-403d-bfb2-d6eba2e9e080"

# ==============================
# Call API
# ==============================
echo "➡️  Sending request to: $API_URL/$PLATE_ID"
echo "---------------------------------------------------"

response=$(curl -s -w "\nHTTP_STATUS:%{http_code}" -X GET "$API_URL/$PLATE_ID" \
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
echo "Response Body:"
echo "$body" | jq .
