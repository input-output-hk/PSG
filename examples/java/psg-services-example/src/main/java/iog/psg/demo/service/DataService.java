package iog.psg.demo.service;

import com.google.protobuf.timestamp.Timestamp;
import io.grpc.stub.StreamObserver;
import iog.psg.demo.Observer;
import iog.psg.demo.model.TransactionStatus;
import iog.psg.service.metadata.client.Metadata;
import iog.psg.service.metadata.metadata_service.SubmitMetadataResponse;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

@Service
@Log
public class DataService {

    @Autowired
    private Metadata metadataService;

    public ResponseBodyEmitter getMetadata() {

        Timestamp endAt = Timestamp.apply(Instant.now());
        Timestamp startAt = Timestamp.apply(Instant.now().minus(24, ChronoUnit.HOURS));
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        metadataService.listMetadata(startAt, endAt, Observer.observer(emitter, response ->
                TransactionStatus.builder()
                        .id(response.getTxStatus().txId())
                        .state(response.getTxStatus().txState().toString())
                        .metadata(response.getTxStatus().getMetadata().toString())
                        .errorCode(response.getProblem().code())
                        .errorMessage(response.getProblem().msg())
                        .build()
        ));
        return emitter;
    }

    public ResponseBodyEmitter submitMetadata(String metadata) {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        metadataService.submitMetadata(" { \"123456789\": { \"string\": \"" + metadata + "\" } }", Observer.observer(emitter, response ->
                        TransactionStatus.builder()
                                .id(response.getTxStatus().txId())
                                .state(response.getTxStatus().txState().toString())
                                .metadata(response.getTxStatus().getMetadata().toString())
                                .errorCode(response.getProblem().code())
                                .errorMessage(response.getProblem().msg())
                                .build()


        ));
        return emitter;
    }


}
