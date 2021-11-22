package io.psg.demo.service;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import io.psg.demo.model.StoreResult;
import iog.psg.service.storeandhash.*;
import lombok.extern.java.Log;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log
public class StoreService {
    @Value("${s3.key}")
    private String key;
    @Value("${s3.secret}")
    private String secret;
    @Value("${s3.bucket}")
    private String bucket;
    @Value("${s3.region}")
    private String region;

    @GrpcClient("psg-services")
    private StoreAndHashServiceGrpc.StoreAndHashServiceStub storeAndHashService;

    private List<StoreResult> results = new ArrayList<>();

    public void storeFile(String path, String content) {
        List<StoreAndHashRequest> requests = new ArrayList<>();
        requests.add(StoreAndHashRequest.newBuilder()
                .setDetails(connectionDetails(path)).build());
        requests.add(StoreAndHashRequest.newBuilder()
                .setChunk(Chunk.newBuilder().setPart(ByteString.copyFromUtf8(content)))
                .build());

        StreamObserver<StoreAndHashResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(StoreAndHashResponse storeAndHashResponse) {
                results.add(StoreResult.builder()
                        .hash(storeAndHashResponse.getHash().getHashBase64())
                        .problem(storeAndHashResponse.getProblem())
                        .url(storeAndHashResponse.getUrl())
                        .build());
            }

            @Override
            public void onError(Throwable throwable) {
                log.info("Unable to get response message");
            }

            @Override
            public void onCompleted() {
                log.info("Completed");
            }
        };

        StreamObserver<StoreAndHashRequest> requestObserver = storeAndHashService.storeAndHashHttp(responseObserver);
        try {
            for (StoreAndHashRequest r : requests) {
                requestObserver.onNext(r);
            }
        } catch (RuntimeException ex) {
            requestObserver.onError(ex);
        }
        requestObserver.onCompleted();
    }

    public List<StoreResult> getResults() {
        return results;
    }

    private UploadDetails connectionDetails(String path) {
        return UploadDetails.newBuilder()
                .setAws(awsCredentials())
                .setPath(path).build();
    }

    private AwsCredentials awsCredentials() {
        return AwsCredentials.newBuilder()
                .setKeyId(key)
                .setKeySecret(secret)
                .setBucket(bucket)
                .setRegion(region)
                .build();
    }
}
