#!/bin/bash

set -e  # Exit on error
set -x  # Print each command (for debugging)

# Configurable values
BRIDGE_NAME=br0
VETH_APP=veth1
VETH_BRIDGE=veth1-br
BRIDGE_IP=192.168.100.1/24
APP_IP=192.168.100.101/24

# 1. Create the bridge if it doesn't exist
if ! ip link show "$BRIDGE_NAME" &>/dev/null; then
    echo "[+] Creating bridge: $BRIDGE_NAME"
    ip link add "$BRIDGE_NAME" type bridge
    ip addr add "$BRIDGE_IP" dev "$BRIDGE_NAME"
    ip link set "$BRIDGE_NAME" up
else
    echo "[*] Bridge $BRIDGE_NAME already exists"
fi

# 2. Delete existing veth pair if it exists
ip link del "$VETH_APP" &>/dev/null || true

# 3. Create veth pair
ip link add "$VETH_APP" type veth peer name "$VETH_BRIDGE"

# 4. Attach one end to the bridge
ip link set "$VETH_BRIDGE" master "$BRIDGE_NAME"

# 5. Bring both ends up
ip link set "$VETH_APP" up
ip link set "$VETH_BRIDGE" up

# 6. Assign IP to microservice end
ip addr add "$APP_IP" dev "$VETH_APP"

# 7. Optional: ping test
echo "[*] Testing connectivity"
ping -c 2 192.168.100.1

echo "[✔] Virtual network setup complete!"
