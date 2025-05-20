#!/bin/bash

set -e  # Exit on error
set -x  # Debug output

# Configurable values
BRIDGE_NAME=br0
VETH_APP=veth1
VETH_BRIDGE=veth1-br
APP_IP=192.168.100.101/24
BRIDGE_IP=192.168.100.1/24

NS_NAME=clientns
VETH_CLIENT=veth-client
VETH_CLIENT_BR=veth-client-br
CLIENT_IP=192.168.100.102/24

echo "[*] Cleaning up existing interfaces and namespaces if they exist..."

# Delete existing bridge
ip link del "$BRIDGE_NAME" &>/dev/null || true

# Delete veth pairs
ip link del "$VETH_APP" &>/dev/null || true
ip link del "$VETH_CLIENT_BR" &>/dev/null || true

# Delete namespace
ip netns del "$NS_NAME" &>/dev/null || true

echo "[+] Creating bridge: $BRIDGE_NAME"
ip link add "$BRIDGE_NAME" type bridge
ip addr add "$BRIDGE_IP" dev "$BRIDGE_NAME"
ip link set "$BRIDGE_NAME" up

echo "[+] Creating veth pair for app: $VETH_APP <-> $VETH_BRIDGE"
ip link add "$VETH_APP" type veth peer name "$VETH_BRIDGE"
ip link set "$VETH_BRIDGE" master "$BRIDGE_NAME"
ip link set "$VETH_APP" up
ip link set "$VETH_BRIDGE" up
ip addr add "$APP_IP" dev "$VETH_APP"

echo "[+] Creating client namespace: $NS_NAME"
ip netns add "$NS_NAME"

echo "[+] Creating veth pair for client namespace: $VETH_CLIENT <-> $VETH_CLIENT_BR"
ip link add "$VETH_CLIENT" type veth peer name "$VETH_CLIENT_BR"

echo "[+] Moving $VETH_CLIENT into namespace $NS_NAME"
ip link set "$VETH_CLIENT" netns "$NS_NAME"

echo "[+] Setting up $VETH_CLIENT_BR on host"
ip addr add "$CLIENT_IP" dev "$VETH_CLIENT_BR"
ip link set "$VETH_CLIENT_BR" up
ip link set "$VETH_CLIENT_BR" master "$BRIDGE_NAME"

echo "[+] Configuring interface inside namespace"
ip netns exec "$NS_NAME" ip addr add "$CLIENT_IP" dev "$VETH_CLIENT"
ip netns exec "$NS_NAME" ip link set "$VETH_CLIENT" up
ip netns exec "$NS_NAME" ip link set lo up

echo "[*] Testing connectivity from namespace to app interface"
ip netns exec "$NS_NAME" ping -c 3 192.168.100.101 || {
  echo "[!] Ping failed. Check setup manually."
  exit 1
}

echo "[✔] Virtual network setup complete!"
