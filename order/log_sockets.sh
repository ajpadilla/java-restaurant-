#!/bin/bash

echo "Logging TCP socket states on port 8081..."
echo "Timestamp,SS Output" > tcp_log.csv

while true; do
  timestamp=$(date +"%Y-%m-%d %H:%M:%S.%3N")
  ss_output=$(ss -tanp | grep ':8081')
  if [[ ! -z "$ss_output" ]]; then
    while IFS= read -r line; do
      echo "\"$timestamp\",\"$line\"" >> tcp_log.csv
    done <<< "$ss_output"
  fi
  sleep 0.2
done
