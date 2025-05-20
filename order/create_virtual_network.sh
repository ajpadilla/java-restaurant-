#!/bin/bash

set -e
set -x

# Server (App) configuration
BRIDGE_NAME=br0
VETH_APP=veth1
VETH_BRIDGE=veth1-br
BRIDGE_IP=192.168.100.1/24
APP_IP=192.168.100.101/24

# Client configuration
NS_NAME=clientns
VETH_CLIENT=veth-client
VETH_CLIENT_BR=veth-client-br
CLIENT_IP=192.168.100.102/24

# 1. Create the bridge if it doesn't exist
if ! ip link show "$BRIDGE_NAME" &>/dev/null; then
    ip link add "$BRIDGE_NAME" type bridge
    ip addr add "$BRIDGE_IP" dev "$BRIDGE_NAME"
    ip link set "$BRIDGE_NAME" up
fi

# 2. Delete existing server veth pair
ip link del "$VETH_APP" &>/dev/null || true

# 3. Create server veth pair and connect
ip link add "$VETH_APP" type veth peer name "$VETH_BRIDGE"
ip link set "$VETH_BRIDGE" master "$BRIDGE_NAME"
ip link set "$VETH_APP" up
ip link set "$VETH_BRIDGE" up
ip addr add "$APP_IP" dev "$VETH_APP"

# 4. Delete old namespace and veth if exist
ip netns del "$NS_NAME" &>/dev/null || true
ip link del "$VETH_CLIENT_BR" &>/dev/null || true

# 5. Create client namespace
ip netns add "$NS_NAME"

# 6. Create veth pair for client
ip link add "$VETH_CLIENT" type veth peer name "$VETH_CLIENT_BR"

# 7. Move one end into the namespace
ip link set "$VETH_CLIENT" netns "$NS_NAME"

# 8. Configure bridge side of client
ip addr add "$CLIENT_IP" dev "$VETH_CLIENT_BR"
ip link set "$VETH_CLIENT_BR" up
ip link set "$VETH_CLIENT_BR" master "$BRIDGE_NAME"

# 9. Configure client namespace side
ip netns exec "$NS_NAME" ip addr add "$CLIENT_IP" dev "$VETH_CLIENT"
ip netns exec "$NS_NAME" ip link set "$VETH_CLIENT" up
ip netns exec "$NS_NAME" ip link set lo up

# 10. Ping test from client
echo "[*] Testing connectivity from client namespace"
ip netns exec "$NS_NAME" ping -c 3 192.168.100.101

# 11. Optional: generate load
echo "[*] Starting load test from client..."
ip netns exec "$NS_NAME" wrk -t2 -c20 -d10s http://192.168.100.101:8081/api/v1/orders/health

echo "[✔] Network setup with test traffic complete!"
