package main;

import io.grpc.stub.StreamObserver;
import iog.psg.service.metadata.client.MetadataBuilder;
import iog.psg.service.metadata.TransactionMetadataResponse;

public class GetMetadata {
    public static void main(String[] args) throws InterruptedException {
        //client setup
        var client = MetadataBuilder
                .create("key", "clientId")
                .build();

        //consuming results
        var so = new StreamObserver<TransactionMetadataResponse>() {
            public void onNext(TransactionMetadataResponse o) {
                System.out.println("got " + o.getMetadata());
            }

            public void onError(Throwable throwable) {
                System.out.println("got an error");
            }

            public void onCompleted() {
                System.out.println("finished");
            }
        };

        //call
        client.transactionMetadata("transaction_hash", so);

        //wait for async call to finish
        Thread.sleep(10 * 1000L);
    }
}
