#!/bin/bash

echo "Provisioning infrastructure..."

echo "Finding my public IP address..."
MY_PUBLIC_IP="$(curl -s ipinfo.io/ip)"
echo "... your public IP is $MY_PUBLIC_IP"

echo "Starting terraform..."
terraform apply -var "my_public_ip=$MY_PUBLIC_IP/32"