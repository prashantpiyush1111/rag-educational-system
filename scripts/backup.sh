#!/bin/bash
set -e

TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="./backups/$TIMESTAMP"

mkdir -p "$BACKUP_DIR"

echo "Starting backup at $TIMESTAMP..."

if [ -d "python-ai-service/data/chroma_db" ]; then
  echo "Backing up ChromaDB..."
  cp -r python-ai-service/data/chroma_db "$BACKUP_DIR/chroma_db"
fi

if command -v pg_dump &> /dev/null; then
  echo "Backing up PostgreSQL..."
  pg_dump -U "${DB_USER:-postgres}" -h "${DB_HOST:-localhost}" "${DB_NAME:-rag_db}" > "$BACKUP_DIR/postgres_dump.sql"
fi

echo "Backup completed: $BACKUP_DIR"