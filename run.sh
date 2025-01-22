docker compose up -d rabbitMq
sleep 20
docker compose up app-api customer-facade manager-facade merchant-facade account-management payment-management token-service repostservice