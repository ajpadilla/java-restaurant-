#!/bin/bash

echo "[*] Active Listening Sockets:"
ss -ltpn

echo "[*] SYN-RECV Queue (incomplete handshakes):"
ss -ntp state syn-recv || echo "None detected"

echo "[*] Established Connections:"
ss -ntp state established

echo "[*] Socket Buffers (Recv-Q/Send-Q) from 192.168.100.102:"
ss -i src 192.168.100.102 | grep -v "Recv-Q: 0" || echo "No active buffered connections from this source"

echo "[*] Raw Socket States (searching for port 8081 in hex):"
cat /proc/net/tcp6 | grep -i ':1f91'

