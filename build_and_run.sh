set -e

./build.sh

docker compose down
docker image prune --force

docker compose up -d rabbitMq
sleep 20
docker compose up app-api customer-facade merchant-facade account-management payment-management token-service repostservice
