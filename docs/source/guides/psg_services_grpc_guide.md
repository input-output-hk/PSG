# PSG Services - gRPC API configuration

PSG Services are implemented as gRPC services and hosted by IOG; this describes how to use the services.

To discuss hosting these services, contact PSG [(enterprise.solutions@iohk.io)](mailto:enterprise.solutions@iohk.io)

For test and development purposes, we provide a testnet facing version of the services.

## **Steps:**
1. Get proto files from [PSG repository](https://github.com/input-output-hk/PSG/tree/develop/protos)
2. Generate gRPC clients with your preferred [client generator](https://grpc.io/docs/languages/), e.g. [akka-grpc](https://doc.akka.io/docs/akka-grpc/current/index.html) or [scalapb](https://github.com/scalapb/ScalaPB)
3. Implement clients for one of the chosen services: Metadata, StoreAndHash or Native Assets

## Connection Details

**Note:** Make sure that you use **secure connection (TLS = true)** for any request to PSG Services gRPC API.

### Cardano Testnet

#### PSG Services
- **host:** psg-testnet.iog.services
- **port:** 2001
- **TLS** true
- **client_id:** your username from [PSG Self Serve UI](https://psg-testnet.iog.services)
- **api_token:** token, generated at [API Token](https://psg-testnet.iog.services/apitokens) page

#### Native Assets Service

- **host:** psg-testnet.iog.services
- **port:** 2002
- **TLS** false
- **client_id:** your username from [PSG Self Serve UI](https://psg-testnet.iog.services)
- **api_token:** token, generated at [API Token](https://psg-testnet.iog.services/apitokens) page

### Cardano Mainnet

#### PSG Services
- **host:** psg.iog.services
- **port:** 2001
- **TLS** true
- **client_id:** your username from [PSG Self Serve UI](https://psg.iog.services)
- **api_token:** token, generated at [API Token](https://psg.iog.services/apitokens) page

#### Native Assets Service

- **host:** psg.iog.services
- **port:** 2002
- **TLS** false
- **client_id:** your username from [PSG Self Serve UI](https://psg-testnet.iog.services)
- **api_token:** token, generated at [API Token](https://psg-testnet.iog.services/apitokens) page