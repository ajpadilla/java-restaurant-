#!/bin/bash

API_URL="http://localhost:8081/api/v1/plates/create"

# Preloaded ingredients with full info
# Format: "id|name|requiredQuantity"
INGREDIENTS=(
  "11111111-1111-1111-1111-111111111111|Tomato|2"
  "22222222-2222-2222-2222-222222222222|Cheese|1"
)

for i in {1..5}; do
  plate_id=$(uuidgen)
  plate_name="plate_$i"

  # Build JSON array of objects
  ingredients_json="["
  for ingredient in "${INGREDIENTS[@]}"; do
    IFS="|" read -r id name qty <<< "$ingredient"
    ingredients_json+="{\"ingredientId\":\"$id\",\"ingredientName\":\"$name\",\"requiredQuantity\":$qty},"
  done
  ingredients_json="${ingredients_json%,}]"  # Remove trailing comma and close array

 # Send request and capture response + status
   response=$(curl -s -w "HTTPSTATUS:%{http_code}" -X POST "$API_URL" \
     -H "Content-Type: application/json" \
     -d "{
       \"id\": \"$plate_id\",
       \"name\": \"$plate_name\",
       \"ingredients\": $ingredients_json
     }")

  # Split response
  body=$(echo "$response" | sed -e 's/HTTPSTATUS\:.*//g')
  status=$(echo "$response" | tr -d '\n' | sed -e 's/.*HTTPSTATUS://')

  echo "---------------------------------------------------"
  echo "Plate: $plate_name"
  echo "Ingredients: $ingredients_json"
  echo "Status Code: $status"

  if [ "$status" -eq 201 ]; then
    echo "✅ Successfully created!"
    echo "Response: $body" | jq .
  else
    echo "❌ Failed to create plate (status: $status)"
    echo "Response: $body"
  fi
done
