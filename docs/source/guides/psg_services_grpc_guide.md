# PSG Services - gRPC API guide

PSG Services are implemented as gRPC servers and hosted by IOG; this describes how to use the services.

To discuss hosting these services, contact PSG [(enterprise.solutions@iohk.io)](mailto:enterprise.solutions@iohk.io)

For test and development purposes, we provide a testnet facing version of the services.

### **Steps:**
1. Get proto files from [PSG repository](https://github.com/input-output-hk/PSG/tree/develop/protos)
2. Generate gRPC clients with your preferred [client generator](https://grpc.io/docs/languages/), e.g. [akka-grpc](https://doc.akka.io/docs/akka-grpc/current/index.html) or [scalapb](https://github.com/scalapb/ScalaPB)
3. Implement clients for one or more services: [Metadata service](https://psg-services.readthedocs.io/en/latest/guides/metadata_service_guide.html) and/or [StoreAndHash](https://psg-services.readthedocs.io/en/latest/guides/store_and_hash_service_guide.html) service


### Cardano Testnet

#### **Connection Details:**

- **host:** psg-testnet.iog.services
- **port:** 2001
- **TLS** true

- **client_id:** your username from [PSG Self Serve UI](https://psg-testnet.iog.services)
- **api_token:** token, generated at [API Token](https://psg-testnet.iog.services/apitokens) page


### Cardano Mainnet

#### **Connection Details:**
- **host:** psg.iog.services
- **port:** 2001
- **TLS** true

- **client_id:** your username from [PSG Self Serve UI](https://psg.iog.services)
- **api_token:** token, generated at [API Token](https://psg.iog.services/apitokens) page