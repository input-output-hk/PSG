# Native Assets Service - JAVA SDK

## maven dependency
* Replace `${latest.version}` with latest version from maven central (https://central.sonatype.dev/artifact/solutions.iog/native-assets-client_2.13/0.3.1/versions)
```xml
<dependency>
    <groupId>solutions.iog</groupId>
    <artifactId>native-assets-client_2.13</artifactId>
    <version>${latest.version}</version>
</dependency>
```


### Create the api
* Make sure, that you have registered an account in PSG Self Serve UI, purchased a package and generated API Token
* Replace `clientId` with your PSG Self serve account
* Replace `token` with your PSG Self Serve generated API token
* By default, client will connect to psg.iog.services, which is operating on Cardano mainnet. There are additional methods on the builder that allows you to specify `host` to connect to different environments (like one operating on preprod Cardano network).

```java
NativeAssetsApi api = NativeAssetsBuilder.create(token, clientId).build();
```



### Create the Policy which allows to burn/mint 
* Replace `policyName` with arbitrary policy name
* Replace `beforeSlot` - optional - if specified it defines the `end` of time window in which the policy allows to burn/mint
* Replace `afterSlot` - optional - if specified it defines the `start` of time window in which the policy allows to burn/mint

```java
    public CompletionStage<Policy> createPolicy() {
        String policyName = "policyName";
        Integer afterSlot = 140215;
        Integer beforeSlot = 348215;

        TimeBounds timeBounds = TimeBounds
                .newBuilder()
                .setAfter(afterSlot)
                .setBefore(beforeSlot)
                .build();
        // Create the policy with name `My Policy`
        return api.createPolicy("policyName", timeBounds)
                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });

    }
```
### Get the Policy which allows to burn/mint
* Replace `policyId` with the `policyId`
```java
    public CompletionStage<Policy> getPolicy() {
        String policyId = "policyId";
        return api.getPolicy(policyId)
                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }
```
### Get all Policies which allows to burn/mint
```java
    public CompletionStage<List<Policy>> getPolicies() {
        return api.getPolices()
                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getPoliciesList();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }
```
### Delete a Policy which allows to burn/mint
* Replace `policyId` with the `policyId`
```java
    public CompletionStage<Boolean> deletePolicy() {
        String policyId = "policyId";
        return api.deletePolicy(policyId)
                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return true;
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }

```

### Create an Asset
* Replace `policyId` with the `policyId`
* Replace `assetName` with arbitrary `token name`
```java
    public CompletionStage<NativeAsset> createAsset() {
        String policyId = "policyId";
        String assetName = "assetName";
        
        NativeAssetId nativeAssetId = NativeAssetId
                .newBuilder()
                .setName(assetName)
                .setPolicyId(policyId)
                .build();

        return api.createAsset(nativeAssetId)
                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getAsset();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });

    }
```

### Get an Asset
* Replace `policyId` with the `policyId`
* Replace `assetName` with arbitrary `token name`
```java
    public CompletionStage<NativeAsset> getAsset() {
        String policyId = "policyId";
        String assetName = "assetName";
        
        NativeAssetId nativeAssetId = NativeAssetId
                .newBuilder()
                .setName(assetName)
                .setPolicyId(policyId)
                .build();
        return api.getAsset(nativeAssetId)
                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getAsset();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }
```

### Get all Assets
```java
    public CompletionStage<List<NativeAsset>> getAssets() {
        return api.listAssets()
                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getAssetsList();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }
```

### Delete an Asset
* Replace `policyId` with the `policyId`
* Replace `assetName` with arbitrary `token name`
```java
    public CompletionStage<Boolean> deleteAsset() {
        String policyId = "policyId";
        String assetName = "assetName";
        
        NativeAssetId nativeAssetId = NativeAssetId
                .newBuilder()
                .setName(assetName)
                .setPolicyId(policyId)
                .build();
        return api.deleteAsset(nativeAssetId)
                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return true;
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }
```
### Mint native assets

* Replace `policyId` with the `policyId`
* Replace `assetName` with arbitrary `token name`
* Replace `amount` with amount of tokens to mint
* Replace `depth` with the number of network confirmations required to treat the transaction as Confirmed
* Replace `nfts` with List containing  0 or n `nft metadata`  <a href="https://github.com/cardano-foundation/CIPs/tree/master/CIP-0025">CIP-0025</a> 
```java
    public CompletionStage<NativeAssetTransaction> mintAsset() {
        String policyId = "policyId";
        String assetName = "assetName";
        Long amount = 1l;
        Integer depth = 4;
       
        NativeAssetId nativeAssetId = NativeAssetId
                .newBuilder()
                .setName(assetName)
                .setPolicyId(policyId)
                .build();

        List<Nft> nfts = List.of(
                  Nft.newBuilder()
                    .setAssetName(assetName)
                    .setName(assetName)
                    .build()
                  );


        CompletableFuture<NativeAssetTransaction> completableFuture = new CompletableFuture();
        api.mintAsset(nativeAssetId, amount, nfts, depth, lastResult(completableFuture,
                r -> r.getAssetTx()));
        return completableFuture;

    }
```
**note** check observer sample for `lastResult`

### Mint Native Asset
* Replace `policyId` with the `policyId`
* Replace `assetName` with arbitrary `token name`
* Replace `amount` with amount of tokens to mint
* Replace `depth` with the number of network confirmations required to treat the transaction as Confirmed
* Replace `data` with map of arbitrary metadata
```java
    public CompletionStage<NativeAssetTransaction> mintWithArbitraryData() {
        String policyId = "policyId";
        String assetName = "assetName";
        Long amount = 10l;
        Integer depth = 4;
        
        NativeAssetId nativeAssetId = NativeAssetId
                .newBuilder()
                .setName(assetName)
                .setPolicyId(policyId)
                .build();
        //Arbitrary metadata
        Map<String, Value> data = new HashMap<>();
        Struct struct = com.google.protobuf.Struct.newBuilder()
                .putAllFields(data)
                .build();

        CompletableFuture<NativeAssetTransaction> completableFuture = new CompletableFuture();
        api.mintAssetWithArbitraryMetadata(nativeAssetId, amount, struct, depth, lastResult(completableFuture,
                r -> r.getAssetTx()));
        return completableFuture;

    }
```
**note** check observer sample for `lastResult`

### Burn a Native Asset
* Replace `policyId` with the `policyId`
* Replace `assetName` with arbitrary `token name`
* Replace `amount` with amount of tokens to burn
* Replace `depth` with the number of network confirmations required to treat the transaction as Confirmed
```java
    public CompletionStage<NativeAssetTransaction> burn() {
        String policyId = "policyId";
        String assetName = "assetName";
        Long amount = 10l;
        Integer depth = 4;
        
        NativeAssetId nativeAssetId = NativeAssetId
                .newBuilder()
                .setName(assetName)
                .setPolicyId(policyId)
                .build();

        CompletableFuture<NativeAssetTransaction> completableFuture = new CompletableFuture();
        api.burnAsset(nativeAssetId, amount, depth, lastResult(completableFuture,
                r -> r.getAssetTx()));
        return completableFuture;

    }
```
**note** check observer sample for `lastResult`


### Transfer a Native Asset
* Replace `policyId` with the `policyId`
* Replace `assetName` with arbitrary `token name`
* Replace `toAddress` with the address the token will be transferred to
* Replace `amount` with amount of tokens to transfer
* Replace `depth` with the number of network confirmations required to treat the transaction as Confirmed

```java
    public CompletionStage<NativeAssetTransaction> transfer() {
        String policyId = "policyId";
        String assetName = "assetName";
        String toAddress = "targetAddress";
        Long amount = 10l;
        Integer depth = 4;
        
        NativeAssetId nativeAssetId = NativeAssetId
                .newBuilder()
                .setName(assetName)
                .setPolicyId(policyId)
                .build();

        CompletableFuture<NativeAssetTransaction> completableFuture = new CompletableFuture();
        api.transferAsset(nativeAssetId, toAddress, amount, depth, lastResult(completableFuture,
                r -> r.getAssetTx()));
        return completableFuture;

    }
```
**note** check observer sample for `lastResult`


### Fund the address associated with the policy
* Replace `policyId` with the `policyId`
* Replace `amount` with amount of maximum lovelace to fund 
* Replace `depth` with the number of network confirmations required to treat the transaction as Confirmed
```java
    public CompletionStage<FundNativeAssetTransaction> fund() {
        String policyId = "policyId";
        Long amount = 10l;
        Integer depth = 4;

        CompletableFuture<FundNativeAssetTransaction> completableFuture = new CompletableFuture();
        api.fundPolicyAddress(policyId, amount, depth, lastResult(completableFuture,
                r -> r.getFundTx()));
        return completableFuture;

    }
```
**note** check observer sample for `lastResult`

### Create an Airdrop
* Replace `policyId` with the `policyId`
* Replace `assetName` with arbitrary `token name`
* Replace `targetAddress` with the address the assets will be sent to
* Replace `amount` with amount of asset to airdrop
```java
    public CompletionStage<String> sendAirDrop() {
        String policyId = "policyId";
        String assetName = "assetName";
        String targetAddress = "targetAddress";
        Long amount = 10l;

        List<AirDrop> airDrops = List.of(AirDrop.newBuilder()
                .setPolicyId(policyId)
                .setAssetName(assetName)
                .setAddress(targetAddress)
                .setAmountToAirDrop(amount)
                .build());
        
        //Some Metadata
        Map<String, Value> data = new HashMap<>();
        Struct struct = com.google.protobuf.Struct.newBuilder()
                .putAllFields(data)
                .build();

        return api.sendAirDropBatch(struct, airDrops)
                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getBatchId();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });

    }
```

### Check the airdrop status
* Replace `batchId` with the `batchId` of Airdrop
```java
    public CompletionStage<AirDropStatusResponse> airDropStatus() {
        String batchId = "batchId";
        return api.getAirDropStatus(batchId);

    }
```

### Observer sample used in snippets above
```java
public <E, R> StreamObserver<E> lastResult(CompletableFuture<R> result, Function<E, R> f) {
        return new StreamObserver<>() {
            ArrayList<R> buff = new ArrayList();

            @Override
            public void onNext(E event) {
                R converted = f.apply(event);
                System.out.println("Got interim response: " + converted);
                buff.add(converted);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Got error: " + throwable.getCause());
            }

            @Override
            public void onCompleted() {
                result.complete(buff.get(buff.size() - 1));
            }
        };

    }
```





