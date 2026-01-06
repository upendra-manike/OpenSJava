#!/bin/bash

# Upload the Maven bundle to Sonatype Central
# This uses the bundle ZIP file with proper Maven repository layout

set -e

ARTIFACT_ID="safe-config"
VERSION="0.1.0"
BUNDLE_NAME="${ARTIFACT_ID}-${VERSION}-bundle.zip"
BASE_URL="https://central.sonatype.com/api/v1/publisher/upload"
AUTH_TOKEN="WldKaVNmOlhlZVJFMHZObUwxNTRpOXBLNFQ2ZVQyM2FaSjNWWkFJSQ=="

cd "$(dirname "$0")"
cd target

if [ ! -f "$BUNDLE_NAME" ]; then
    echo "Bundle not found. Creating it..."
    cd ..
    ./create-bundle.sh
    cd target
fi

echo "Uploading bundle ${BUNDLE_NAME} to Sonatype Central..."
echo ""

RESPONSE=$(curl -s -w "\n%{http_code}" -X 'POST' \
  "${BASE_URL}?publishingType=USER_MANAGED" \
  -H 'accept: text/plain' \
  -H "Authorization: Basic ${AUTH_TOKEN}" \
  -H 'Content-Type: multipart/form-data' \
  -F "bundle=@${BUNDLE_NAME};type=application/zip")

HTTP_CODE=$(echo "$RESPONSE" | tail -n 1)
BODY=$(echo "$RESPONSE" | sed '$d')

echo "Response (HTTP $HTTP_CODE):"
echo "$BODY"
echo ""

if [ "$HTTP_CODE" = "200" ] || [ "$HTTP_CODE" = "201" ] || [ "$HTTP_CODE" = "202" ]; then
    echo "✓ Upload successful!"
    echo ""
    echo "Check deployment status at:"
    echo "https://central.sonatype.com/publishing/deployments"
else
    echo "✗ Upload failed (HTTP $HTTP_CODE)"
    echo "Check the response above for details"
    exit 1
fi

