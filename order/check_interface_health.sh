#!/bin/bash

source ./network_utils.sh

INTERFACE=$(get_active_interface)

#Fetch interface details
OUTPUT=$(ip -s link show "$INTERFACE")

# Extract flags (e.g., <UP,LOWER_UP>)
FLAGS=$(echo "$OUTPUT" | grep -oP '(?<=<)[^>]+')

# Check for UP and LOWER_UP
if [[ "$FLAGS" != *"UP"* || "$FLAGS" != *"LOWER_UP"* ]]; then
  echo "❌ $INTERFACE is DOWN or not physically connected."
  exit 1
fi

# Extract RX and TX error stats
RX_ERRORS=$(echo "$OUTPUT" | awk '/RX:/ {getline; print $3}')
TX_ERRORS=$(echo "$OUTPUT" | awk '/TX:/ {getline; print $3}')

RX_DROPPED=$(echo "$OUTPUT" | awk '/RX:/ {getline; print $4}')
TX_DROPPED=$(echo "$OUTPUT" | awk '/TX:/ {getline; print $4}')

# Threshold for alerting (you can set this to > 0 if needed)
if [[ "$RX_ERRORS" -ne 0 || "$TX_ERRORS" -ne 0 || "$RX_DROPPED" -ne 0 || "$TX_DROPPED" -ne 0 ]]; then
  echo "⚠️ Detected issues on $INTERFACE:"
  echo "   RX Errors: $RX_ERRORS, Dropped: $RX_DROPPED"
  echo "   TX Errors: $TX_ERRORS, Dropped: $TX_DROPPED"
  exit 2
else
  echo "✅ $INTERFACE is healthy. No errors or dropped packets."
fi