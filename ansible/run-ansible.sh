#!/bin/bash

echo "Starting Ansible..."

ANSIBLE_HOST_KEY_CHECKING=false ansible-playbook -i ../terraform/hosts --private-key /home/felipe/Documentos/aws/keys/beerstore_key beerstore-playbook.yml -v