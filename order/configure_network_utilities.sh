#!/bin/bash

# Define your desired packages
packages=("nload" "iftop" "unzip")

# Install available packages from APT
for pkg in "${packages[@]}"; do
  if ! dpkg -l | grep -qw "$pkg"; then
    if apt-cache show "$pkg" &>/dev/null; then
      echo "📦 Installing $pkg..."
      sudo apt-get install -y "$pkg"
    else
      echo "❌ Package $pkg not found in APT."
    fi
  else
    echo "✅ $pkg is already installed."
  fi
done

# Special handling for 'wrk' (must be built from source)
if ! command -v wrk &>/dev/null; then
  echo "📦 'wrk' not found. Installing from source..."

  sudo apt-get install -y build-essential libssl-dev git unzip

  if [ -d /tmp/wrk ]; then
    echo "🧹 Cleaning up existing /tmp/wrk..."
    rm -rf /tmp/wrk
  fi

  git clone https://github.com/wg/wrk.git /tmp/wrk || { echo "❌ Failed to clone wrk."; exit 1; }
  cd /tmp/wrk || { echo "❌ Failed to cd into /tmp/wrk."; exit 1; }

  make || { echo "❌ Build failed."; exit 1; }

  sudo cp wrk /usr/local/bin || { echo "❌ Failed to copy binary."; exit 1; }

  echo "✅ 'wrk' installed successfully."
else
  echo "✅ 'wrk' is already installed."
fi
