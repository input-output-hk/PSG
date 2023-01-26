# Multisig Native Assets CLI - User Guide

This document describes usage of Multisig Native Assets Client.  

You can read more about native assets in the [Cardano documentation](https://docs.cardano.org/native-tokens/learn).

Native Assets Client wraps the work of **minting**, **transferring**, and **burning** native assets 
on the Cardano blockchain the form of a java command-line tool. It also support the **minting** and **burning** with multiple signatures

## Installation:
- Download the latest version from the [repository](https://psg-releases.s3.us-east-2.amazonaws.com/native-assets-client-0.3.1.zip)
- Unzip it into a folder
- Set `clientId` and`apiKey` at `/conf/application.conf` to the ones from Self Serve UI
- Set `host`, `port` for testnet or mainnet environments
- Make native asset client executable
```bash
sudo chmod +x /bin/native-assets-client
```
- Run client from the command line
```bash
./bin/native-assets-client -multisig -COMMAND
```

where COMMAND - one of the supported ones
## Supported commands
- `createPolicy` - Creates the policy context for minting, burning etc
- `getPolicy` - Gets the policy by id
- `listPolicies` - Lists the existing policies contexts
- `mint` - Mints specific amount of new native assets with multisignature support
- `burn` - Burns specific amount of native assets with multisignature support
- `transfer` - Transfers new native assets to an address
- `addWitness` - Add signature to the specified transaction id
- `listWitness` - List all signatures that were added to the specified transaction id
- `sendTx` - Sends transaction to the blockchain.
- `getTx` -  Get transaction stored in backend associated with policy Id
- `listTxs` - List all transactions that are stored in backend
- `genVerKey` -  Generates verification key
- `genKeys` -  Generates an ED25519 key pair
- `genAddress` - Generates a cardano enterprise address from public key


## Multisig Native Asset Client - API

### Create new policy
```bash
# Create policy associated with public keys
./bin/native-assets-client -multisig  -createPolicy -name MyDemoPolicy -policyIdOut ./policyId -verKeyFiles "./user1.vkey ./user2.vkey"
# Create policy associated with private keys 
./bin/native-assets-client -multisig  -createPolicy -name MyDemoPolicy -policyIdOut ./policyId -signingKeyFiles "./user1.skey ./user2.skey"
```

### Check list of policies
```bash
./bin/native-assets-client -multisig -listPolicies
```

### Get policy
```bash
#Get Policy by Id
./bin/native-assets-client -multisig -getPolicy -policyId c86e802e283dca7e99227907ec585bc0ad93ee55e9a8b8d86c12f19d
#Get Policy by Name
./bin/native-assets-client -multisig -getPolicy -name MyDemoPolicy
```
### Mint Asset

```bash
# Mint native assets defined in metadata file
  ./bin/native-assets-client -multisig -mint -assetName MyDemoAsset -paymentAddress addr_testxxxxxxx -mintTargetAddressaddr_testxxxxxxx -policyId ./policyId -txIdOut ./txIdOut -txOut ./txOut -arbitraryMetadata "./metadataFile.json" -amount 100
# Mint NFTs
  ./bin/native-assets-client -multisig -mint -assetName MyDemoAsset -paymentAddress addr_testxxxxxxx -mintTargetAddressaddr_testxxxxxxx -policyIdFile ./policyId -txIdOut ./txIdOut -txOut ./txOut
```

### Burn Asset
```bash
# Burn `assetName` from `targetAddress` of specified `amount` associated with policy and transaction id loaded from files
  ./bin/native-assets-client -multisig -burn -assetName MyDemoAsset -mintTargetAddressaddr_testxxxxxxx -policyIdFile ./policyId -txIdOut ./txIdOut -txOut ./txOut -amount 10 
```

### Transfer Asset
```bash
# Transfer `assetName` from `fromAddress` to `toAddress` of specified `amount` associated with policy and transaction id loaded from files
  ./bin/native-assets-client -multisig -transfer -fromAddress addr_testxxxxxxx -toAddress addr_testyyyyyyy -policyIdFile ./policyId -txIdOut ./txIdOut -txOut ./txOut -tokenAmount 10
```
### Add Witness
```bash
  # Add witness from a `private key file` to the transaction with `id` loaded from file 
  ./bin/native-assets-client -multisig -addWitness -txIdFile ./txId -txFile ./txOut -signingKeyFile ./user1.skey
  # Add witness as a `param` to the transaction with `id` loaded from file
  ./bin/native-assets-client -multisig -addWitness -txIdFile ./txId -txFile ./txOut -signingKey cdb9ea82555a6f10294963705342538a8cab22ea4a40a2d2ca59e6da75b42dd6
  
  # Add witness where private key is derived from a `public key file` to the transaction with `id` loaded from file 
  ./bin/native-assets-client -multisig -addWitness -txIdFile ./txId -txFile ./txOut -verKeyFile ./user1.vkey
  # Add witness where private key is derived from provided `public key` to the transaction with `id` loaded from file
  ./bin/native-assets-client -multisig -addWitness -txIdFile ./txId -txFile ./txOut -verKey abe17d93a342dc4f8dadef5b40ca5f32b504de815cf0f086af1fd0cbdaf04b54
  
```

### List Witnesses
```bash
  # List all witnesses associated to the transaction with `id` defined in param 
  ./bin/native-assets-client -multisig -listWitnesses -txId 493eeee2f64fc476fbcd70e614288fe692fbc1b428f6fc1a468b08f8678e0d19
  # List all witnesses associated to the transaction with `id` defined in file
  ./bin/native-assets-client -multisig -listWitnesses -txIdFile ./txIdOut
```
### Send Transaction
```bash
# Send the transaction to the blockchain  associated with the Id loaded from file
  ./bin/native-assets-client -multisig -sendTx -txIdFile ./txIdFile
```

### Get Transaction
```bash
# Get the transaction associated with the Id loaded from file
  ./bin/native-assets-client -multisig -getTx -txIdFile ./txIdFile
```

### List Transactions
```bash
   # List Transactions associated with `Policy` loaded from file
  ./bin/native-assets-client -multisig -listTxs -policyIdFile ./policyIdfile
  # List all Transactions
  ./bin/native-assets-client -multisig -listTxs 
```


### Generate Keys
```bash
  # Generate and store public key and private key to associated files
  ./bin/native-assets-client -multisig -genKeys -verKeyOut my.vkey -signKeyOut my.skey
```

### Generate Public Keys
```bash
  # Generate and store public key to the output file
  ./bin/native-assets-client -multisig -genVerKey -out my.vkey
```

### Generate address
```bash
  # Generate and store the Address to the output file
  ./bin/native-assets-client -multisig -genAddress -out ./my.addr
```

