#!/bin/bash

# Upload GPG public key to PGP key servers
# This is required for Central to verify signatures

GPG_KEY="537E05AE209ED7AE8790C4A4240C903B13D9C122"

echo "Uploading GPG public key to PGP key servers..."
echo "Key ID: $GPG_KEY"
echo ""

# Try multiple key servers
SERVERS=(
    "hkps://keyserver.ubuntu.com"
    "hkps://keys.openpgp.org"
    "hkps://pgp.mit.edu"
    "hkps://keyserver.pgp.com"
)

for server in "${SERVERS[@]}"; do
    echo "Trying $server..."
    if gpg --keyserver "$server" --send-keys "$GPG_KEY" 2>&1 | grep -v "failed\|error" | head -1; then
        echo "✓ Successfully uploaded to $server"
    else
        echo "✗ Failed to upload to $server (this is OK, will try others)"
    fi
    echo ""
done

echo ""
echo "Alternative: Upload manually via web interface:"
echo "1. Export your public key: gpg --armor --export $GPG_KEY > gpg-key.asc"
echo "2. Visit: https://keyserver.ubuntu.com/"
echo "3. Paste the key content and submit"
echo ""
echo "Or use curl to upload:"
echo "curl -X POST --data-binary @- https://keyserver.ubuntu.com/pks/add <<< \"\$(gpg --armor --export $GPG_KEY)\""

