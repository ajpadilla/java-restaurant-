#!/bin/bash

URL="http://localhost:8081/api/v1/orders/health"
STATUS=$(curl -s -o /dev/null -w "%{http_code}" $URL)

if [ "$STATUS" -ne 200 ]; then
  echo "NGINX is DOWN! Status: $STATUS"
else
  echo "NGINX is running fine. Status: $STATUS"
fi
