#!/bin/bash

# Universal script to publish any library to Sonatype Central
# Usage: ./publish-library.sh <library-name>
# Example: ./publish-library.sh resilient-core

set -e

if [ -z "$1" ]; then
    echo "Usage: $0 <library-name>"
    echo "Example: $0 resilient-core"
    exit 1
fi

LIBRARY_NAME="$1"
BASE_DIR="$(cd "$(dirname "$0")" && pwd)"
LIB_DIR="$BASE_DIR/$LIBRARY_NAME"

if [ ! -d "$LIB_DIR" ]; then
    echo "Error: Library directory not found: $LIB_DIR"
    exit 1
fi

cd "$LIB_DIR"

# Get artifact info from POM
ARTIFACT_ID=$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout 2>/dev/null || echo "$LIBRARY_NAME")
VERSION=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout 2>/dev/null || echo "0.1.0")
GROUP_ID=$(mvn help:evaluate -Dexpression=project.groupId -q -DforceStdout 2>/dev/null || echo "io.github.upendra-manike")
GPG_KEY="537E05AE209ED7AE8790C4A4240C903B13D9C122"
BASE_URL="https://central.sonatype.com/api/v1/publisher/upload"
AUTH_TOKEN="WldKaVNmOlhlZVJFMHZObUwxNTRpOXBLNFQ2ZVQyM2FaSjNWWkFJSQ=="

echo "=========================================="
echo "Publishing: $ARTIFACT_ID v$VERSION"
echo "=========================================="
echo ""

# Step 1: Build and package
echo "Step 1: Building project..."
export MAVEN_GPG_PASSPHRASE='Blue-Running-2026!-stars'
mvn clean install -DskipTests > /dev/null 2>&1 || mvn clean package -DskipTests > /dev/null 2>&1
echo "✓ Build complete"
echo ""

# Step 2: Generate checksums
echo "Step 2: Generating checksums..."
cd target

generate_checksums() {
    local file=$1
    if [ -f "$file" ]; then
        md5sum "$file" | cut -d' ' -f1 > "${file}.md5" 2>/dev/null || md5 -q "$file" > "${file}.md5"
        sha1sum "$file" | cut -d' ' -f1 > "${file}.sha1" 2>/dev/null || shasum -a 1 "$file" | cut -d' ' -f1 > "${file}.sha1"
    fi
}

FILES=(
    "${ARTIFACT_ID}-${VERSION}.pom"
    "${ARTIFACT_ID}-${VERSION}.jar"
    "${ARTIFACT_ID}-${VERSION}-sources.jar"
    "${ARTIFACT_ID}-${VERSION}-javadoc.jar"
)

for file in "${FILES[@]}"; do
    if [ -f "$file" ]; then
        generate_checksums "$file"
    fi
done
echo "✓ Checksums generated"
echo ""

# Step 3: Sign files
echo "Step 3: Signing files..."
sign_file() {
    local file=$1
    if [ -f "$file" ] && [ ! -f "${file}.asc" ]; then
        gpg --batch --pinentry-mode loopback --passphrase "$MAVEN_GPG_PASSPHRASE" \
            --default-key "$GPG_KEY" \
            --armor --detach-sign "$file" 2>/dev/null || true
    fi
}

for file in "${FILES[@]}"; do
    if [ -f "$file" ]; then
        sign_file "$file"
    fi
done
echo "✓ Files signed"
echo ""

# Step 4: Create bundle
echo "Step 4: Creating Maven bundle..."
BUNDLE_NAME="${ARTIFACT_ID}-${VERSION}-bundle.zip"
BUNDLE_DIR="bundle-temp"
rm -rf "$BUNDLE_DIR"
mkdir -p "$BUNDLE_DIR"

GROUP_PATH=$(echo "$GROUP_ID" | tr '.' '/')
mkdir -p "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION"

# Copy all files
for file in "${FILES[@]}"; do
    if [ -f "$file" ]; then
        cp "$file" "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION/" 2>/dev/null || true
        cp "${file}.asc" "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION/" 2>/dev/null || true
        cp "${file}.md5" "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION/" 2>/dev/null || true
        cp "${file}.sha1" "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION/" 2>/dev/null || true
    fi
done

cd "$BUNDLE_DIR"
zip -r "../${BUNDLE_NAME}" . > /dev/null
cd ..
rm -rf "$BUNDLE_DIR"
echo "✓ Bundle created: $BUNDLE_NAME"
echo ""

# Step 5: Upload
echo "Step 5: Uploading to Sonatype Central..."
RESPONSE=$(curl -s -w "\n%{http_code}" -X 'POST' \
  "${BASE_URL}?publishingType=USER_MANAGED" \
  -H 'accept: text/plain' \
  -H "Authorization: Basic ${AUTH_TOKEN}" \
  -H 'Content-Type: multipart/form-data' \
  -F "bundle=@${BUNDLE_NAME};type=application/zip")

HTTP_CODE=$(echo "$RESPONSE" | tail -n 1)
BODY=$(echo "$RESPONSE" | sed '$d')

if [ "$HTTP_CODE" = "200" ] || [ "$HTTP_CODE" = "201" ] || [ "$HTTP_CODE" = "202" ]; then
    echo "✓ Upload successful! (HTTP $HTTP_CODE)"
    echo "  Deployment ID: $BODY"
    echo ""
    echo "Check status at: https://central.sonatype.com/publishing/deployments"
else
    echo "✗ Upload failed (HTTP $HTTP_CODE)"
    echo "$BODY"
    exit 1
fi

echo ""
echo "=========================================="
echo "✓ $ARTIFACT_ID v$VERSION published!"
echo "=========================================="


