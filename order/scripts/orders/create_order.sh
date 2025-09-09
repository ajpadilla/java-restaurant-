#!/bin/bash

# ==============================
# Config
# ==============================
API_URL="http://192.168.100.101:8081/api/v1/orders/create"

# Example plate IDs (replace with real ones)
PLATE_IDS=(
  "a3f6c75c-269d-4bfb-969c-7fdc0623d0a6"
  "3c028982-50ed-4816-a209-e62721a6cabf"
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
