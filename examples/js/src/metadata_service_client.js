const CommonMessages = require('./common_pb.js');
const { loggingStreamHandler } = require('./util.js');
const MetadataMessages = require('./metadata_service_pb.js');
const MetadataService = require('./metadata_service_grpc_pb.js');
const grpc = require('@grpc/grpc-js');

const metadataSvc = new MetadataService.MetadataServiceClient(
    'psg-testnet.iog.services:2001',
    grpc.credentials.createSsl());

const credentials = new CommonMessages.CredentialsMessage()
    .setClientId("your_psg_username")
    .setApiToken("your_api_token");

// List Metadata Request
loggingStreamHandler(
    metadataSvc.listMetadata(
        new MetadataMessages
            .ListMetadataRequest()
            .setCredentials(credentials)
    )
);

// Submit Metadata Request
loggingStreamHandler(
    metadataSvc.submitMetadata(
        new MetadataMessages
            .SubmitMetadataRequest()
            .setCredentials(credentials)
            .setMetadata(JSON.stringify(
                { "17329656595257689515": { "string": "your metadata" } }
            ))
    )
);
