#!/bin/bash

# File: analyze_requests.sh
# Usage: ./analyze_requests.sh dump.log

# Ensure consistent decimal formatting (dot, not comma)
export LC_NUMERIC=C

DUMP_FILE=$1

if [ -z "$DUMP_FILE" ]; then
  echo "Usage: $0 dump.log"
  exit 1
fi

echo "Analyzing TCP requests on port 8081 from: $DUMP_FILE"
echo ""

# 1. Extract SYN packets (start of each connection)
grep 'Flags \[S\]' "$DUMP_FILE" | while read -r line; do
  # Extract source IP and port, destination IP and port
  CLIENT=$(echo "$line" | awk '{print $4}')
  SERVER=$(echo "$line" | awk '{print $6}' | sed 's/://')

  CLIENT_IP=$(echo "$CLIENT" | cut -d. -f1-4)
  CLIENT_PORT=$(echo "$CLIENT" | cut -d. -f5)
  SERVER_IP=$(echo "$SERVER" | cut -d. -f1-4)
  SERVER_PORT=$(echo "$SERVER" | cut -d. -f5)

  # Filter all packets related to this flow (both directions)
  FLOW=$(grep "$CLIENT_IP.$CLIENT_PORT > $SERVER_IP.$SERVER_PORT" "$DUMP_FILE"; grep "$SERVER_IP.$SERVER_PORT > $CLIENT_IP.$CLIENT_PORT" "$DUMP_FILE")

  # Count packets
  TOTAL_PACKETS=$(echo "$FLOW" | wc -l)

  # Sum payload bytes
  TOTAL_BYTES=$(echo "$FLOW" | grep 'length' | awk '{sum+=$NF} END {print sum+0}')

  # Check for FIN or RST
  CLEAN_CLOSE=$(echo "$FLOW" | grep -q '\[F.\]' && echo "Yes" || echo "No")
  RESET=$(echo "$FLOW" | grep -q '\[R\]' && echo "Yes" || echo "No")

  # Extract handshake timestamps
  T1=$(echo "$FLOW" | grep 'Flags \[S\]' | head -n1 | awk '{print $2}')
  T2=$(echo "$FLOW" | grep 'Flags \[S.\]' | head -n1 | awk '{print $2}')
  T3=$(echo "$FLOW" | grep 'Flags \[\.\]' | grep -v 'Flags \[S.\]' | head -n1 | awk '{print $2}')

  # Convert to float seconds with microsecond precision
  t1_float=$(echo "$T1" | awk -F: '{ split($3,s,"."); printf "%.9f", ($1 * 3600) + ($2 * 60) + s[1] + ("0." s[2]) }')
  t2_float=$(echo "$T2" | awk -F: '{ split($3,s,"."); printf "%.9f", ($1 * 3600) + ($2 * 60) + s[1] + ("0." s[2]) }')
  t3_float=$(echo "$T3" | awk -F: '{ split($3,s,"."); printf "%.9f", ($1 * 3600) + ($2 * 60) + s[1] + ("0." s[2]) }')

  # Calculate RTT and Handshake Time
  RTT=$(awk -v t1="$t1_float" -v t2="$t2_float" 'BEGIN { printf "%.6f", t2 - t1 }')
  HANDSHAKE_TIME=$(awk -v t1="$t1_float" -v t3="$t3_float" 'BEGIN { printf "%.6f", t3 - t1 }')

  # Output results
  echo "Client: $CLIENT_IP:$CLIENT_PORT"
  echo "Server: $SERVER_IP:$SERVER_PORT"
  echo "  ➤ Total packets  : $TOTAL_PACKETS"
  echo "  ➤ Total bytes    : $TOTAL_BYTES"
  echo "  ➤ Closed cleanly : $CLEAN_CLOSE"
  echo "  ➤ Reset detected : $RESET"
  echo "  ➤ RTT            : $RTT seconds"
  echo "  ➤ Handshake time : $HANDSHAKE_TIME seconds"
  echo "  ➤ Packet weights:"

  # Show packet lengths and weight relative to total bytes
  echo "$FLOW" | grep 'length' | while read -r packet; do
    PACKET_LENGTH=$(echo "$packet" | awk '{print $NF}')
    WEIGHT=$(awk -v len="$PACKET_LENGTH" -v total="$TOTAL_BYTES" 'BEGIN {if (total > 0) printf "%.2f", (len/total)*100; else print "0.00"}')
    echo "     - Packet length: $PACKET_LENGTH bytes (${WEIGHT}%)"
  done

  echo "-------------------------------"
done
