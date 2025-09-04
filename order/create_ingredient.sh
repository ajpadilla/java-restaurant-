#!/bin/bash

API_URL="http://192.168.100.101:8081/api/v1/ingredients/create"

for i in {1..50}; do
  id=$(uuidgen)   # generate a random UUID
  name="ingredient_$i"
  quantity=$((RANDOM % 100 + 1))

  sudo ip netns exec clientns curl -s -X POST "$API_URL" \
    -H "Content-Type: application/json" \
    -d "{
      \"id\": \"$id\",
      \"name\": \"$name\",
      \"quantity\": $quantity
    }"

  echo "Created ingredient $name"
done
