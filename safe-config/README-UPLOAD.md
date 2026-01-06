# Uploading to Sonatype Central

## Issues Fixed

✅ **Checksums Generated**: MD5 and SHA1 checksums for all files
✅ **Signatures**: All files (JAR, POM, sources, javadoc) are signed
✅ **Bundle Created**: Maven repository layout bundle with all artifacts

## Remaining Issue: GPG Public Key

The GPG public key needs to be uploaded to a PGP key server so Central can verify signatures.

### Option 1: Upload via Web Interface (Recommended)

1. Export your public key:
   ```bash
   gpg --armor --export 537E05AE209ED7AE8790C4A4240C903B13D9C122 > gpg-key.asc
   ```

2. Visit one of these key servers and paste the key:
   - https://keyserver.ubuntu.com/
   - https://keys.openpgp.org/
   - https://pgp.mit.edu/

3. Wait 5-10 minutes for the key to propagate

### Option 2: Use gpg command (if network allows)

```bash
gpg --keyserver hkps://keyserver.ubuntu.com --send-keys 537E05AE209ED7AE8790C4A4240C903B13D9C122
```

## Upload the Bundle

Once the GPG key is uploaded and propagated:

```bash
cd safe-config
./upload-bundle.sh
```

Or manually:

```bash
curl -X 'POST' \
  'https://central.sonatype.com/api/v1/publisher/upload?publishingType=USER_MANAGED' \
  -H 'accept: text/plain' \
  -H 'Authorization: Basic WldKaVNmOlhlZVJFMHZObUwxNTRpOXBLNFQ2ZVQyM2FaSjNWWkFJSQ==' \
  -H 'Content-Type: multipart/form-data' \
  -F 'bundle=@target/safe-config-0.1.0-bundle.zip;type=application/zip'
```

## Verify Upload

Check the deployment status at:
https://central.sonatype.com/publishing/deployments

After uploading the GPG key and waiting a few minutes, the signature validation errors should resolve.

