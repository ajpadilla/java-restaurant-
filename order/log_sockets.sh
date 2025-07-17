#!/bin/bash

echo "Logging TCP socket states on port 8081..."
echo "Timestamp,State,Recv-Q,Send-Q,Local,Remote,Process" > tcp_log.csv

while true; do
  timestamp=$(date +"%Y-%m-%d %H:%M:%S.%3N")
  ss_output=$(ss -tanp | grep ':8081')
  if [[ ! -z "$ss_output" ]]; then
    while IFS= read -r line; do
      # Use awk to parse relevant fields:
      # Format example:
      # ESTAB  66 0  [::ffff:192.168.100.101]:8081  [::ffff:192.168.100.102]:39572 users:(("java",pid=2307,fd=24))
      state=$(echo "$line" | awk '{print $1}')
      recvq=$(echo "$line" | awk '{print $2}')
      sendq=$(echo "$line" | awk '{print $3}')
      local_addr=$(echo "$line" | awk '{print $4}')
      remote_addr=$(echo "$line" | awk '{print $5}')
      process=$(echo "$line" | grep -oP 'users:\(\K.*\)$' | tr -d ')')

      echo "\"$timestamp\",\"$state\",\"$recvq\",\"$sendq\",\"$local_addr\",\"$remote_addr\",\"$process\"" >> tcp_log.csv
    done <<< "$ss_output"
  fi
  sleep 0.2
done
