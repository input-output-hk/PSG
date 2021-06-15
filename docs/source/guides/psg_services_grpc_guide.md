# PSG Services - gRPC API guide

### **Steps:**
1. Get proto files from [PSG repository](https://github.com/input-output-hk/PSG/tree/master/protos)
2. Generate gRPC clients with any suitable [libraries](https://grpc.io/docs/languages/), e.g. [akka-grpc](https://doc.akka.io/docs/akka-grpc/current/index.html) or [scalapb](https://github.com/scalapb/ScalaPB)
3. Implement clients for one of the chosen services: Metadata service, AuthMailMetadata service, StoreAndHash service

### **Credentials**

**Current version of PSG Services is using Cardano testnet**.  

- You **can specify any client_id or api_token** in gRPC requests. (request validation disabled)

**When Cardano mainnet version will be turned on - you may need to specify your credentials:**
- **client_id:** your username from [PSG Self Serve UI](https://prod.iog.services)
- **api_token:** token, generated at [API Token](https://prod.iog.services/apitokens) page

### **Connection details:**

Cardano testnet:
- **host:** http://ec2-3-135-64-128.us-east-2.compute.amazonaws.com
- **port:** 2000

Cardano mainnet:
- **host:** TBD
- **port:** 2000
