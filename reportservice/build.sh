#!/bin/bash
set -e
mvn clean install
docker-compose build reportservice
