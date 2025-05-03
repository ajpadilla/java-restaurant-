#!/bin/bash

# Ensure logs directory exists
mkdir -p logs

# Clear or create orders.log
> logs/orders.log

# Start Order service
echo "Starting Order service..."
nohup java -jar target/order-0.0.1-SNAPSHOT.jar > logs/orders.log 2>&1 &


