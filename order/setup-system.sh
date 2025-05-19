#!/bin/bash
set -e
export DEBIAN_FRONTEND=noninteractive

echo "🔄 Enabling required APT repositories and updating package index..."

# Enable universe repo (only needs to be done once)
sudo add-apt-repository -y universe

# Update APT index
sudo apt-get update -y

echo "✅ System setup complete."
