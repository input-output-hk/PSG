# Mail Service Usage Guide (gpg command line)

It is possible to send encrypted messages with attachments to Mail Service for further storing it in the Cardano blockchain.

Please, follow prerequisites and configuration steps.

## Prerequisites

To save transaction (with message and attachment), you will need:

* [gpg](https://gpgtools.org/) tool

* User's public/private key pair that is [generated and saved to local keychain](#how-to-generate-public-private-key-pair)

* Purchased package at PSG Self Service UI

* API Token - generated at PSG Self Service UI

* Recipient public key [is downloaded](#how-to-get-recipient-public-key)

* Recipient's public key [is added to local keychain](#how-to-add-recipient-public-key-to-the-local-keychain)

* AWS IAM user and S3 bucket [are configured](create_minimal_s3_user.md)

## Steps

1. Create a new user or login with an existing one to Self Service UI

2. Add your email address, public PGP key on the PGP page and save

3. Form a message to send according to specific [format](#email-message-format)

4. [Encrypt message](#how-to-encrypt-messages-with-gpg-tool) with recipient's public key (mandatory)

5. [Encrypt attachment](#how-to-encrypt-messages-with-gpg-tool) with recipient's public key

6. Sent email to [recipient](#how-to-get-recipient-public-key)

7. Wait for reply message (with link to the file on AWS S3 and link to the transaction in Cardano Explorer)

In case of success, you will get a message in reply:

``` text
Your message EMAIL_SUBJECT Thu Apr 29 11:56:34 GMT 2021 was processed successfully.

Transaction: https://explorer.cardano.org/en/transaction?id=<transactionId>

Attachments:
  http://aws_url/download/your_mail_com/96/test_attachment.txt
```

In case of failure, you will get the message in reply:

``` text
We could not process your message, subject: EMAIL_SUBJECT, sent date: Wed Apr 21 13:18:33 GMT 2021, please contact administrators.
```

11. [Download file](#how-to-download-file-from-s3-bucket) from AWS S3 bucket if needed

## Email message format

``` text
API_TOKEN=your_api_token
METADATA=Some Test Metadata for Encrypted Message
S3_BUCKET=your_bucket_name
S3_REGION=your_bucket_region
S3_KEY=aws_user_access_key
S3_SECRET=aws_user_secret_key
PLAINTEXT_REPLY=true
BASE_URL=optional_custom_download_url
```

**API_TOKEN** - User token, generated at [PSG Self Serve UI](https://psg.iog.services/)  

**METADATA** - User message to be included in the transaction metadata (Optional)

**S3_BUCKET** - AWS S3 bucket name  

**S3_REGION** - AWS S3 region name for bucket

**S3_KEY** - AWS IAM user access key  

**S3_SECRET** - AWS IAM user secret key  

**BASE_URL** - Custom URL prefix for files saved on AWS S3 (Optional)

**PLAINTEXT_REPLY** - If set to true - response email will not be encrypted by the recipient key.
Configured to **false** by default or if parameter is not specified. (Optional)

## How to encrypt messages with the gpg tool

``` bash
gpg --encrypt --sign --armor -u your@mail.com -r recipient@mail.com your_file.txt
```

``` bash
gpg --encrypt --sign --armor -u your@mail.com -r recipient@mail.com your_attachment.txt
```

### How to generate public/private key-pair

``` bash
gpg --gen-key
```

Recommended algorithm for keys is RSA 4096-bit.

Do not forget to specify **your email** during key generation.

### How to add recipient public key to the local keychain

``` bash
gpg --import recipient_public.key
```

### How to get recipient public key
**Recipient addresses:**

* psg.authemail@gmail.com (for [PSG Testnet Self Serve UI](https://psg-testnet.iog.services/))
* robot@iog.services (for [PSG Mainnet Self Serve UI](https://psg.iog.services/)) 

Find public key by email using one of the following PGP Key Servers:

* [keys.openpgp.org](https://keys.openpgp.org/)
* [Mailvelope Key Server](https://keys.mailvelope.com/)  

### How to download file from S3 bucket

- If you configured [public access for all](create_minimal_s3_user.md#L84) - you can download the file by executing GET request URL from Mail Service response
  (e.g.from browser)
- If you set restricted access, you need to include a custom headers to download request:
```
curl -i -H "aws_key: your_key" -H "aws_secret: your_secret" -H "aws_region: bucket_region"  https://psg.iog.services:2001/download/bucketname/path-to-file
```

