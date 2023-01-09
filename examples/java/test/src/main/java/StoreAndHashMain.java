import io.grpc.stub.StreamObserver;
import iog.psg.service.storeandhash.client.AwsConf;
import iog.psg.service.storeandhash.client.StoreAndHash;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StoreAndHashMain {

    StoreAndHash storeAndHashApi = new StoreAndHash("API_TOKEN", "CLIENT_ID", "localhost", 2000);

    public CompletionStage<String> storeFileAtAws() {
        String path = "person";
        String fileContent = "{\"name\": \"John\"}";

        String awsKey = "23423";
        String awsSecret = "23423";
        String awsBucket = "23423";
        String awsRegion = "us‑east‑1";

        AwsConf awsConf = new AwsConf(awsKey, awsSecret, awsBucket, awsRegion);
        CompletableFuture<String> completableFuture = new CompletableFuture();

        storeAndHashApi.storeAndHashHttp(path, fileContent.getBytes(StandardCharsets.UTF_8), awsConf, streamObserver(completableFuture, r -> r.getHash().toString()));
        return completableFuture;
    }

    public CompletionStage<String> storeFileAtIPFS() {
        String fileContent = "{\"name\": \"John\"}";

        String host = "ipfs host";
        Integer port = 5001;
        CompletableFuture<String> completableFuture = new CompletableFuture();
        storeAndHashApi.storeAndHashIpfs(host, port, fileContent, streamObserver(completableFuture, r -> r.getHash().toString()));
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
    public static void main(String[] args) {

    }


}
