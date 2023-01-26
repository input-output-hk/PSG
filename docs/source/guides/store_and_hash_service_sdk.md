# Client SDK API - How to

## maven dependency
* Replace `${latest.version}` with latest version from maven central (https://central.sonatype.dev/artifact/solutions.iog/store-and-hash-client_2.13/0.3.1/versions)
```xml
<dependency>
    <groupId>solutions.iog</groupId>
    <artifactId>store-and-hash-client_2.13</artifactId>
    <version>${latest.version}</version>
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