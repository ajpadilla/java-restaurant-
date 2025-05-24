#!/bin/bash

echo "[*] Running monitoring script..."
bash monitor/monitor_flow.sh &

sleep 2  # Let tcpdump start first

echo "[*] Sending traffic with wrk..."
python3 client/python/send_wrk.py

echo "[✓] All tasks completed. Check logs/ folder."
