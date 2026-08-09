#!/bin/bash
set -e

API_URL="${API_URL:-http://127.0.0.1:8000}"
CONCURRENT_REQUESTS="${1:-10}"
TOTAL_REQUESTS="${2:-50}"

echo "Running load test against $API_URL/query/"
echo "Concurrent: $CONCURRENT_REQUESTS | Total: $TOTAL_REQUESTS"

if ! command -v hey &> /dev/null; then
  echo "This script requires 'hey' (https://github.com/rakyll/hey)."
  echo "Install it and re-run this script."
  exit 1
fi

hey -n "$TOTAL_REQUESTS" -c "$CONCURRENT_REQUESTS" -m POST \
  -H "Content-Type: application/json" \
  -d '{"question": "What topics are covered in this document?", "top_k": 4}' \
  "$API_URL/query/"

echo "Load test complete."