# Client SDK API - How to 

## maven dependency
* Replace `${latest.version}` with latest version from maven central (https://search.maven.org/search?q=a:native-assets-client_2.13)
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
public NativeAssetsMultisigApi nativeAssetsMultisigApi() {
        return NativeAssetsBuilder
        .create(apiKey, clientId)
        .buildMultisig();
}
```

### Create the Policy which allows to burn/mint the holders of secret keys associated with provided public keys
* Replace `policyName` with arbitrary policy name
* Replace `hexPubKey` public key in hex format. The policy allows only the holder of associated `secret key` to the provided public key to mint/burn
* Replace `beforeSlot` - optional - if specified it defines the `end` of time window in which the policy allows to burn/mint 
* Replace `afterSlot` - optional - if specified it defines the `start` of time window in which the policy allows to burn/mint
```java
    public CompletionStage<Policy> createPolicyWithPublicKeys() throws CborSerializationException {
        String policyName = "policy";
        String hexPubKey1 = "ac3c4";
        String hexPubKey1 = "bd4d5";
        Optional<Integer> beforeSlot = Optional.of(8231864);
        Optional<Integer> afterSlot =  Optional.of(8200864);
        VerificationKey pubKey = VerificationKey.create(HexUtil.decodeHexString(hexPubKey));

        return nativeAssetsMultisigApi.createPolicy(policyName, beforeSlot, afterSlot, Arrays.asList(pubKey))
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }
```
### Create the Policy with secret keys
* Replace `policyName` with arbitrary policy name
* Replace `hexSecKey` the secret key in hex format. The policy allows only the holder of this key to mint/burn
* Replace `beforeSlot` - optional - if specified it defines the `end` of time window in which the policy allows to burn/mint
* Replace `afterSlot` - optional - if specified it defines the `start` of time window in which the policy allows to burn/mint
```java
    public CompletionStage<Policy> createPolicyWithSecKeys() throws CborSerializationException {
        String policyName = "policy";
        String hexSecKey = "ac3c4";
        Optional<Integer> beforeSlot = Optional.of(8231864);
        Optional<Integer> afterSlot =  Optional.of(8200864);
        SecretKey secKey = SecretKey.create(HexUtil.decodeHexString(hexSecKey));

        return nativeAssetsMultisigApi.createPolicyUsingPrivateKeys(policyName, beforeSlot, afterSlot, Arrays.asList(secKey))
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }
```
### Get the Policy by policy id
* Replace `policyId` with Id of Policy 
```java
    public CompletionStage<Policy> getPolicyById() {
        String policyId = "policyId";
        return nativeAssetsMultisigApi.getPolicyById(policyId)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }
```
### Get the Policy by policy name
* Replace `policyByName` with policy name 
```java
    public CompletionStage<Policy> getPolicyByName() {
        String policyByName = "PolicyByName";
        return nativeAssetsMultisigApi.getPolicyByName(policyByName)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }
```
### List all created policies
```java
    public CompletionStage<List<Policy>> listPolices() {
        return nativeAssetsMultisigApi.listPolices()
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return JavaConverters.seqAsJavaList(response.policies());
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }
```
### Create mint transaction for NFT
* Replace `assetName` with the name of NFT
* Replace `policyId` the Id of Policy
* Replace `paymentAddress` the address from which the mint fee will be deducted
* Replace `mintTargetAddress` - the address to which the asset will be minted
```java
    public CompletionStage<String> createMintTransaction() {
        String assetName = "assetName";
        String policyId = "policyId";
        String paymentAddress = "ad23424a23424";
        String mintTargetAddress = "ad23424a23424";

        Nft nativeNft = Nft$.MODULE$.defaultInstance()
                .withName(assetName)
                .withAssetName(assetName);

        AddressedNft addressedNft = AddressedNft$.MODULE$.defaultInstance()
                .withNft(nativeNft)
                .withAddress(mintTargetAddress);

        return nativeAssetsMultisigApi.createMintTransaction(policyId, paymentAddress, addressedNft)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getTx().tx();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }
```
### Create mint transaction native asset
* Replace `assetName` with the name of Native Asset
* Replace `policyId` the Id of Policy 
* Replace `paymentAddress` the address from which the mint fee will be deducted
* Replace `mintTargetAddress` - the address to which the asset will be minted
* Replace `amount` - with amount of Native Asset you want to mint
* Replace `json` - in the form of Java Map. This json/map should correspond with **(https://developers.cardano.org/docs/transaction-metadata/)**
```java

    public CompletionStage<String> createMintTransactionWithArbitraryMetadata() {
        String assetName = "assetName";
        String policyId = "policyId";
        Long amount = 1000l;
        String paymentAddress = "ad23424a23424";
        String mintTargetAddress = "bbcd2124389096753424a23424";
        Map json = new HashMap();

        Struct struct = com.google.protobuf.struct.Struct.of(
                CollectionConverters.MapHasAsScala(json).
                        asScala()
                        .toMap($less$colon$less$.MODULE$.refl())
        );

        NativeAssetId nativeAssetId = NativeAssetId$.MODULE$
                .defaultInstance()
                .withName(assetName)
                .withPolicyId(policyId);
        NativeAsset nativeAsset = NativeAsset$.MODULE$
                .defaultInstance()
                .withAmount(amount)
                .withId(nativeAssetId);

        AddressedNativeAsset addressedNativeAsset = AddressedNativeAsset$.MODULE$
                .defaultInstance()
                .withAddress(mintTargetAddress)
                .withNativeAsset(nativeAsset);

        AddressedNativeAssets assets = AddressedNativeAssets$.MODULE$
                .defaultInstance()
                .withPaymentAddress(paymentAddress)
                .withMetadata(struct)
                .withNativeAssets(JavaConverters.asScala(Arrays.asList(addressedNativeAsset)).toSeq());


        return nativeAssetsMultisigApi.createMintTransactionWithArbitraryMetadata(assets)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getTx().tx();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }
```
### Create transfer transaction
* Replace `assetName` with the name of Native Asset
* Replace `policyId` the Id of Policy
* Replace `fromAddress` with the address from which the asset will be transferred out
* Replace `toAddress` with the address to which the asset will be transferred in
* Replace `amount` with amount of Native Asset you want to transferr
```java
    public CompletionStage<String> createTransferTransaction() {
        String policyId = "policyId";
        String assetName = "assetName";
        String fromAddress = "ad23424a23424";
        String toAddress = "cc4a23424";
        Long amount = 10l;

        return nativeAssetsMultisigApi.createTransferTransaction(policyId, assetName, fromAddress, toAddress, amount)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getTx().tx();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }
```
### Create burn transaction
* Replace `policyId` the Id of Policy
* Replace `assetName` with the name of Native Asset
* Replace `targetAddress` - the address from which the asset will be burnt
* Replace `amount` - with amount of Native Asset you want to burn
```java
    public CompletionStage<String> createBurnTransaction() {
        String policyId = "policyId";
        String assetName = "assetName";
        String targetAddress = "ad23424a23424";
        Long amount = 10l;
        return nativeAssetsMultisigApi.createBurnTransaction(policyId, assetName, targetAddress, amount)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getTx().tx();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }
```
### Add witness with secret key
* Replace `txId` with the Id Transaction
* Replace `secretKeyHex` with the private key in Hex format, which will be used to sign the transaction with
```java
    public CompletionStage<String> addWitnessWithSecretKey() throws CborSerializationException {
        String txId = "ab2343245";
        String secretKeyHex = "cccc232c";
        SecretKey privateKey = SecretKey.create(HexUtil.decodeHexString(secretKeyHex));
        return nativeAssetsMultisigApi.addWitness(txId, privateKey)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return "signature added";
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }
```
### Add witness with public key and signature
* Replace `txId` with the Id Transaction
* Replace `publicKeyHex` with the public key in Hex format, which is used for verification of signature provided
* Replace `signatureHex` with signature in Hex format. This signature must be created with secret key that corresponds to public key above
```java
    public CompletionStage<String> addWitnessWithPublicKey() throws CborSerializationException {
        String txId = "ab2343245";
        String publicKeyHex = "cccc232c";
        String signatureHex = "ccadcc2c";
        VerificationKey publicKey = VerificationKey.create(HexUtil.decodeHexString(publicKeyHex));

        return nativeAssetsMultisigApi.addWitness(txId, publicKey, HexUtil.decodeHexString(signatureHex))
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return "signature added";
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }
```
### Get Transaction by ID
* Replace `txId` with the Id Transaction
```java
    public CompletionStage<Long> getTx() throws CborSerializationException {
        String txId = "ab2343245";

        return nativeAssetsMultisigApi.getTx(txId)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getTx().confirmations();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }
```
### List all Transactions 
```java
    public CompletionStage<List<String>> listTxs() {

        return nativeAssetsMultisigApi.listTxs()
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return JavaConverters.asJava(response.transactions()).stream().map(ur -> ur.txId()).collect(Collectors.toList());
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }
```
### List all Transactions belonging to policy with ID
* Replace `policyId` with the Policy id
```java
    public CompletionStage<List<String>> listTxsByPolicyId()  {
        String policyId = "ab2343245";

        return nativeAssetsMultisigApi.listTxs(policyId)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return JavaConverters.asJava(response.transactions()).stream().map(ur -> ur.txId()).collect(Collectors.toList());
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }
```
### List all witnesses
* Replace `txId` with the Id Transaction
```java
    public CompletionStage<List<String>> listWitnesses()  {
        String txId = "ab2343245";

        return nativeAssetsMultisigApi.listWitnesses(txId)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return  JavaConverters.asJava(response.verKeyHashes());
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }

```
### Submit the transaction to the blockchain
* Replace `txId` with the Id Transaction
* Replace `depth` with the Integer - specifies the number of network Confirmations required to treat the transaction as Confirmed
```java
    public CompletionStage<String> sendTransaction()  {
        String txId = "ab2343245";
        Integer depth = 4;
        CompletableFuture<String> completableFuture = new CompletableFuture();
        nativeAssetsMultisigApi.sendTransaction(txId, depth, streamObserver(completableFuture, (r -> r.getSendTx().toString())));
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
### Generate cardano enterprise address
* Replace `hexPubKey` with the public key in Hex format, for which the Cardano Address will be generated
```java
    public Address generateAddress() throws CborSerializationException {
        String hexPubKey = "ac3c4";
        return nativeAssetsMultisigApi.generateAddress(VerificationKey.create(HexUtil.decodeHexString(hexPubKey)), Networks.preprod());
    }
```

### Generate public key
* Replace `hexSecKey` with the secret key in Hex format, for which the Public Key will be generated
```java
    public VerificationKey generateVerKey() throws CborSerializationException {
        String hexSecKey = "ac3c4";
        return nativeAssetsMultisigApi.generateVerificationKey(SecretKey.create(HexUtil.decodeHexString(hexSecKey)));
    }

```

### Generate key pair (secret key and associated public key)
```java
    public Keys generateKeys() throws CborSerializationException {
        return nativeAssetsMultisigApi.generateKeys();
    }
```



