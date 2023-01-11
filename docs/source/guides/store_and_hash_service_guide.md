# The StoreAndHash Service

The StoreAndHash service provides an API for posting data to [AWS S3](https://aws.amazon.com/s3/) or [IPFS](https://ipfs.io/) storages, getting the hash of the data and URL for further download.

This document describes the service API and how to use it.

Working knowledge of gRPC is assumed.

## The StoreAndHash Service

The rationale is to provide a complement to the SubmitMetadata functionality.

Submitting large amounts of metadata is not practical, so a standard solution is to submit a hash of a document, thereby attesting to the
existence of the data by putting its unique fingerprint in an immutable blockchain

This API allows the caller to upload documents to a permanent storage (AWS) and
returns the permanent URL to the document, the SHA-256 hash (a unique fingerprint of the document), which can then be attested to through the `SubmitMetadata` functionality described elsewhere.

**Supported storages:**
* [AWS S3](#storeandhashhttp)
* [IPFS](#storeandhashipfs)

## The StoreAndHash Service API

This is a partial listing of the service
```
service StoreAndHashService {
    rpc StoreAndHashHttp (stream StoreAndHashRequest) returns (stream StoreAndHashResponse);
    rpc StoreAndHashIpfs (stream StoreAndHashIpfsRequest) returns (stream StoreAndHashResponse);
}

message StoreAndHashRequest {
    oneof options {
        Chunk chunk = 1;
        UploadDetails details = 2;
    }
}

message Chunk {
    bytes part = 1;
}

message UploadDetails {
    string path = 1;
    AwsCredentials aws = 2;
    iog.psg.service.common.CredentialsMessage credentials = 3;
}

message AwsCredentials {
   string keyId = 1;
   string keySecret = 2;
   string bucket = 3;
   string region = 4;
}

message StoreAndHashIpfsRequest {
  oneof options {
    Chunk chunk = 1;
    IpfsUploadDetails details = 2;
  }
}

message IpfsUploadDetails {
  IpfsAddress ipfsAddress = 1;
  iog.psg.service.common.CredentialsMessage credentials = 2;
}

message IpfsAddress {
  string host = 1;
  int32 port = 2;
}
```

### StoreAndHashHttp

The **StoreAndHashHttp** method expects a stream of requests from the client: **UploadDetails** and one or more **Chunk** of data to be stored. The result of the call is [StoreAndHashResponse](#storeandhashresponse)

***First***, the client must provide the `UploadDetails`, which consists of

- **Path**: The path within the bucket that the file will be stored at will form part of the download URL.

- **CredentialsMessage**: These are the clients secret key and identifiers so that they be validated and their credits debited

- **AwsCredentials**: The AWS credentials identify and authorize the service to upload to a particular aws s3 bucket.

**AwsCredentials** object is expected to contain:
* key id
* key secret
* bucket name
* region

Details on how to create a minimal IAM user suitable for use in this service are [here](create_minimal_s3_user.md)

***Second***, each following call should to contain a chunk of the bytes read from the file.

- chunk
```
message Chunk {
    bytes part = 1;
}
```

When the whole file is uploaded in chunks, the responses are returned.
The call provides a stream of responses: the first containing the hash of the file and second containing the permanent URL to file served over HTTP through the PSG service
or a client can choose to use the s3 URL provided by amazon or serve the file by any other means.

### StoreAndHashIpfs

The StoreAndHashIpfs method expects a stream of requests from the client: **IpfsUploadDetails** and one or more **Chunk** of data to be stored. The result of the call is [StoreAndHashResponse](#storeandhashresponse)

***First***, the client must provide the `IpfsUploadDetails`, which consists of

- **IpfsAddress**: IP address and port of IPFS node
  ```
  message IpfsAddress {
  string host = 1;
  int32 port = 2;
    }
  ```

- **CredentialsMessage**: These are the clients secret key and identifiers so that they be validated and their credits debited

***Second***, each following call should to contain a chunk of the bytes read from the file.

- chunk
```
message Chunk {
    bytes part = 1;
}
```

### StoreAndHashResponse 

The result of the call to service endpoint will be a stream of StoreAndHash responses containing one of the following:
 - **Hash** object contains the type of hash used (SHA-256) and hashes in bytes and base64 String formats.
 - **URL** to the uploaded file

**NOTE**: If any error occurred, the `AppError` response will be returned with the code and error message as strings:
```
message AppError {
  string code = 1;
  string msg = 2;
}
```

There are two groups of errors, that can occur while using PSG services:
- permanent (E.g. `failed to authorise call, valid API credentials should be provided`)
- temporary (E.g. `The system is temporarily unavailable. Please try again later`)

In case of temporary errors - you need just try to make a call to service later. 

# Client SDK API - How to

## maven dependency
```xml
<dependency>
    <groupId>solutions.iog</groupId>
    <artifactId>store-and-hash-client_2.13</artifactId>
    <version>0.0.3</version>
</dependency>
```

### Create the api
```java
 StoreAndHash storeAndHashApi = new StoreAndHash("API_TOKEN", "CLIENT_ID", "localhost", 2000);
```

### Store Metadata in AWS
* Replace `path` replace with <a href="https://docs.aws.amazon.com/AmazonS3/latest/userguide/object-keys.html">AWS object key name</a> for AWS bucket
* Replace `fileContent` with the content as a string value that will be stored in AWS S3
* Replace `awsKey` with timestamp to which metadata should be listed
* Replace `awsSecret` with aws secret
* Replace `awsBucket` with aws bucket
* Replace `awsRegion` with aws region

```java
    public CompletionStage<String> storeFileAtAws() {
        String path = "my-organization";
        String fileContent = "{\"name\": \"John\"}";

        String awsKey = "23423";
        String awsSecret = "23423";
        String awsBucket = "23423";
        String awsRegion = "us‑east‑1";

        AwsConf awsConf = new AwsConf(awsKey, awsSecret, awsBucket, awsRegion);
        CompletableFuture<String> completableFuture = new CompletableFuture();

        storeAndHashApi.storeAndHashHttp(path, fileContent.getBytes(StandardCharsets.UTF_8), awsConf, streamObserver(completableFuture, r -> r.getHash().toString()));
        return completableFuture;
    }
```

### Store Metadata in IPFS
* Replace `fileContent` with the content as a string value that will be stored in AWS S3
* Replace `host` with host of the ipfs file server
* Replace `port` port of the ipfs file server
```java
    public CompletionStage<String> storeFileAtIPFS() {
        String fileContent = "{\"name\": \"John\"}";

        String host = "ipfs host";
        Integer port = 5001;
        CompletableFuture<String> completableFuture = new CompletableFuture();
        storeAndHashApi.storeAndHashIpfs(host, port, fileContent, streamObserver(completableFuture, r -> r.getHash().toString()));
        return completableFuture;
    }
```

##### The Observer
The example of observer. This might be used in the example above. It gets the events about the transaction sent details.
```java
    private <V> StreamObserver<V> streamObserver(CompletableFuture<String> completableFuture,
                                                 Function<V, String> converter) {
        return new StreamObserver<V>() {
            List<String> buffer = new ArrayList();

            @Override
            public void onNext(V res) {
                buffer.add(converter.apply(res));
            }

            @Override
            public void onError(Throwable throwable) {
                buffer.add(throwable.toString());
            }

            @Override
            public void onCompleted() {
                String res = buffer
                        .stream()
                        .collect(Collectors.joining("\n"));
                completableFuture.complete(res);
            }
        };
    }
```