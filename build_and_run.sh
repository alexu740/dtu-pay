set -e

./build.sh

docker compose down
docker image prune --force
docker compose build

docker compose up -d rabbitMq
sleep 20

docker compose up -d app-api customer-facade merchant-facade manager-facade account-management payment-management token-service repostservice