#!/bin/bash

# Define your desired packages
packages=("nload" "iftop" "unzip", "python3" "python3-pip" "python3-venv")

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


if [ ! `command -v python3 &>/dev/null` ]; then
  echo "📦 Python3 not found. Installing..."
  sudo apt-get install -y python3 python3-pip
else
  echo "✅ Python3 is already installed."
fi

if [ ! python3 -m venv --help &>/dev/null]; then
  echo "📦 python3-venv not found. Installing..."
  sudo apt-get install -y python3-venv
else
    echo "✅ python3-venv is already available."
fi