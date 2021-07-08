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

## Steps

1. Create a new user or login with an existing one to Self Service UI

2. Add your email address, public PGP key on the PGP page and save

3. Form a message to send according to specific [format](#email-message-format)

4. [Encrypt message](#how-to-encrypt-messages-with-gpg-tool) with recipient's public key (mandatory)

5. [Encrypt attachment](#how-to-encrypt-messages-with-gpg-tool) with recipient's public key

6. Sent email to recipient@mail.com

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

## Email message format

``` text
    PLAINTEXT_REPLY=true
    API_TOKEN=your_api_token
    METADATA=Your desired information to be stored in blockchain
```

**PLAINTEXT_REPLY=true** - PSG Mail Service will send the reply in plain text

**PLAINTEXT_REPLY=false** - PSG Mail Service will send the reply encrypted by the recipient key

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
Recipient addresses:

* psg.authemail@gmail.com (for [PSG Testnet Self Serve UI](https://psg-testnet.iog.services/))
* robot@iog.services (for [PSG Mainnet Self Serve UI](https://psg.iog.services/)) 

Find public key by email using one of the following PGP Key Servers:

* [keys.openpgp.org](https://keys.openpgp.org/)
* [Mailvelope Key Server](https://keys.mailvelope.com/)  

