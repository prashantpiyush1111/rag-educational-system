#!/bin/bash
set -e

ENV="${1:-dev}"

echo "Deploying RAG Educational System (env: $ENV)..."

if [ "$ENV" == "prod" ]; then
  COMPOSE_FILE="infrastructure/docker-compose.prod.yml"
else
  COMPOSE_FILE="infrastructure/docker-compose.dev.yml"
fi

echo "Pulling latest changes..."
git pull origin main

echo "Building images..."
docker compose -f "$COMPOSE_FILE" build

echo "Starting services..."
docker compose -f "$COMPOSE_FILE" up -d

echo "Deployment complete. Checking service health..."
sleep 5
docker compose -f "$COMPOSE_FILE" ps