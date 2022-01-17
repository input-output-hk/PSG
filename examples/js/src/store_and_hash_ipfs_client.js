const CommonMessages = require('./common_pb.js');
const { loggingStreamHandler } = require('./util.js');
const StoreAndHashMessages = require('./storeandhash-service_pb.js');
const StoreAndHashService = require('./storeandhash-service_grpc_pb.js');
const grpc = require('@grpc/grpc-js');

const storeAndHashSvc = new StoreAndHashService.StoreAndHashServiceClient(
    'psg-testnet.iog.services:2001',
    grpc.credentials.createSsl()
);

// Replace CLIENT_ID and API_TOKEN with your PSG Self Serve UI account values
const credentials = new CommonMessages.CredentialsMessage()
    .setClientId("CLIENT_ID")
    .setApiToken("API_TOKEN");

// Replace IPFS_HOST and IPFS_PORT with your chosen IPFS node ip address and port
const IPFS_PORT = 5001
const storeAndHashIpfsRequests = [
    new StoreAndHashMessages.StoreAndHashIpfsRequest()
        .setDetails(
            new StoreAndHashMessages.IpfsUploadDetails()
            .setIpfsaddress(new StoreAndHashMessages.IpfsAddress()
            .setHost("IPFS_HOST").setPort(IPFS_PORT))
            .setCredentials(credentials)
        ),

    new StoreAndHashMessages
        .StoreAndHashIpfsRequest()
        .setChunk(new StoreAndHashMessages.Chunk().setPart("Demo Data To Be Saved At IPFS"))
]

const storeAndHashIpfsCall = storeAndHashSvc.storeAndHashIpfs();
loggingStreamHandler(storeAndHashIpfsCall);
storeAndHashIpfsRequests.forEach(req => storeAndHashIpfsCall.write(req));
storeAndHashIpfsCall.end();