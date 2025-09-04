#!/bin/bash

# Ensure logs directory exists
mkdir -p logs

# Clear or create orders.log
> logs/orders.log

# Start Order service with perf-map-agent and diagnostic options
echo "Starting Order service with perf-map-agent..."
nohup java \
  -agentpath:/home/alvaro/perf-map-agent/perf-map-agent/out/libperfmap.so \
  -XX:+UnlockDiagnosticVMOptions \
  -XX:+DebugNonSafepoints \
  -jar target/order-0.0.1-SNAPSHOT.jar > logs/orders.log 2>&1 &
