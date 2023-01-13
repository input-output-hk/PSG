package main;

import com.google.protobuf.Struct;
import iog.psg.client.nativeassets.NativeAssetsBuilder;
import iog.psg.service.nativeassets.AirDrop;
import iog.psg.service.nativeassets.NativeAssetId;
import iog.psg.service.nativeassets.TimeBounds;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

public class Airdrops {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //client setup
        var client = NativeAssetsBuilder
                .create("key", "clientId")
                .build();

        //if you do not have policy / asset, create them with this calls:
        //you have to transfer ADA to your policy address
//        var policy = client.createPolicy("demo policy", TimeBounds.newBuilder().build())
//                .toCompletableFuture().get().getPolicy();
//        System.out.println(policy.getPolicyId());
//        System.out.println(policy.getPaymentAddress());

        //native assets for airdrop
        var nativeAssets = List.of(AirDrop.newBuilder()
                .setPolicyId("policy_id")
                .setAssetName("youtNameHere")
                .setAddress("address_to_send_asset")
                .setAmountToAirDrop(1L)
                .build());

        //call
        var batchId = client.sendAirDropBatch(Struct.newBuilder().build(), nativeAssets)
                .toCompletableFuture()
                .get()
                .getBatchId();

        //check status
        for (int nbr = 0; nbr < 20; nbr++) {
            Thread.sleep(5 * 1000L);
            var batchStatus = client.getAirDropStatus(batchId).toCompletableFuture().get();
            System.out.println(batchStatus);
        }
    }
}
