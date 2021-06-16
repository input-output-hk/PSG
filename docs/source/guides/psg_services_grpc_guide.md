# PSG Services - gRPC API guide

PSG Services are implemented as gRpc servers and hosted by IOG, this describes how to get started using the services.

To discuss hosting these services contact PSG [(enterprise.solutions@iohk.io)](mailto:enterprise.solutions@iohk.io)

For test and development purposes we provide a testnet facing version of the services.
 
### **Steps:**
1. Get proto files from [PSG repository](https://github.com/input-output-hk/PSG/tree/develop/protos)
2. Generate gRPC clients with your preferred [client generator](https://grpc.io/docs/languages/), e.g. [akka-grpc](https://doc.akka.io/docs/akka-grpc/current/index.html) or [scalapb](https://github.com/scalapb/ScalaPB)
3. Implement clients for one or more services: [Metadata service](https://github.com/input-output-hk/PSG/blob/develop/protos/cardano-metadata-service/protobuf/README.md) and/or [StoreAndHash](https://github.com/input-output-hk/PSG/tree/develop/protos/store-and-hash-service/protobuf/README.md) service


### Cardano Testnet  

#### **Connection Details:**

- **host:** psg-aggregate-services-testnet.iog.solutions
- **port:** 2000

- You **can specify any client_id or api_token** in gRPC requests. (request validation disabled)

### Cardano Mainnet

#### **Connection Details:**
- **host:** psg-aggregate-services.iog.solutions
- **port:** 2000

- **client_id:** your username from [PSG Self Serve UI](https://prod.iog.services)
- **api_token:** token, generated at [API Token](https://prod.iog.services/apitokens) page

