#!/bin/bash
set -e

ENV="${1:-dev}"

echo "Rolling back RAG Educational System (env: $ENV)..."

if [ "$ENV" == "prod" ]; then
  COMPOSE_FILE="infrastructure/docker-compose.prod.yml"
else
  COMPOSE_FILE="infrastructure/docker-compose.dev.yml"
fi

echo "Stopping current services..."
docker compose -f "$COMPOSE_FILE" down

echo "Checking out previous commit..."
git checkout HEAD~1

echo "Rebuilding and restarting..."
docker compose -f "$COMPOSE_FILE" build
docker compose -f "$COMPOSE_FILE" up -d

echo "Rollback complete."