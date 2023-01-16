package main
import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.common.model.Networks;
import com.bloxbean.cardano.client.crypto.Keys;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import io.grpc.stub.StreamObserver;
import iog.psg.client.nativeassets.NativeAssetsApi;
import iog.psg.client.nativeassets.NativeAssetsBuilder;
import iog.psg.client.nativeassets.multisig.v1.NativeAssetsMultisigApi;
import iog.psg.service.nativeassets.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class MintBurnTransferCustodial {

    public static <E, R> StreamObserver<E> lastResult(CompletableFuture<R> result, Function<E, R> f) {
        return new StreamObserver<>() {
            ArrayList<R> buff = new ArrayList();

            @Override
            public void onNext(E event) {
                R converted = f.apply(event);
                System.out.println("Got interim response: " + converted);
                buff.add(converted);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Got error: " + throwable.getCause());
            }

            @Override
            public void onCompleted() {
                result.complete(buff.get(buff.size() - 1));
            }
        };

    }

    public static void main(String[] args) throws ExecutionException, CborSerializationException, InterruptedException {
        String apiKey = ""; // apiKey
        String clientId = ""; // clientId
        // Create the Api
        NativeAssetsApi api = NativeAssetsBuilder.create(apiKey, clientId).build();

        // create policy with name My Policy and
        TimeBounds timeBounds = TimeBounds
                .newBuilder()
                .setAfter(140215)
                .setBefore(348215)
                .build();

        // Create the policy with name `My Policy`
        CreatePolicyResponse response = api.createPolicy("My Policy", timeBounds)
                .toCompletableFuture()
                .get();

        String policyId = response.getPolicy().getPolicyId();
        Integer depth = 4;
        CompletableFuture<FundNativeAssetTransaction> fundResult = new CompletableFuture();
        // Funds the paymentAddress associated with policy with name `policyId`
        api.fundPolicyAddress(policyId, 10000000l, 4, lastResult(fundResult, r -> r.getFundTx()));

        String assetName = "MyAssetName";
        NativeAssetId nativeAssetId = NativeAssetId
                .newBuilder()
                .setName(assetName)
                .setPolicyId(policyId)
                .build();
        // Create the asset with the policy `policyId` and asset `assetName` on the server
        CreateNativeAssetResponse assetResponse = api.createAsset(nativeAssetId)
                .toCompletableFuture()
                .get();


        Map<String, Value> data = new HashMap<>();
        Struct struct = com.google.protobuf.Struct.newBuilder()
                .putAllFields(data)
                .build();
        //Mint `100` tokens of the native asset defined with `nativeAssetId`
        CompletableFuture<NativeAssetTransaction> mintResult = new CompletableFuture<>();
        api.mintAssetWithArbitraryMetadata(nativeAssetId, 100L, struct, 4, lastResult(mintResult, r -> r.getAssetTx()));

        //Burn `11` tokens of the native asset defined with `nativeAssetId`
        CompletableFuture<NativeAssetTransaction> burnResult = new CompletableFuture<>();
        api.burnAsset(nativeAssetId, 11l, depth, lastResult(burnResult, r -> r.getAssetTx()));

        String targetAddress = "addr_myaddress";
        //Transfer `13` tokens of the native asset defined with `nativeAssetId` to `targetTransferAddress`
        CompletableFuture<NativeAssetTransaction> transferResult = new CompletableFuture<>();
        api.transferAsset(nativeAssetId, targetAddress, 13L, depth, lastResult(transferResult, r -> r.getAssetTx()));
    }

}