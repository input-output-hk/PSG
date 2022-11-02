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
### Native Asset service demo
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
   
  
