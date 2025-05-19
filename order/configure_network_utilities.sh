#!/bin/bash

# Define your desired packages
packages=("nload" "iftop")

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

  sudo apt-get install -y build-essential libssl-dev git

  git clone https://github.com/wg/wrk.git /tmp/wrk
  cd /tmp/wrk
  make

  sudo cp wrk /usr/local/bin
  echo "✅ 'wrk' installed successfully."
else
  echo "✅ 'wrk' is already installed."
fi

