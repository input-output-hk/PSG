const CommonMessages = require('./common_pb.js');
const { loggingStreamHandler } = require('./util.js');
const StoreAndHashMessages = require('./storeandhash-service_pb.js');
const StoreAndHashService = require('./storeandhash-service_grpc_pb.js');
const grpc = require('@grpc/grpc-js');

const storeAndHashSvc = new StoreAndHashService.StoreAndHashServiceClient(
    'psg-testnet.iog.services:2001',
    grpc.credentials.createSsl()
);

// Replace AWS_ACCESS_KEY, AWS_SECRET_KEY, AWS_S3_BUCKET and BUCKET_REGION with your S3 account values and replace demo-file-path with your desired path.
const reqs = [
    new StoreAndHashMessages.StoreAndHashRequest()
        .setDetails(
            new StoreAndHashMessages.UploadDetails()
                .setAws(new StoreAndHashMessages.AwsCredentials()
                    .setKeyid("AWS_ACCCESS_KEY")
                    .setKeysecret("AWS_SECRET_KEY")
                    .setBucket("AWS_S3_BUCKET")
                    .setRegion("BUCKET_REGION")
                )
                .setPath("demo-file-path")
        ),

    new StoreAndHashMessages
        .StoreAndHashRequest()
        .setChunk(new StoreAndHashMessages.Chunk().setPart("Demo Data To Be Saved"))
]

const storeAndHashCall = storeAndHashSvc.storeAndHashHttp();
loggingStreamHandler(storeAndHashCall);
reqs.forEach(req => storeAndHashCall.write(req));
storeAndHashCall.end();

