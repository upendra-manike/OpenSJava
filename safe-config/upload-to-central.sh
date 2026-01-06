#!/bin/bash

# Script to upload safe-config to Sonatype Central
# Usage: ./upload-to-central.sh

set -e

ARTIFACT_ID="safe-config"
VERSION="0.1.0"
BASE_URL="https://central.sonatype.com/api/v1/publisher/upload"
AUTH_TOKEN="WldKaVNmOlhlZVJFMHZObUwxNTRpOXBLNFQ2ZVQyM2FaSjNWWkFJSQ=="

cd "$(dirname "$0")"
cd target

echo "Uploading ${ARTIFACT_ID}-${VERSION} to Sonatype Central..."
echo ""

# Upload POM first (required)
echo "1. Uploading POM file..."
curl -X 'POST' \
  "${BASE_URL}?publishingType=USER_MANAGED" \
  -H 'accept: text/plain' \
  -H "Authorization: Basic ${AUTH_TOKEN}" \
  -H 'Content-Type: multipart/form-data' \
  -F "bundle=@${ARTIFACT_ID}-${VERSION}.pom;type=application/xml" \
  -v 2>&1 | grep -E "(< HTTP|upload|success|error)" || echo "POM upload completed"

echo ""
echo "2. Uploading JAR file..."
curl -X 'POST' \
  "${BASE_URL}?publishingType=USER_MANAGED" \
  -H 'accept: text/plain' \
  -H "Authorization: Basic ${AUTH_TOKEN}" \
  -H 'Content-Type: multipart/form-data' \
  -F "bundle=@${ARTIFACT_ID}-${VERSION}.jar;type=application/java-archive" \
  -v 2>&1 | grep -E "(< HTTP|upload|success|error)" || echo "JAR upload completed"

echo ""
echo "3. Uploading sources JAR..."
curl -X 'POST' \
  "${BASE_URL}?publishingType=USER_MANAGED" \
  -H 'accept: text/plain' \
  -H "Authorization: Basic ${AUTH_TOKEN}" \
  -H 'Content-Type: multipart/form-data' \
  -F "bundle=@${ARTIFACT_ID}-${VERSION}-sources.jar;type=application/java-archive" \
  -v 2>&1 | grep -E "(< HTTP|upload|success|error)" || echo "Sources upload completed"

echo ""
echo "4. Uploading javadoc JAR..."
curl -X 'POST' \
  "${BASE_URL}?publishingType=USER_MANAGED" \
  -H 'accept: text/plain' \
  -H "Authorization: Basic ${AUTH_TOKEN}" \
  -H 'Content-Type: multipart/form-data' \
  -F "bundle=@${ARTIFACT_ID}-${VERSION}-javadoc.jar;type=application/java-archive" \
  -v 2>&1 | grep -E "(< HTTP|upload|success|error)" || echo "Javadoc upload completed"

echo ""
echo "Upload process completed. Check Central dashboard for status."


