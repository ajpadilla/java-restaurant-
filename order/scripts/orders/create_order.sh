#!/bin/bash

# ==============================
# Config
# ==============================
API_URL="http://localhost:8081/api/v1/orders/create"

# Example plate IDs (replace with real ones)
PLATE_IDS=(
  "8062e671-3106-4930-bf03-660829fd78a2"
  "1222a517-df18-4237-ae32-84ad782fe796"
)

# Generate a UUID for the order
ORDER_ID=$(uuidgen)

# ==============================
# Build JSON payload
# ==============================
plate_ids_json=$(printf '"%s",' "${PLATE_IDS[@]}")
plate_ids_json="[${plate_ids_json%,}]"  # Remove trailing comma

payload=$(cat <<EOF
{
  "id": "$ORDER_ID",
  "plateIds": $plate_ids_json
}
EOF
)

echo "➡️ Sending POST request to $API_URL"
echo "Payload:"
echo "$payload" | jq .

# ==============================
# Send request and capture response
# ==============================
response=$(curl -s -w "HTTPSTATUS:%{http_code}" -X POST "$API_URL" \
  -H "Content-Type: application/json" \
  -d "$payload")

# ==============================
# Parse response
# ==============================
body=$(echo "$response" | sed -e 's/HTTPSTATUS\:.*//g')
status=$(echo "$response" | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')

# ==============================
# Output
# ==============================
echo "---------------------------------------------------"
echo "Status Code: $status"
if [ "$status" -eq 201 ]; then
    echo "✅ Order successfully created!"
    echo "Response Body:"
    echo "$body" | jq .
else
    echo "❌ Failed to create order (status: $status)"
    echo "Response Body:"
    echo "$body"
fi
