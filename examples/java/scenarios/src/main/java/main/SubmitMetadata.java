package main;

import io.grpc.stub.StreamObserver;
import iog.psg.service.metadata.SubmitMetadataResponse;
import iog.psg.service.metadata.client.MetadataBuilder;

public class SubmitMetadata {
    public static void main(String[] args) throws InterruptedException {
        String apiKey = ""; // apiKey
        String clientId = ""; // clientId

        //client setup
        var client = MetadataBuilder
                .create(apiKey, clientId)
                .build();

        //consuming results
        var so = new StreamObserver<SubmitMetadataResponse>() {
            public void onNext(SubmitMetadataResponse o) {
                System.out.println("got transaction status" + o.getTxStatus());
                System.out.println("got problem" + o.getProblem());
            }

            public void onError(Throwable throwable) {
                System.out.println("got an error");
            }

            public void onCompleted() {
                System.out.println("finished");
            }
        };

        //call
        client.submitMetadata("{\"42\": \"text to store on chain\"}", so);

        //wait for async call to finish
        Thread.sleep(10 * 1000L);
    }
}
