#!/bin/bash

# Create a Maven repository layout bundle for Central upload
# This creates a ZIP file with the proper Maven directory structure

set -e

ARTIFACT_ID="safe-config"
VERSION="0.1.0"
GROUP_ID="io.github.upendra-manike"
BUNDLE_NAME="${ARTIFACT_ID}-${VERSION}-bundle.zip"

cd "$(dirname "$0")"

# Generate POM file if it doesn't exist
if [ ! -f "target/${ARTIFACT_ID}-${VERSION}.pom" ]; then
    echo "Generating POM file..."
    export MAVEN_GPG_PASSPHRASE='Blue-Running-2026!-stars'
    mvn clean install -DskipTests > /dev/null 2>&1 || mvn clean package -DskipTests > /dev/null 2>&1
fi

cd target

echo "Creating Maven bundle for ${ARTIFACT_ID}-${VERSION}..."

# Create Maven repository layout structure
BUNDLE_DIR="bundle-temp"
rm -rf "$BUNDLE_DIR"
mkdir -p "$BUNDLE_DIR"

# Convert groupId to directory path (io.github.upendra-manike -> io/github/upendra-manike)
GROUP_PATH=$(echo "$GROUP_ID" | tr '.' '/')
mkdir -p "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION"

# Copy all required files
echo "Copying files..."
cp "${ARTIFACT_ID}-${VERSION}.pom" "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION/"
cp "${ARTIFACT_ID}-${VERSION}.jar" "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION/"
cp "${ARTIFACT_ID}-${VERSION}-sources.jar" "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION/" 2>/dev/null || echo "Sources JAR not found, skipping"
cp "${ARTIFACT_ID}-${VERSION}-javadoc.jar" "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION/" 2>/dev/null || echo "Javadoc JAR not found, skipping"

# Copy signatures if they exist
cp "${ARTIFACT_ID}-${VERSION}.pom.asc" "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION/" 2>/dev/null || echo "POM signature not found, skipping"
cp "${ARTIFACT_ID}-${VERSION}.jar.asc" "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION/" 2>/dev/null || echo "JAR signature not found, skipping"
cp "${ARTIFACT_ID}-${VERSION}-sources.jar.asc" "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION/" 2>/dev/null || echo "Sources signature not found, skipping"
cp "${ARTIFACT_ID}-${VERSION}-javadoc.jar.asc" "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION/" 2>/dev/null || echo "Javadoc signature not found, skipping"

# Copy checksums
echo "Copying checksums..."
for file in "${ARTIFACT_ID}-${VERSION}.pom" "${ARTIFACT_ID}-${VERSION}.jar" "${ARTIFACT_ID}-${VERSION}-sources.jar" "${ARTIFACT_ID}-${VERSION}-javadoc.jar"; do
    if [ -f "$file.md5" ]; then
        cp "$file.md5" "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION/" 2>/dev/null || true
    fi
    if [ -f "$file.sha1" ]; then
        cp "$file.sha1" "$BUNDLE_DIR/$GROUP_PATH/$ARTIFACT_ID/$VERSION/" 2>/dev/null || true
    fi
done

# Create ZIP bundle
echo "Creating ZIP bundle..."
cd "$BUNDLE_DIR"
zip -r "../${BUNDLE_NAME}" . > /dev/null
cd ..
rm -rf "$BUNDLE_DIR"

echo ""
echo "✓ Bundle created: ${BUNDLE_NAME}"
echo ""
echo "Bundle structure:"
unzip -l "$BUNDLE_NAME" | head -20
echo ""
echo "To upload, run:"
echo "curl -X 'POST' \\"
echo "  'https://central.sonatype.com/api/v1/publisher/upload?publishingType=USER_MANAGED' \\"
echo "  -H 'accept: text/plain' \\"
echo "  -H 'Authorization: Basic WldKaVNmOlhlZVJFMHZObUwxNTRpOXBLNFQ2ZVQyM2FaSjNWWkFJSQ==' \\"
echo "  -H 'Content-Type: multipart/form-data' \\"
echo "  -F 'bundle=@${BUNDLE_NAME};type=application/zip'"

