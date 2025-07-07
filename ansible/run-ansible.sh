#!/bin/bash

echo "Starting Ansible..."

# Verifica se a variável de ambiente está definida
if [ -z "$ANSIBLE_PRIVATE_KEY" ]; then
  echo "Erro: a variável de ambiente ANSIBLE_PRIVATE_KEY não está definida."
  echo "Por favor, defina ANSIBLE_PRIVATE_KEY com o caminho para a chave privada."
  exit 1
fi

ANSIBLE_HOST_KEY_CHECKING=false ansible-playbook \
  -i ../terraform/hosts \
  --private-key "$ANSIBLE_PRIVATE_KEY" \
  beerstore-playbook.yml -v
