package main;

import com.google.protobuf.timestamp.Timestamp;
import io.grpc.stub.StreamObserver;
import iog.psg.service.metadata.client.Metadata;
import iog.psg.service.metadata.client.MetadataBuilder;
import iog.psg.service.metadata.ListMetadataResponse;

import java.time.Instant;

public class ListMetadata {
    public static void main(String[] args) throws InterruptedException {
        String apiKey = ""; // apiKey
        String clientId = ""; // clientId
        //client setup
        Metadata client = MetadataBuilder
                .create(apiKey, clientId)
                .build();

        //consuming results
        var so = new StreamObserver<ListMetadataResponse>() {
            public void onNext(ListMetadataResponse o) {

                System.out.println("I am here " + o.getTxStatus());
            }

            public void onError(Throwable throwable) {
                System.out.println("got an error");

            }

            public void onCompleted() {
                System.out.println("finished");
            }
        };

        //call
        var startAt = Timestamp.apply(Instant.now().minusSeconds(2 * 24 * 60 * 60));
        var endAt = Timestamp.apply(Instant.now());
        client.listMetadata(startAt, endAt, so);

        //wait for async call to finish
        Thread.sleep(10 * 1000L);
    }
}
