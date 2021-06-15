# PSG Services - gRPC API guide

### **Steps:**
1. Get proto files from [PSG repository](https://github.com/input-output-hk/PSG/tree/develop/protos)
2. Generate gRPC clients with any suitable [libraries](https://grpc.io/docs/languages/), e.g. [akka-grpc](https://doc.akka.io/docs/akka-grpc/current/index.html) or [scalapb](https://github.com/scalapb/ScalaPB)
3. Implement clients for one of the chosen services: [Metadata service](https://github.com/input-output-hk/PSG/blob/develop/protos/cardano-metadata-service/protobuf/README.md) and/or [StoreAndHash](https://github.com/input-output-hk/PSG/tree/develop/protos/store-and-hash-service/protobuf/README.md) service

### PSG Services

### Cardano testnet  

#### **Connection details:**

- **host:** ec2-3-135-64-128.us-east-2.compute.amazonaws.com
- **port:** 2000

- You **can specify any client_id or api_token** in gRPC requests. (request validation disabled)

### Cardano mainnet

#### **Connection details:**
- **host:** psg-aggregate-services.iog.solutions
- **port:** 2000

- **client_id:** your username from [PSG Self Serve UI](https://prod.iog.services)
- **api_token:** token, generated at [API Token](https://prod.iog.services/apitokens) page

