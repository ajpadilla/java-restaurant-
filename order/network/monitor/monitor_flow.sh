#!/bin/bash
set -e

LOG_DIR="../logs"
mkdir -p "$LOG_DIR"

echo "[*] Starting tcpdump..."
sudo tcpdump -i br0 port 8081 -w "$LOG_DIR/dump.pcap" &
TCPDUMP_PID=$!

echo "[*] Capturing iftop output for 10 seconds..."
sudo timeout 10s iftop -i br0 -n -t > "$LOG_DIR/iftop_log.txt"

echo "[*] Launching nload for 10 seconds..."
sudo timeout 10s nload veth1-br

echo "[*] Stopping tcpdump..."
sudo kill $TCPDUMP_PID

echo "[✓] Monitoring completed. Logs saved in $LOG_DIR"
