#!/bin/bash
API_URL="http://192.168.100.101:8081/api/v1/plates/create"

payload='{
  "id": "'"$(uuidgen)"'",
  "name": "No Ingredients",
  "ingredients": []
}'

# Capture both status and response body
response=$(curl -s -w "HTTPSTATUS:%{http_code}" -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d "$payload")

# Extract body and status
body=$(echo "$response" | sed -e 's/HTTPSTATUS\:.*//g')
status=$(echo "$response" | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')

# Check status and print message
if [ "$status" -eq 400 ]; then
  error=$(echo "$body" | jq -r '.error')
  message=$(echo "$body" | jq -r '.message')
  echo "✅ Correctly rejected. Error: $error | Message: $message"
else
  echo "❌ Unexpected status $status"
  echo "Response body: $body"
  exit 1
fi
