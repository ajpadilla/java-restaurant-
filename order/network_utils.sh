#!/bin/bash

get_active_interface() {
  ip route get 8.8.8.8 | awk '{print $5; exit}'
}

