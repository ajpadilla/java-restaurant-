#!/bin/bash

echo "[*] Active Listening Sockets:"
ss -ltpn

echo "[*] SYN-RECV Queue:"
ss -ntp state syn-recv

echo "[*] Established Connections:"
ss -ntp state established

echo "[*] Socket Buffers (Recv-Q/Send-Q):"
ss -i src 192.168.100.102

echo "[*] Raw Socket States:"
cat /proc/net/tcp | grep 0501  # 8081 in hex
