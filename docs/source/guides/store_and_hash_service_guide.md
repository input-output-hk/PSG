# The StoreAndHash Service

The StoreAndHash service provides an API for posting data to AWS S3 storage, getting the hash of the data and URL for further download.

This document describes the service API and how to use it.

Working knowledge of gRPC is assumed.

## The Service

The rationale is to provide a complement to the SubmitMetadata functionality.

Submitting large amounts of metadata is not practical, so a standard solution is to submit a hash of a document, thereby attesting to the
existence of the data by putting its unique fingerprint in an immutable blockchain

This API allows the caller to upload documents to S3 (other persistence mechanisms coming soon) as a permanent store and
returns the permanent URL to the document, the SHA-265 hash (a unique fingerprint of the document), which can then
be attested to through the `SubmitMetadata` functionality described elsewhere.

### The StoreAndHash Service API

#### StoreAndHashHttp

This is a partial listing of the service circa v0.2
```
service StoreAndHashService {
    rpc StoreAndHashHttp (stream StoreAndHashRequest) returns (stream StoreAndHashResponse);
}

message StoreAndHashRequest {
    oneof options {
        Chunk chunk = 1;
        UploadDetails details = 2;
    }
}

message UploadDetails {
    string path = 1;
    iog.psg.service.common.CredentialsMessage credentials = 2;
    AwsCredentials aws = 3;
}

message Chunk {
    bytes part = 1;
}

message AwsCredentials {
   string keyId = 1;
   string keySecret = 2;
   string bucket = 3;
   string region = 4;
}

```
The StoreAndHashHttp method expects a stream of requests from the client: UploadDetails and one or more Chunk of data to be stored.

First, the client must provide the `UploadDetails`, which consists of

- path

The path within the bucket that the file will be stored at will form part of the download URL.

- credentials

These are the clients secret key and identifiers so that they be validated and their credits debited

- aws

The aws credentials identify and authorize the service to upload to a particular aws s3 bucket.

AWS's credentials object is expected to contain:
* key id
* key secret
* bucket name
* region

Details on how to create a minimal IAM user suitable for use in this service are [here](create_minimal_s3_user.md)

Each following call should to contain a chunk of the bytes read from the file.

- chunk

When the whole file is uploaded in chunks, the responses are returned.
The call provides a stream of responses: the first containing the hash of the file and second containing the permanent URL to file served over HTTP through the PSG service
or a client can choose to use the s3 URL provided by amazon or serve the file by any other means.

The result will be a stream of responses:
A hash object contains the type of hash used (SHA-256) and hashes in bytes and base64 String formats.
- URL to file stored

If any issue occurred, the `problem` field of response contains the root cause of the case.