# PSG Services - JAVA example (Spring Boot)

## Prerequisites
Install the following tools:

- [Java](https://www.oracle.com/java/technologies/downloads/)
- [Maven](https://maven.apache.org/)

**NOTE:** make sure, that **for development and testing purposes** you are using **[PSG Services - Testnet](https://psg-testnet.iog.services/)**

## How to run example service:

1. Make sure, that you have registered an account in [PSG Self Serve UI](https://psg.iog.services/), purchased a package and generated API Token

2. Replace CLIENT_ID and API_TOKEN with your PSG Self Serve UI account values in the src/resources/application.properties file:
```shell
clientId=CLIENT_ID
token=API_TOKEN
```
3. Run Spring Boot application by executing the following command in terminal:
```shell
$ ./mvnw spring-boot:run
```
4. Build fat jar to target/native-assets-0.0.1-SNAPSHOT.jar
```shell
$ mvn package
```
5. Run the fat jar
```shell
$ java -jar target/native-assets-0.0.1-SNAPSHOT.jar
```
### Native Asset service example
- Create Policy with `policyName`:
  - ```$ curl -X POST "http://localhost:8181/policies/{policyName}"```
- Get all Policies
  -  ```$ curl -X GET "http://localhost:8181/policies```
- Get Policy with `policyId`
  -  ```$ curl -X GET "http://localhost:8181/policies/{policyId}```
- Delete Policy with `policyId`
  - ```$ curl -X DELETE "http://localhost:8181/policies/{policyId}```



- Create Asset with `assetName` and `policyId`:
  - ```$ curl -X POST "http://localhost:8181/assets/{assetName}/policy/{policyId}"```
- Get all Assets
  -  ```$ curl -X GET "http://localhost:8181/assets```
- Get Asset with `assetName` and `policyId`
  -  ```$ curl -X GET "http://localhost:8181/assets/{assetName}/policy{policyId}```
- Delete Asset with `assetName` and `policyId`
  - ```$ curl -X DELETE "http://localhost:8181/assets/{assetName}/policy{policyId}```
- Mint Asset with `assetName` and `policyId`
  - ```curl -X POST "localhost:8181/mint" -H "Content-Type: application/json"  -d ' {"name": "${AssetName}","policyId": "${policyId}", "amount": 10, "depth": 3, "nfts": []}'  ```

### Multisig Native Asset service example
- Create Policy with `policyName` and `public key`:
- ```$ curl -X POST "http://localhost:8181/policies/{name}/vkey/{vkey}"```
- Create Policy with `policyName` and `secret key`:
- ```$ curl -X POST "http://localhost:8181/policies/{name}/skey/{sKey}"```
- Get Policy with `policyId`:
- ```$ curl -X GET "http://localhost:8181/policies/id/{policyId}"```
- Get Policy with `policyName`:
- ```$ curl -X GET "http://localhost:8181/policies/name/{policyName}"```
- Get All Policies:
- ```$ curl -X GET "http://localhost:8181/policies"```
- Create Mint Transaction for NFT  with json containing  `assetName` and `policyId`, addresses `paymentAddress` and `mintTargetAddress`
- ```$ curl -X POST "http://localhost:8181/mint/nft"  -H "Content-Type: application/json"  -d ' {"name": "${AssetName}","policyId": "${policyId}", "paymentAddress": "${paymentAddress}", "mintTargetAddress": "${mintTargetAddress}"}'```
- Create Mint Transaction for Asset with json containing `assetName` and `policyId`, addresses `paymentAddress` and `mintTargetAddress` and specified mint `amount`
- ```$ curl -X POST "http://localhost:8181/mint"  -H "Content-Type: application/json"  -d ' {"name": "${AssetName}","policyId": "${policyId}", "paymentAddress": "${paymentAddress}", "mintTargetAddress": "${mintTargetAddress}", "amount": 10}'```
- Create Burn Transaction for Asset  with json containing `assetName` and `policyId`, address `targetAddress` and specified burn `amount`
- ```$ curl -X POST "http://localhost:8181/burn"  -H "Content-Type: application/json"  -d ' {"name": "${AssetName}","policyId": "${policyId}", "targetAddress": "${targetAddress}", "amount": 10}'```
- Create Transfer Transaction for Asset with json containing `assetName` and `policyId`, addresses `fromAddress` and `targetAddress` and specified transfer `amount`
- ```$ curl -X POST "http://localhost:8181/transfer"  -H "Content-Type: application/json"  -d ' {"name": "${AssetName}","policyId": "${policyId}",  "fromAddress": "${fromAddress}", "targetAddress": "${targetAddress}", "amount": 10}'```
- Add Witness with private key `sKey` for transaction defined by `txId`
- ```$ curl -X POST "http://localhost:8181/witnesses/{txId/{sKey}"```
- Add Witness with public key `vKey` and signature `sig` for transaction defined by`txId`
- ```$ curl -X POST "http://localhost:8181/witnesses/{txId/{vKey}/{sig}"```
- List all Witnesses for transaction `txId`
- ```$ curl -X GET "http://localhost:8181/witnesses/{txId}"```
- List all Transactions
- ```$ curl -X GET "http://localhost:8181/txs"```
- Get the Transaction with `txId`
- ```$ curl -X GET "http://localhost:8181/txs/txId/{txid}"``` 
- Get the Transactions related to policy `policyId`
- ```$ curl -X GET "http://localhost:8181/txs/txId/{policyId}"```
- Submit the Transactions identified with `txId`
- ```$ curl -X POST "http://localhost:8181/txs/{txId}/submit"```

