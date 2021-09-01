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
    .setClientId("your_psg_username")
    .setApiToken("your_api_token");
```
3. Execute in Terminal:
```shell
node src/submit_metadata_client.js
```
## How to execute example for StoreAndHash service

1. Make sure, that you have registered an account in [PSG Self Serve UI](https://psg.iog.services/), purchased a package and generated API Token.
2. Update access key, secret key, bucket and region from your AWS S3 bucket in src/store_and_hash_client.js:
```shell
const reqs = [
    new StoreAndHashMessages.StoreAndHashRequest()
        .setDetails(
            new StoreAndHashMessages.UploadDetails()
                .setAws(new StoreAndHashMessages.AwsCredentials()
                    .setKeyid("AWS_ACCCESS_KEY")
                    .setKeysecret("AWS_SECRET_KEY")
                    .setBucket("AWS_S3_BUCKET")
                    .setRegion("BUCKET_REGION")
                )
```

3. Execute in Terminal:
```shell
node src/store_and_hash_client.js
```
