package main;

import io.grpc.stub.StreamObserver;
import iog.psg.service.storeandhash.client.AwsConf;
import iog.psg.service.storeandhash.client.StoreAndHashBuilder;
import iog.psg.service.storeandhash.storeandhash_service.StoreAndHashResponse;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class StoreAndHash {

    public static void main(String[] args) throws InterruptedException {
        //data
        String metadata = "{\n" +
                "    \"81\": {\n" +
                "      \"name\": \"MultiSample\",\n" +
                "      \"randomKey\": 42,\n" +
                "      \"arrTest\": [\n" +
                "        {\"1\": 3},\n" +
                "        {\"2\": \"tada\"}\n" +
                "      ],\n" +
                "      \"mapTest\": {\n" +
                "        \"eight\": \"eight\",\n" +
                "        \"nested\": {\n" +
                "          \"nestedValue\": \"I'm not empty\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }";

        //AWS setup
        String pathToUpload = "__";
        String awsKey = "__";
        String awsSecret = "__";
        String awsBucket = "__";
        String awsRegion = "__";
        AwsConf awsConf = new AwsConf(awsKey, awsSecret, awsBucket, awsRegion);

        String apiKey = ""; // apiKey
        String clientId = ""; // clientId

        //client setup
        var storeAndHashApi = StoreAndHashBuilder
                .create(apiKey, clientId)
                .build();

        //consuming results
        var so = new StreamObserver<StoreAndHashResponse>() {
            @Override
            public void onNext(StoreAndHashResponse o) {
                System.out.println("got url" + o.getUrl());
                System.out.println("got problem " + o.getProblem());
                System.out.println("got hash " + o.getHash());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("got an error");
            }

            @Override
            public void onCompleted() {
                System.out.println("finished");
            }
        };

        //call
        storeAndHashApi.storeAndHashHttp(pathToUpload, metadata.getBytes(StandardCharsets.UTF_8), awsConf, so);

        //wait for async call to finish
        Thread.sleep(30 * 1000L);
    }

}
