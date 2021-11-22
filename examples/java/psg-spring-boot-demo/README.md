# PSG Services - JAVA example (Spring Boot)

## Prerequisites
Install the following tools:

- [Java](https://www.oracle.com/java/technologies/downloads/)
- [Protocol Buffer Compiler](https://grpc.io/docs/protoc-installation/)

**NOTE:** make sure, that **for development and testing purposes** you are using **[PSG Services - Testnet](https://psg-testnet.iog.services/)**

## How to run example service:

1. Make sure, that you have registered an account in [PSG Self Serve UI](https://psg.iog.services/), purchased a package and generated API Token

2. Set your username and API Token in the src/resources/application.properties file:
```shell
psg.client=your_psg_username
psg.token=your_api_token
```

3. Replace AWS_ACCESS_KEY, AWS_SECRET_KEY, AWS_S3_BUCKET and BUCKET_REGION with your S3 account values and replace demo-file-path with your desired path in src/resources/application.properties:
```shell
s3.key=AWS_ACCCESS_KEY
s3.secret=AWS_SECRET_KEY
s3.bucket=AWS_S3_BUCKET
s3.region=BUCKET_REGION
```

3. Run Spring Boot application by executing the following command in terminal:
```shell
$ mvn spring-boot:run
```
### Metadata service
- ListMetadata: ```$ curl -X GET "http://localhost:8080/listmetadata"```

- SubmitMetadata:```$ curl -X POST "http://localhost:8080/submitmetadata?metadata=SOMEDATA"```

### StoreAndHash service
- StoreAndHash (save file): ```$ curl -X POST "http://localhost:8080/store?path=test&content=test"```
- StoreAndHash (get results): ```$ curl -X GET "http://localhost:8080/store/result"```