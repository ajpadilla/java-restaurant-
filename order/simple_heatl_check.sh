#!/bin/bash

source ./network_utils.sh

URL="http://localhost:8081/api/v1/orders/health"
INTERFACE=$(get_active_interface)

STATUS=$(curl -s -o /dev/null -w "%{http_code}" $URL)
RECEIVED_PKTS=$(cat /sys/class/net/$INTERFACE/statistics/rx_packets)
DROPPED_PKTS=$(cat /sys/class/net/$INTERFACE/statistics/rx_dropped)

if [ "$STATUS" -ne 200 ]; then
  echo "NGINX is DOWN! Status: $STATUS"
else
  echo "NGINX is running fine. Status: $STATUS"
fi

echo "Interface:" $INTERFACE
echo "Received Packets:" $RECEIVED_PKTS
echo "Dropped Packets:" $DROPPED_PKTS