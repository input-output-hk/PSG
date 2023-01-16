# Metadata Service - JAVA SDK

## maven dependency
* Replace `${latest.version}` with latest version from maven central (https://central.sonatype.dev/artifact/solutions.iog/metadata-client_2.13/0.3.1) 
```xml
<dependency>
     <groupId>solutions.iog</groupId>
     <artifactId>metadata-client_2.13</artifactId>
    <version>${latest.version}</version>
</dependency>
```
### Create the api
```java
    // Creates client that connects to MAINNET by default. 
    Metadata client = MetadataBuilder
        .create("API_TOKEN", "CLIENT_ID")
        .build();
```

### Submit the Metadata to Blockchain
* Replace `metadata` with string value that represents your metadata
```java
    public CompletionStage<String> submitMetadata() {
        String metadata= "MyMetadata";
        CompletableFuture<String> completableFuture = new CompletableFuture();
        metadataApi.submitMetadata(metadata, streamObserver(completableFuture, r -> r.getTxStatus().toString()));
        return completableFuture;

    }
```    

### Get the Metadata
* Replace `txId` with the Id Transaction
```java
    public CompletionStage<String> transactionMetadata() {
        String txHash = "234234acb";
        CompletableFuture<String> completableFuture = new CompletableFuture();
        metadataApi.transactionMetadata(txHash, streamObserver(completableFuture, r -> r.getMetadata().toString()));
        return completableFuture;

    }
```

### List the Metadata transaction statuses
* Replace `startAt` with timestamp from which metadata should be listed
* Replace `endAt` with timestamp to which metadata should be listed
```java
    public CompletionStage<String> listMetadata() {
        Timestamp startAt = Timestamp.of(100000000, 0);
        Timestamp endAt = Timestamp.of(200000000, 0);
        CompletableFuture<String> completableFuture = new CompletableFuture();
        metadataApi.listMetadata(startAt, endAt, streamObserver(completableFuture, r -> r.getTxStatus().toString()));
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










