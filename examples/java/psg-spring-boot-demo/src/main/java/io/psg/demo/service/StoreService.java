package io.psg.demo.service;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import io.psg.demo.model.StoreResult;
import iog.psg.service.common.CredentialsMessage;
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
    @Value("${clientId}")
    private String clientId;
    @Value("${token}")
    private String token;

    @GrpcClient("psg-services")
    private StoreAndHashServiceGrpc.StoreAndHashServiceStub storeAndHashService;

    private List<StoreResult> results = new ArrayList<>();

    public void storeAtAws(String path, String content) {
        List<StoreAndHashRequest> requests = new ArrayList<>();
        requests.add(StoreAndHashRequest.newBuilder()
                .setDetails(awsConnectionDetails(path)).build());
        requests.add(StoreAndHashRequest.newBuilder()
                .setChunk(Chunk.newBuilder().setPart(ByteString.copyFromUtf8(content)))
                .build());

        StreamObserver<StoreAndHashResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(StoreAndHashResponse storeAndHashResponse) {
                results.add(StoreResult.builder()
                        .hash(storeAndHashResponse.getHash().getHashBase64())
                        .errorCode(storeAndHashResponse.getProblem().getCode())
                        .errorMessage(storeAndHashResponse.getProblem().getMsg())
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

    public void storeAtIpfs(String host, int port, String content) {
        List<StoreAndHashIpfsRequest> requests = new ArrayList<>();
        requests.add(StoreAndHashIpfsRequest.newBuilder()
                .setDetails(ipfsConnectionDetails(host, port)).build());
        requests.add(StoreAndHashIpfsRequest.newBuilder()
                .setChunk(Chunk.newBuilder().setPart(ByteString.copyFromUtf8(content)))
                .build());

        StreamObserver<StoreAndHashResponse> responseObserver = new StreamObserver<>() {
            @Override
            public void onNext(StoreAndHashResponse storeAndHashResponse) {
                results.add(StoreResult.builder()
                        .hash(storeAndHashResponse.getHash().getHashBase64())
                        .errorCode(storeAndHashResponse.getProblem().getCode())
                        .errorMessage(storeAndHashResponse.getProblem().getMsg())
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

        StreamObserver<StoreAndHashIpfsRequest> requestObserver = storeAndHashService.storeAndHashIpfs(responseObserver);
        try {
            for (StoreAndHashIpfsRequest r : requests) {
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

    private UploadDetails awsConnectionDetails(String path) {
        return UploadDetails.newBuilder()
                .setCredentials(getCredentials())
                .setAws(awsCredentials())
                .setPath(path).build();
    }

    private IpfsUploadDetails ipfsConnectionDetails(String host, int port) {
        return IpfsUploadDetails.newBuilder()
                .setCredentials(getCredentials())
                .setIpfsAddress(IpfsAddress.newBuilder().setHost(host).setPort(port))
                .build();
    }

    private AwsCredentials awsCredentials() {
        return AwsCredentials.newBuilder()
                .setKeyId(key)
                .setKeySecret(secret)
                .setBucket(bucket)
                .setRegion(region)
                .build();
    }

    private CredentialsMessage getCredentials() {
        return CredentialsMessage.newBuilder()
                .setClientId(clientId)
                .setApiToken(token)
                .build();
    }
}
