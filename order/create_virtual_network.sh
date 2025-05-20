#!/bin/bash

set -e
set -x

BRIDGE_NAME=br0
BRIDGE_IP=192.168.100.1/24

# APP namespace setup
APP_NS=appns
VETH_APP=veth-app
VETH_APP_BR=veth-app-br
APP_IP=192.168.100.101/24

# CLIENT namespace setup
CLIENT_NS=clientns
VETH_CLIENT=veth-client
VETH_CLIENT_BR=veth-client-br
CLIENT_IP=192.168.100.102/24

echo "[*] Cleaning up..."
ip netns del $APP_NS 2>/dev/null || true
ip netns del $CLIENT_NS 2>/dev/null || true
ip link del $VETH_APP_BR 2>/dev/null || true
ip link del $VETH_CLIENT_BR 2>/dev/null || true
ip link del $BRIDGE_NAME 2>/dev/null || true

echo "[+] Creating bridge..."
ip link add $BRIDGE_NAME type bridge
ip addr add $BRIDGE_IP dev $BRIDGE_NAME
ip link set $BRIDGE_NAME up

# ---- APP Side ----
echo "[+] Setting up app namespace..."
ip netns add $APP_NS
ip link add $VETH_APP type veth peer name $VETH_APP_BR
ip link set $VETH_APP netns $APP_NS
ip link set $VETH_APP_BR master $BRIDGE_NAME
ip link set $VETH_APP_BR up
ip netns exec $APP_NS ip addr add $APP_IP dev $VETH_APP
ip netns exec $APP_NS ip link set $VETH_APP up
ip netns exec $APP_NS ip link set lo up

# ---- CLIENT Side ----
echo "[+] Setting up client namespace..."
ip netns add $CLIENT_NS
ip link add $VETH_CLIENT type veth peer name $VETH_CLIENT_BR
ip link set $VETH_CLIENT netns $CLIENT_NS
ip link set $VETH_CLIENT_BR master $BRIDGE_NAME
ip link set $VETH_CLIENT_BR up
ip netns exec $CLIENT_NS ip addr add $CLIENT_IP dev $VETH_CLIENT
ip netns exec $CLIENT_NS ip link set $VETH_CLIENT up
ip netns exec $CLIENT_NS ip link set lo up

echo "[✔] Network is ready!"
