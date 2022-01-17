# PSG Services - JS Client Example 

## Prerequisites 
Install the following tools:

- [Node.js & npm](https://docs.npmjs.com/downloading-and-installing-node-js-and-npm) 
- [Protocol Buffer Compiler](https://grpc.io/docs/protoc-installation/)
- [grpc-tools](https://www.npmjs.com/package/grpc-tools)

**NOTE:** make sure, that **for development and testing purposes** you are using **[PSG Services - Testnet](https://psg-testnet.iog.services/)**

## How to generate gRPC clients:

1. Proceed to the /examples/js folder
2. Execute in Terminal
```shell
grpc_tools_node_protoc --proto_path=../../protos/cardano-metadata-service/protobuf \
  --proto_path=../../protos/common-service/protobuf \
  --proto_path=../../protos/store-and-hash-service/protobuf \
  --js_out=import_style=commonjs,binary:src \
  --grpc_out=grpc_js:src ../../protos/*/protobuf/*.proto
```
3. As a result, gRPC clients will be generated to the src folder

## How to execute example for Metadata service

1. Make sure, that you have registered an account in [PSG Self Serve UI](https://psg.iog.services/), purchased a package and generated API Token.
2. Set your username and API Token in the src/metadata_service_client.js file:
```shell
const credentials = new CommonMessages.CredentialsMessage()
    .setClientId("CLIENT_ID")
    .setApiToken("API_TOKEN");
```
3. Execute in Terminal:
```shell
node src/metadata_service_client.js
```
## How to execute example for StoreAndHashHttp

1. Make sure, that you have registered an account in [PSG Self Serve UI](https://psg.iog.services/), purchased a package and generated API Token.
2. Replace CLIENT_ID and API_TOKEN with your PSG Self Serve UI account values in src/store_and_hash_http_client.js
```shell
const credentials = new CommonMessages.CredentialsMessage()
    .setClientId("CLIENT_ID")
    .setApiToken("API_TOKEN");
```
3. Replace AWS_ACCESS_KEY, AWS_SECRET_KEY, AWS_S3_BUCKET and BUCKET_REGION with your S3 account values and replace demo-file-path with your desired path in src/store_and_hash_http_client.js:
```shell
const storeAndHashHttpRequests = [
    new StoreAndHashMessages.StoreAndHashRequest()
        .setDetails(
            new StoreAndHashMessages.UploadDetails()
                .setAws(new StoreAndHashMessages.AwsCredentials()
                    .setKeyid("AWS_ACCESS_KEY")
                    .setKeysecret("AWS_SECRET_KEY")
                    .setBucket("AWS_S3_BUCKET")
                    .setRegion("BUCKET_REGION")
                )
                .setCredentials(credentials)
                .setPath("demo-file-path")
        ),
```

3. Execute in Terminal:
```shell
node src/store_and_hash_http_client.js
```

## How to execute example for StoreAndHashIpfs

1. Make sure, that you have registered an account in [PSG Self Serve UI](https://psg.iog.services/), purchased a package and generated API Token.
2. Replace CLIENT_ID and API_TOKEN with your PSG Self Serve UI account values in src/store_and_hash_ipfs_client.js
```shell
const credentials = new CommonMessages.CredentialsMessage()
    .setClientId("CLIENT_ID")
    .setApiToken("API_TOKEN");
```
3. Replace IPFS_HOST, IPFS_PORT with your chosen IPFS node ip address and port in src/store_and_hash_ipfs_client.js:
```shell
const storeAndHashIpfsRequests = [
    new StoreAndHashMessages.StoreAndHashIpfsRequest()
        .setDetails(
            new StoreAndHashMessages.IpfsUploadDetails()
            .setIpfsaddress(new StoreAndHashMessages.IpfsAddress()
            .setHost("IPFS_HOST").setPort(IPFS_PORT))
            .setCredentials(credentials)
        ),
```

3. Execute in Terminal:
```shell
node src/store_and_hash_ipfs_client.js
```
