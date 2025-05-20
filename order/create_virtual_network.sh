#!/bin/bash

set -e
set -x

BRIDGE_NAME=br0
VETH_APP=veth1
VETH_BRIDGE=veth1-br
BRIDGE_IP=192.168.100.1/24
APP_IP=192.168.100.101/24

# Create bridge if it doesn't exist
if ! ip link show "$BRIDGE_NAME" &>/dev/null; then
    ip link add "$BRIDGE_NAME" type bridge
    ip addr add "$BRIDGE_IP" dev "$BRIDGE_NAME"
    ip link set "$BRIDGE_NAME" up
fi

# Delete old veth pair if exists
ip link del "$VETH_APP" &>/dev/null || true

# Create veth pair
ip link add "$VETH_APP" type veth peer name "$VETH_BRIDGE"

# Attach one end to the bridge
ip link set "$VETH_BRIDGE" master "$BRIDGE_NAME"
ip link set "$VETH_BRIDGE" up

# Bring up the app side
ip link set "$VETH_APP" up
ip addr add "$APP_IP" dev "$VETH_APP"

# Enable IP forwarding so packets can flow through VM if needed
sysctl -w net.ipv4.ip_forward=1

# Optional: add NAT rules if you want to route traffic between VM and host

echo "[*] Setup complete. Bridge $BRIDGE_NAME with $APP_IP on $VETH_APP created."

