#!/usr/bin/env bash
set -euo pipefail
BASE_URL="${BASE_URL:-http://localhost:8080}"
EMAIL="${SEED_EMAIL:-student@example.com}"
FULL_NAME="${SEED_FULL_NAME:-Demo Student}"
PASSWORD="${SEED_PASSWORD:-ChangeMe123!}"
payload=$(python -c 'import json,os; print(json.dumps({"fullName":os.environ["FULL_NAME"],"email":os.environ["EMAIL"],"password":os.environ["PASSWORD"]}))' FULL_NAME="$FULL_NAME" EMAIL="$EMAIL" PASSWORD="$PASSWORD")
response=$(curl --silent --show-error --fail-with-body -H 'Content-Type: application/json' -d "$payload" "$BASE_URL/api/auth/register" || true)
if [[ -z "$response" ]] || ! grep -q '"token"' <<<"$response"; then
 response=$(curl --silent --show-error --fail-with-body -H 'Content-Type: application/json' -d "$payload" "$BASE_URL/api/auth/login")
fi
token=$(python -c 'import json,sys; print(json.load(sys.stdin)["token"])' <<<"$response")
echo "Authenticated seed user $EMAIL. Token omitted."
curl --silent --show-error --fail-with-body -H "Authorization: Bearer $token" "$BASE_URL/api/auth/me"
echo
