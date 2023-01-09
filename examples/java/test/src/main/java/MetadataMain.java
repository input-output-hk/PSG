import com.google.protobuf.timestamp.Timestamp;
import io.grpc.stub.StreamObserver;
import iog.psg.service.metadata.client.MetadataAsync;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MetadataMain {
    MetadataAsync metadataApi = new MetadataAsync("API_TOKEN", "CLIENT_ID", "localhost", 2000);



    public CompletionStage<String> submitMetadata() {
        String metadata= "MyMetadata";
        CompletableFuture<String> completableFuture = new CompletableFuture();
        metadataApi.submitMetadata(metadata, streamObserver(completableFuture, r -> r.getTxStatus().toString()));
        return completableFuture;

    }

    public CompletionStage<String> transactionMetadata() {
        String txHash = "234234acb";
        CompletableFuture<String> completableFuture = new CompletableFuture();
        metadataApi.transactionMetadata(txHash, streamObserver(completableFuture, r -> r.getMetadata().toString()));
        return completableFuture;

    }

    public CompletionStage<String> listMetadata() {
        Timestamp startAt = Timestamp.of(100000000, 0);
        Timestamp endAt = Timestamp.of(200000000, 0);
        CompletableFuture<String> completableFuture = new CompletableFuture();
        metadataApi.listMetadata(startAt, endAt, streamObserver(completableFuture, r -> r.getTxStatus().toString()));
        return completableFuture;

    }

    private <V> StreamObserver<V> streamObserver(CompletableFuture<String> completableFuture,
                                                 Function<V, String> converter) {
        return new StreamObserver<V>() {
            List<String> buffer = new ArrayList();

            @Override
            public void onNext(V res) {
                buffer.add(converter.apply(res));
            }

            @Override
            public void onError(Throwable throwable) {
                buffer.add(throwable.toString());
            }

            @Override
            public void onCompleted() {
                String res = buffer
                        .stream()
                        .collect(Collectors.joining("\n"));
                completableFuture.complete(res);
            }
        };
    }
}
