#!/bin/bash

set -e
set -x

# Configuration
BRIDGE_NAME=br0
APP_VETH=veth1
APP_VETH_BR=veth1-br
APP_IP=192.168.100.101/24

CLIENT_NS=clientns
CLIENT_VETH=veth-client
CLIENT_VETH_BR=veth-client-br
CLIENT_IP=192.168.100.102/24

BRIDGE_IP=192.168.100.1/24

# Clean up previous setup
ip netns del $CLIENT_NS 2>/dev/null || true
ip link del $APP_VETH 2>/dev/null || true
ip link del $CLIENT_VETH_BR 2>/dev/null || true
ip link del $BRIDGE_NAME 2>/dev/null || true

# Create bridge
ip link add "$BRIDGE_NAME" type bridge
ip addr add "$BRIDGE_IP" dev "$BRIDGE_NAME"
ip link set "$BRIDGE_NAME" up

# Create veth pair for app
ip link add "$APP_VETH" type veth peer name "$APP_VETH_BR"
ip addr add "$APP_IP" dev "$APP_VETH"
ip link set "$APP_VETH" up
ip link set "$APP_VETH_BR" master "$BRIDGE_NAME"
ip link set "$APP_VETH_BR" up

# Create veth pair for client in a namespace
ip netns add $CLIENT_NS
ip link add "$CLIENT_VETH" type veth peer name "$CLIENT_VETH_BR"
ip link set "$CLIENT_VETH" netns "$CLIENT_NS"
ip link set "$CLIENT_VETH_BR" master "$BRIDGE_NAME"
ip link set "$CLIENT_VETH_BR" up

# Configure client namespace interface
ip netns exec "$CLIENT_NS" ip addr add "$CLIENT_IP" dev "$CLIENT_VETH"
ip netns exec "$CLIENT_NS" ip link set "$CLIENT_VETH" up
ip netns exec "$CLIENT_NS" ip link set lo up

# Optional: enable IP forwarding
sysctl -w net.ipv4.ip_forward=1

# Test connectivity
echo "[*] Pinging app interface from client namespace"
ip netns exec "$CLIENT_NS" ping -c 3 192.168.100.101

echo "[✔] Network namespace simulation complete"
