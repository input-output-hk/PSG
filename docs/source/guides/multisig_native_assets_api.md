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
  rpc sendTx(SendTxRequest) returns (stream SendTxResponse);
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
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.) [new_user_guide](./new_user_guide.md)
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
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.) [new_user_guide](./new_user_guide.md)
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
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.) [new_user_guide](./new_user_guide.md)
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
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.) [new_user_guide](./new_user_guide.md)
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
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.) [new_user_guide](./new_user_guide.md)
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
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.) [new_user_guide](./new_user_guide.md)

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
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.) [new_user_guide](./new_user_guide.md)

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
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.) [new_user_guide](./new_user_guide.md)

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
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.) [new_user_guide](./new_user_guide.md)

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
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.) [new_user_guide](./new_user_guide.md)

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
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.) [new_user_guide](./new_user_guide.md)

#### ListWitnessesResponse
- In the case of `success` the call will the list of `hashKeys` associated with `transaction` id
- In the case of a `problem`, that problem is described in the `problem` field of the response.


### Send the Transaction
```
message SendTxRequest {
  string txId = 1;
  iog.psg.service.common.CredentialsMessage credentials = 2;
  int64 depth = 3;
}

message SendTxResponse {
  message TxSubmitted {}

  oneof result {
    AppError problem = 1;
    SendTx sendTx = 2;
    TxSubmitted submitted = 3;
  }
}

```
#### SendTxRequest
- **txId** (the transaction id)
- **credentials** (these are the credentials generated by the PSG Services Self Serve UI after the user has created a "log in" and paid for use of the services.) [new_user_guide](./new_user_guide.md)

#### SendTxResponse
- In the case of `success` the call will return `TxSubmitted` as a first element of the stream, which will be followed by `SendTx` objects. The `TxSubmitted` is indicator that transaction has been submitted. The `SendTx` object contains information about Transaction and Native Asset.
- In the case of a `problem`, that problem is described in the `problem` field of the response.