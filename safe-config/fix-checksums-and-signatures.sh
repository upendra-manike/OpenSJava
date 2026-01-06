#!/bin/bash

# Fix missing checksums and ensure all files are signed
# This script generates MD5, SHA1 checksums and signs all artifacts

set -e

ARTIFACT_ID="safe-config"
VERSION="0.1.0"
GPG_KEY="537E05AE209ED7AE8790C4A4240C903B13D9C122"

cd "$(dirname "$0")"
cd target

echo "Generating checksums and verifying signatures..."
echo ""

# Function to generate checksums
generate_checksums() {
    local file=$1
    if [ -f "$file" ]; then
        echo "  Generating checksums for: $file"
        md5sum "$file" | cut -d' ' -f1 > "${file}.md5"
        sha1sum "$file" | cut -d' ' -f1 > "${file}.sha1"
    fi
}

# Function to sign file if not already signed
sign_file() {
    local file=$1
    if [ -f "$file" ] && [ ! -f "${file}.asc" ]; then
        echo "  Signing: $file"
        export MAVEN_GPG_PASSPHRASE='Blue-Running-2026!-stars'
        gpg --batch --pinentry-mode loopback --passphrase "$MAVEN_GPG_PASSPHRASE" \
            --default-key "$GPG_KEY" \
            --armor --detach-sign "$file"
    elif [ -f "${file}.asc" ]; then
        echo "  Already signed: $file"
    fi
}

# Process all required files
FILES=(
    "${ARTIFACT_ID}-${VERSION}.pom"
    "${ARTIFACT_ID}-${VERSION}.jar"
    "${ARTIFACT_ID}-${VERSION}-sources.jar"
    "${ARTIFACT_ID}-${VERSION}-javadoc.jar"
)

for file in "${FILES[@]}"; do
    if [ -f "$file" ]; then
        echo "Processing: $file"
        generate_checksums "$file"
        sign_file "$file"
        echo ""
    else
        echo "Warning: $file not found, skipping"
    fi
done

echo "✓ Checksums and signatures generated"
echo ""
echo "Files created:"
ls -lh *.md5 *.sha1 *.asc 2>/dev/null | head -20

