#!/bin/bash

set -e 

error_handler() {
  local exit_code=$?  # Capture the exit code of the last command
  echo -e "\nError: An error occurred during the build process."
  echo "Exit code: $exit_code"
  echo "Last command: $BASH_COMMAND"
  exit 1
}

trap 'error_handler' ERR

cd "messaging-utilities"
mvn clean install
mvn clean package
cd ..

declare -a dirs=(
  "account-management"
  "app-api"
  "customer-facade"
  "merchant-facade"
  "manager-facade"
  "payment-management"
  "tokenservice"
  "reportservice"
)

total=${#dirs[@]}

show_progress() {
  local current=$1
  local total=$2
  local width=50
  local percent=$((current * 100 / total))
  local progress=$((current * width / total))
  
  printf "\r["
  for ((i=0; i<progress; i++)); do printf "#"; done
  for ((i=progress; i<width; i++)); do printf " "; done
  printf "] %d%% (%d/%d)" "$percent" "$current" "$total"
}

current=0
for dir in "${dirs[@]}"; do
  current=$((current + 1))
  
  show_progress "$current" "$total"

  cd "$dir" || exit 1
  mvn clean package || exit 1
  cd .. || exit 1
done

echo
echo "All the services are built successfully"
