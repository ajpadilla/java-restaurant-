#!/bin/bash

API_URL="http://192.168.100.101:8081/api/v1/plates/create"

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
  # Remove trailing comma and close array
  ingredients_json="${ingredients_json%,}]"

  sudo ip netns exec clientns curl -s -X POST "$API_URL" \
    -H "Content-Type: application/json" \
    -d "{
      \"id\": \"$plate_id\",
      \"name\": \"$plate_name\",
      \"ingredients\": $ingredients_json
    }"

  echo "Created plate $plate_name with ingredients: $ingredients_json"
done
