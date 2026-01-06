#!/bin/bash

# Upload POM first, then JAR to Central
# This ensures Central can associate the POM with the JAR bundle

set -e

ARTIFACT_ID="safe-config"
VERSION="0.1.0"
BASE_URL="https://central.sonatype.com/api/v1/publisher/upload"
AUTH_TOKEN="WldKaVNmOlhlZVJFMHZObUwxNTRpOXBLNFQ2ZVQyM2FaSjNWWkFJSQ=="

cd "$(dirname "$0")"
cd target

echo "Uploading ${ARTIFACT_ID}-${VERSION} to Sonatype Central..."
echo ""

# CRITICAL: Upload POM FIRST
echo "Step 1: Uploading POM file (required first)..."
POM_RESPONSE=$(curl -s -w "\n%{http_code}" -X 'POST' \
  "${BASE_URL}?publishingType=USER_MANAGED" \
  -H 'accept: text/plain' \
  -H "Authorization: Basic ${AUTH_TOKEN}" \
  -H 'Content-Type: multipart/form-data' \
  -F "bundle=@${ARTIFACT_ID}-${VERSION}.pom;type=application/xml")

HTTP_CODE=$(echo "$POM_RESPONSE" | tail -1)
POM_BODY=$(echo "$POM_RESPONSE" | head -n -1)

if [ "$HTTP_CODE" = "200" ] || [ "$HTTP_CODE" = "201" ]; then
    echo "✓ POM uploaded successfully (HTTP $HTTP_CODE)"
    echo "$POM_BODY"
else
    echo "✗ POM upload failed (HTTP $HTTP_CODE)"
    echo "$POM_BODY"
    exit 1
fi

echo ""
echo "Step 2: Uploading JAR file..."
JAR_RESPONSE=$(curl -s -w "\n%{http_code}" -X 'POST' \
  "${BASE_URL}?publishingType=USER_MANAGED" \
  -H 'accept: text/plain' \
  -H "Authorization: Basic ${AUTH_TOKEN}" \
  -H 'Content-Type: multipart/form-data' \
  -F "bundle=@${ARTIFACT_ID}-${VERSION}.jar;type=application/java-archive")

HTTP_CODE=$(echo "$JAR_RESPONSE" | tail -1)
JAR_BODY=$(echo "$JAR_RESPONSE" | head -n -1)

if [ "$HTTP_CODE" = "200" ] || [ "$HTTP_CODE" = "201" ]; then
    echo "✓ JAR uploaded successfully (HTTP $HTTP_CODE)"
    echo "$JAR_BODY"
else
    echo "✗ JAR upload failed (HTTP $HTTP_CODE)"
    echo "$JAR_BODY"
    exit 1
fi

echo ""
echo "Step 3: Uploading sources JAR..."
curl -s -X 'POST' \
  "${BASE_URL}?publishingType=USER_MANAGED" \
  -H 'accept: text/plain' \
  -H "Authorization: Basic ${AUTH_TOKEN}" \
  -H 'Content-Type: multipart/form-data' \
  -F "bundle=@${ARTIFACT_ID}-${VERSION}-sources.jar;type=application/java-archive" \
  && echo "✓ Sources uploaded"

echo ""
echo "Step 4: Uploading javadoc JAR..."
curl -s -X 'POST' \
  "${BASE_URL}?publishingType=USER_MANAGED" \
  -H 'accept: text/plain' \
  -H "Authorization: Basic ${AUTH_TOKEN}" \
  -H 'Content-Type: multipart/form-data' \
  -F "bundle=@${ARTIFACT_ID}-${VERSION}-javadoc.jar;type=application/java-archive" \
  && echo "✓ Javadoc uploaded"

echo ""
echo "=========================================="
echo "Upload completed! Check Central dashboard:"
echo "https://central.sonatype.com/publishing/deployments"
echo "=========================================="

