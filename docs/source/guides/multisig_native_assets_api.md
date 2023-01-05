# Multisig Assets Service - API

This document describes using the service as an API. Working knowledge of gRpc is assumed.

## Service API

This is a partial listing of the NativeAssetsMultisigService (v0.3.19)
```
service NativeAssetsMultisigService {
  rpc importPolicy(ImportPolicyRequest) returns (ImportPolicyResponse);
  rpc createPolicy(CreatePolicyRequest) returns (CreatePolicyResponse);
  rpc getPolicy(GetPolicyRequest) returns (GetPolicyResponse);
  rpc listPolicies(ListPoliciesRequest) returns (ListPoliciesResponse);

  rpc createMintTx(CreateMintTxRequest) returns (UnsignedTxResponse);
  rpc createMintTxWithArbitraryMetadata(CreateMintTxWithArbitraryMetadataRequest) returns (UnsignedTxResponse);
  rpc createTransferTx(CreateTransferTxRequest) returns (UnsignedTxResponse);
  rpc createBurnTx(CreateBurnTxRequest) returns (UnsignedTxResponse);

  rpc addWitness(AddWitnessRequest) returns (EmptyResponse);

  rpc getTx(GetTxRequest) returns (GetTxResponse);
  rpc listTxs(ListTxsRequest) returns  (ListTxsResponse);

  rpc listWitnesses(ListWitnessesRequest) returns (ListWitnessesResponse);
  rpc sendTx(SendTxRequest) returns (EmptyResponse);
}
```

### Create Policy
```
message CreatePolicyRequest {
  string name = 1;
  TimeBounds timeBounds = 2;
  repeated string verificationKeys = 3;
  iog.psg.service.common.CredentialsMessage credentials = 4;
}

message CreatePolicyResponse {
  oneof result {
    AppError problem = 1;
    Policy policy = 2;
  }
}
```
#### CreatePolicyRequest
- **name** (unique name of the policy)
- **timeBounds** (time bounds for a policy to be created. Consists of two parts: `before` and `after`)
- **verificationKeys** (list of verification keys from which the `keyhashes` are generated and used in Policy )
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.)
#### CreatePolicyResponse
- In the case of `success` the call will return a new `Policy` object.
- In the case of a `problem`, that problem is described in the `problem` field of the response.`

### Get Policy
```
message GetPolicyRequest {
  oneof request {
    string name = 1;
    string policyId = 2;
  }
  iog.psg.service.common.CredentialsMessage credentials = 3;
}

message GetPolicyResponse {
  oneof result {
    AppError problem = 1;
    Policy policy = 2;
  }
}
```
#### GetPolicyRequest
To get policy the caller provides:
- either **policyId** or **name**:
  - **policyId** (unique id of the policy) 
  - **name** (the name of the policy)
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.)
#### GetPolicyResponse
- In the case of `success` the call will return a new `Policy` object.
- In the case of a `problem`, that problem is described in the `problem` field of the response.`

### List Policies
```
message ListPoliciesRequest {
  iog.psg.service.common.CredentialsMessage credentials = 1;
}

message ListPoliciesResponse {
  AppError problem = 1;
  repeated Policy policies = 2;
}
```
#### ListPoliciesRequest
To get the list of policies the caller provides:
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.)
#### ListPoliciesResponse
- In the case of `success` the call will return the list of created `Policy` objects.  
- In the case of a `problem`, that problem is described in the `problem` field of the response.
### Create Mint Transaction
```
message CreateMintTxRequest {
  AddressedNfts addressedNfts = 1;
  iog.psg.service.common.CredentialsMessage credentials = 2;
}
message UnsignedTxResponse {
  oneof result {
    AppError problem = 1;
    UnsignedTx tx = 2;
  }
}

message AddressedNfts {
  string policyId = 1;
  // address which will pay for the transaction
  string paymentAddress = 2;
  repeated AddressedNft addressedNfts = 3;
}

message AddressedNft {
  iog.psg.service.nativeassets.Nft nft = 1;
  // address to which the asset is going to be minted/transferred
  string address = 2;
}
// CIP 25 compliant Nft
message Nft {
  string assetName = 1;
  string name = 2;
  repeated string image = 3;
  string mediaType = 4;
  repeated string description = 5;
  repeated NftFile files = 6;
}

message NftFile {
  string name = 1;
  string mediaType = 2;
  repeated string src = 3;
}
```
#### CreateMintTxRequest
To create mint transaction the caller provides:
- **addressedNfts** (nft object that contains to `policyId`, `paymentAddress` and `AddressedNft` object)
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.)
#### UnsignedTxResponse 
- In the case of `success` the call will return the `UnsignedTx` object which contains `transaction id` and `transaction body`.
- In the case of a `problem`, that problem is described in the `problem` field of the response.
- 
#### Addressed Nfts
- **policyId** (unique policyId)
- **paymentAddress** (the address where the transaction fees are substracted `from`)
- **addressedNft** (nft object that contains the Nft object and address where the NTF is going to be minted `to`)

#### AddressedNft 
- **nft** (the Nft object to create)
- **address** (the address where the NFT is minted to)


### Create Mint Transaction wit Arbitrary Data
```
message CreateMintTxWithArbitraryMetadataRequest {
  AddressedNativeAssets nativeAssets = 1;
  iog.psg.service.common.CredentialsMessage credentials = 2;
}

message UnsignedTxResponse {
  oneof result {
    AppError problem = 1;
    UnsignedTx tx = 2;
  }
}

message AddressedNativeAssets {
  repeated AddressedNativeAsset nativeAssets = 1;
  // arbitrary json acceptable as cardano blockchain metadata
  google.protobuf.Struct metadata = 2;
  // address which will pay for the transaction
  string paymentAddress = 3;
}

message AddressedNativeAsset {
  NativeAsset nativeAsset = 1;
  // address to which the asset is going to be minted/transferred
  string address = 2;
}

message NativeAsset {
  NativeAssetId id = 1;
  uint64 amount = 2;
}

message NativeAssetId {
  string name = 1;
  string policyId = 2;
}
```
#### CreateMintTxWithArbitraryMetadataRequest
To create mint transaction with arbitrary data the caller provides:
- **nativeAssets** (nativeAssets object that contains list of `NativeAssets` with `metadata` and `payment address`)
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.)
#### UnsignedTxResponse
- In the case of `success` the call will return the `UnsignedTx` object which contains `transaction id` and `transaction body`.
- In the case of a `problem`, that problem is described in the `problem` field of the response.
#### AddressedNativeAssets
- **nativeAssets** (the list of AddressedNativeAsset objects)
- **metadata** (the metadata of NativeAsset)
- **paymentAddress** (the address where the transaction fees are substracted from)

#### AddressedNativeAsset
- **nativeAsset** (the object that contains **Native Asset id** and **amount**)
- **address** (the address where the Native Asset is minted to)

#### NativeAsset
- **id** (the NativeAsset id)
- **amount** (the amount to mint)

#### NativeAssetId
- **name** (the name of Native Asset)
- **policyId** (the policy Id which refers to the Policy under which the Native Asset could be minted)

### Create Transfer Transaction
```
message CreateTransferTxRequest {
  NativeAsset nativeAsset = 1;
  string fromAddress = 2;
  string toAddress = 3;
  iog.psg.service.common.CredentialsMessage credentials = 4;
}


message CreateTransferTxResponse {
  oneof result {
    AppError problem = 1;
    UnsignedTx tx = 2;
  }
}

message NativeAsset {
  NativeAssetId id = 1;
  uint64 amount = 2;
}

message NativeAssetId {
  string name = 1;
  string policyId = 2;
}
```
#### CreateTransferTxRequest
- **nativeAsset** (the object that contains **Native Asset id** and **amount**)
- **fromAddress** (the address on Cardano blockchain to transfer tokens from)
- **toAddress** (the address on Cardano blockchain to transfer tokens to)
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.)

#### CreateTransferTxResponse
- In the case of `success` the call will return the `UnsignedTx` object which contains `transaction id` and `transaction body`.
- In the case of a `problem`, that problem is described in the `problem` field of the response.

#### NativeAsset
- **id** (the NativeAsset id)
- **amount** (the amount to transfer)

#### NativeAssetId
- **name** (the name of Native Asset)
- **policyId** (the policy Id which refers to the Policy under which the Native Asset was minted)

### Create Burn Transaction
```
message CreateBurnTxRequest {
  NativeAsset nativeAsset = 1;
  string address = 2;
  iog.psg.service.common.CredentialsMessage credentials = 3;
}

message UnsignedTxResponse {
  oneof result {
    AppError problem = 1;
    UnsignedTx tx = 2;
  }
}

message NativeAsset {
  NativeAssetId id = 1;
  uint64 amount = 2;
}

message NativeAssetId {
  string name = 1;
  string policyId = 2;
}
```
#### CreateBurnTxRequest
- **nativeAsset** (the object that contains **Native Asset id** and **amount**)
- **address** (the address on Cardano blockchain from which the native assets are burnt `from`)
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.)

#### UnsignedTxResponse
- In the case of `success` the call will return the `UnsignedTx` object which contains `transaction id` and `transaction body`.
- In the case of a `problem`, that problem is described in the `problem` field of the response.

#### NativeAsset
- **id** (the NativeAsset id)
- **amount** (the amount to burn)

#### NativeAssetId
- **name** (the name of Native Asset)
- **policyId** (the policy Id which refers to the Policy under which the Native Asset could be minted)


### Adding Witness
```
message AddWitnessRequest {
  reserved 2;
  string txId = 1;
  string witnessFileContent = 3;
  iog.psg.service.common.CredentialsMessage credentials = 4;
}

message EmptyResponse {
  oneof result {
    AppError problem = 1;
    google.protobuf.Empty empty = 2;
  }
}
```


#### AddWitnessRequest
- **txId** (the transaction id to sign off)
- **witnessFileContent** (the content of witness in cardano cli witness format )
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.)

#### EmptyResponse
- In the case of `success` the call will return the `Empty` object
- In the case of a `problem`, that problem is described in the `problem` field of the response.

### Get Transaction
```
message GetTxRequest {
  string txId = 1;
  iog.psg.service.common.CredentialsMessage credentials = 2;
}

message GetTxResponse {
  oneof result {
    AppError problem = 1;
    TxWithConfirmations tx = 2;
  }
}
```
#### GetTxRequest
- **txId** (the transaction id)
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.)

#### GetTxResponse
- In the case of `success` the call will return the `TxWithConfirmations` which contains `UnsignedTx` object and `confirmations` count.
- In the case of a `problem`, that problem is described in the `problem` field of the response.


### List Transactions
```
message ListTxsRequest {
  optional string policyId = 1;
  iog.psg.service.common.CredentialsMessage credentials = 2;
}

message ListTxsResponse {
  AppError problem = 1;
  repeated UnsignedTx transactions = 2;
}

```
#### ListTxsRequest
- **policyId** (the id of policy associated with )
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.)

#### ListTxsResponse
- In the case of `success` the call will return the `UnsignedTx` object which contains `transaction id` and `transaction body`.
- In the case of a `problem`, that problem is described in the `problem` field of the response.

### List witnesess
```
message ListWitnessesRequest {
  string txId = 1;
  iog.psg.service.common.CredentialsMessage credentials = 2;
}

message ListWitnessesResponse {
  AppError problem = 1;
  repeated string verKeyHashes = 2;
}
```
#### ListWitnessesRequest
- **txId** (the transaction id)
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.)

#### ListWitnessesResponse
- In the case of `success` the call will the list of `hashKeys` associated with `transaction` id
- In the case of a `problem`, that problem is described in the `problem` field of the response.


### Send the Transaction
```
message SendTxRequest {
  string txId = 1;
  iog.psg.service.common.CredentialsMessage credentials = 2;
}

message EmptyResponse {
  oneof result {
    AppError problem = 1;
    google.protobuf.Empty empty = 2;
  }
}

```
#### SendTxRequest
- **txId** (the transaction id)
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.)

#### EmptyResponse
- In the case of `success` the call will return the `Empty` object
- In the case of a `problem`, that problem is described in the `problem` field of the response.

# Client SDK API - How to 

## maven dependency
```xml
<dependency>
    <groupId>solutions.iog</groupId>
    <artifactId>native-assets-client_2.13</artifactId>
    <version>0.0.3</version>
</dependency>
```


### Create the api
* Make sure, that you have registered an account in PSG Self Serve UI, purchased a package and generated API Token
* Replace `clientId` with your PSG Self serve account
* Replace `token` with your PSG Self Serve generated API token
* Replace `host` with server host value
* Replace `port` with server port value
* Replace `useTls` with boolean value

```java
public NativeAssetsMultisigApi nativeAssetsMultisigApi() {
        return NativeAssetsMultisigApiBuilder.create()
        .withClientId(clientId)
        .withApiKey(token)
        .withHost(host)
        .withPort(port)
        .withUseTls(useTls)
        .build();

        }
```

### Create the Policy which allows to burn/mint the holders of secret keys associated with provided public keys
* Replace `policyName` with arbitrary policy name
* Replace `hexPubKey` public key in hex format. The policy allows only the holder of associated `secret key` to the provided public key to mint/burn
* Replace `beforeSlot` - optional - if specified it defines the `start` of time window in which the policy allows to burn/mint 
* Replace `afterSlot` - optional - if specified it defines the `end` of time window in which the policy allows to burn/mint
```java
    public CompletionStage<Policy> createPolicyWithPublicKeys() throws CborSerializationException {
        String policyName = "policy";
        String hexPubKey1 = "ac3c4";
        String hexPubKey1 = "bd4d5";
        Optional<Integer> beforeSlot = Optional.of(3);
        Optional<Integer> afterSlot = Optional.of(10);
        VerificationKey pubKey = VerificationKey.create(HexUtil.decodeHexString(hexPubKey));

        return nativeAssetsMultisigApi.createPolicy(policyName, beforeSlot, afterSlot, Arrays.asList(pubKey))
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }
```
### Create the Policy with secret keys
* Replace `policyName` with arbitrary policy name
* Replace `hexSecKey` the secret key in hex format. The policy allows only the holder of this key to mint/burn
* Replace `beforeSlot` - optional - if specified it defines the `start` of time window in which the policy allows to burn/mint
* Replace `afterSlot` - optional - if specified it defines the `end` of time window in which the policy allows to burn/mint
```java
    public CompletionStage<Policy> createPolicyWithSecKeys() throws CborSerializationException {
        String policyName = "policy";
        String hexSecKey = "ac3c4";
        Optional<Integer> beforeSlot = Optional.of(3);
        Optional<Integer> afterSlot = Optional.of(10);
        SecretKey secKey = SecretKey.create(HexUtil.decodeHexString(hexSecKey));

        return nativeAssetsMultisigApi.createPolicyUsingPrivateKeys(policyName, beforeSlot, afterSlot, Arrays.asList(secKey))
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }
```
### Get the Policy by policy id
```java
    public CompletionStage<Policy> getPolicyById() {
        String policyId = "policyId";
        return nativeAssetsMultisigApi.getPolicyById(policyId)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }
```
### Get the Policy by policy name
```java
    public CompletionStage<Policy> getPolicyByName() {
        String policyByName = "PolicyByName";
        return nativeAssetsMultisigApi.getPolicyByName(policyByName)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new NativeAssetException(response.getProblem());
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
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }
```
### Create mint transaction for NFT
```java
    public CompletionStage<String> createMintTransaction() {
        String assetName = "assetName";
        String policyId = "policyId";
        String paymentAddress = "ad23424a23424";
        String mintTargetAddress = "ad23424a23424";

        Nft nft = Nft$.MODULE$.defaultInstance()
                .withName(assetName)
                .withAssetName(assetName);

        AddressedNft addressedNft = AddressedNft$.MODULE$.defaultInstance()
                .withNft(nft)
                .withAddress(mintTargetAddress);

        return nativeAssetsMultisigApi.createMintTransaction(policyId, paymentAddress, addressedNft)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getTx().tx();
                    else {
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }
```
### Create mint transaction native asset
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
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }
```
### Create transfer transaction
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
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }
```
### Create burn transaction
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
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }
```
### Add witness with secret key
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
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }
```
### Add witness with public key and signature
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
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }
```
### Get Transaction by ID
```java
    public CompletionStage<Long> getTx() throws CborSerializationException {
        String txId = "ab2343245";

        return nativeAssetsMultisigApi.getTx(txId)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getTx().confirmations();
                    else {
                        throw new NativeAssetException(response.getProblem());
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
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }
```
### List all Transactions belonging to policy with ID
```java
    public CompletionStage<List<String>> listTxsByPolicyId()  {
        String policyId = "ab2343245";

        return nativeAssetsMultisigApi.listTxs(policyId)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return JavaConverters.asJava(response.transactions()).stream().map(ur -> ur.txId()).collect(Collectors.toList());
                    else {
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }
```
### List all witnesses
```java
    public CompletionStage<List<String>> listWitnesses()  {
        String txId = "ab2343245";

        return nativeAssetsMultisigApi.listWitnesses(txId)
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return  JavaConverters.asJava(response.verKeyHashes());
                    else {
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }

```
### Submit the transaction to the blockchain
```java
    public CompletionStage<String> sendTransaction()  {
        String txId = "ab2343245";
        Integer depth = 4;
        CompletableFuture<String> completableFuture = new CompletableFuture();
        nativeAssetsMultisigApi.sendTransaction(txId, depth, streamObserver(completableFuture, (r -> r.getSendTx().toString())));
        return completableFuture;
    }
```

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
```java

    public Address generateAddress() throws CborSerializationException {
        String hexPubKey = "ac3c4";
        return nativeAssetsMultisigApi.generateAddress(VerificationKey.create(HexUtil.decodeHexString(hexPubKey)), Networks.preprod());
    }
```

### Generate public key
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


