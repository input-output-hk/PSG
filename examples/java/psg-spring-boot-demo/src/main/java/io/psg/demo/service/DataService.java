package io.psg.demo.service;

import io.psg.demo.model.TransactionStatus;
import iog.psg.service.common.CredentialsMessage;
import iog.psg.service.metadata.*;
import lombok.extern.java.Log;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Log
public class DataService {
    @Value("${clientId}")
    private String clientId;
    @Value("${token}")
    private String token;

    @GrpcClient("psg-services")
    private MetadataServiceGrpc.MetadataServiceBlockingStub metadataService;

    public List<TransactionStatus> getMetadata() {
        ListMetadataRequest request = ListMetadataRequest.newBuilder()
                .setCredentials(getCredentials())
                .build();

        List<TransactionStatus> transactions = new ArrayList<>();

        Iterator<ListMetadataResponse> responses = metadataService.listMetadata(request);
        while (responses.hasNext()) {
            ListMetadataResponse response = responses.next();
            transactions.add(
                    TransactionStatus.builder()
                            .id(response.getTxStatus().getTxId())
                            .state(response.getTxStatus().getTxState().toString())
                            .metadata(response.getTxStatus().getMetadata().toString())
                            .errorCode(response.getProblem().getCode())
                            .errorMessage(response.getProblem().getMsg())
                            .build()
            );
        }
        return transactions;
    }

    public List<TransactionStatus> submitMetadata(String metadata) {
        SubmitMetadataRequest request = SubmitMetadataRequest.newBuilder()
                .setMetadata(" { \"123456789\": { \"string\": \"" + metadata + "\" } }")
                .setCredentials(getCredentials()).build();

        List<TransactionStatus> updates = new ArrayList<>();
        Iterator<SubmitMetadataResponse> responses = metadataService.submitMetadata(request);
        while (responses.hasNext()) {
            SubmitMetadataResponse response = responses.next();
            updates.add(TransactionStatus.builder()
                    .id(response.getTxStatus().getTxId())
                    .state(response.getTxStatus().getTxState().toString())
                    .metadata(response.getTxStatus().getMetadata().toString())
                    .errorCode(response.getProblem().getCode())
                    .errorMessage(response.getProblem().getMsg())
                    .build());
        }
        return updates;
    }

    private CredentialsMessage getCredentials() {
        return CredentialsMessage.newBuilder()
                .setClientId(clientId)
                .setApiToken(token)
                .build();
    }
}
