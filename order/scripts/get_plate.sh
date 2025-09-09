#!/bin/bash

# ==============================
# Config
# ==============================
API_URL="http://192.168.100.101:8081/api/v1/plates"
PLATE_ID="589e9977-454a-4900-80f4-28687de88906"

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
