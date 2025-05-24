#!/bin/bash

echo "[*] Running monitoring script..."
bash /home/orders/java-restaurant/order/network/monitor/monitor_flow.sh &

sleep 2  # Let tcpdump start first

echo "[*] Sending traffic with wrk..."
python3 python3 /home/orders/java-restaurant/order/network/client/python/send_wrk.py

echo "[✓] All tasks completed. Check logs/ folder."
