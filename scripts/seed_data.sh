#!/bin/bash
set -e

API_URL="${API_URL:-http://127.0.0.1:8000}"
SEED_DIR="./scripts/seed_files"

if [ ! -d "$SEED_DIR" ]; then
  echo "Seed directory not found: $SEED_DIR"
  echo "Add sample .pdf/.docx/.txt files there before running this script."
  exit 1
fi

echo "Seeding documents from $SEED_DIR to $API_URL..."

for file in "$SEED_DIR"/*; do
  if [ -f "$file" ]; then
    echo "Uploading: $file"
    curl -s -X POST "$API_URL/documents/upload" \
      -F "file=@$file" | echo
    echo ""
  fi
done

echo "Seeding complete."