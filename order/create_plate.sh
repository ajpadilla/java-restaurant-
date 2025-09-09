#!/bin/bash

API_URL="http://192.168.100.101:8081/api/v1/plates/create"

# Preloaded ingredient IDs in your in-memory DB
INGREDIENTS=("11111111-1111-1111-1111-111111111111" "22222222-2222-2222-2222-222222222222")

for i in {1..5}; do
  plate_id=$(uuidgen)
  plate_name="plate_$i"

  # Build JSON array for ingredients
  ingredients_json=$(printf '"%s",' "${INGREDIENTS[@]}")
  ingredients_json="[${ingredients_json%,}]"

  sudo ip netns exec clientns curl -s -X POST "$API_URL" \
    -H "Content-Type: application/json" \
    -d "{
      \"id\": \"$plate_id\",
      \"name\": \"$plate_name\",
      \"ingredientsIds\": $ingredients_json
    }"

  echo "Created plate $plate_name with ingredients ${INGREDIENTS[*]}"
done
