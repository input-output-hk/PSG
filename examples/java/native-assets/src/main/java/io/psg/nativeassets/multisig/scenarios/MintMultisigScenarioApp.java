package io.psg.nativeassets.multisig.scenarios;

import com.bloxbean.cardano.client.common.model.Networks;
import com.bloxbean.cardano.client.crypto.Keys;
import com.bloxbean.cardano.client.crypto.SecretKey;
import com.bloxbean.cardano.client.crypto.VerificationKey;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.util.HexUtil;
import com.google.protobuf.Struct;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.grpc.stub.StreamObserver;
import iog.psg.client.nativeassets.NativeAssetsBuilder;
import iog.psg.client.nativeassets.multisig.v1.NativeAssetsMultisigApi;
import iog.psg.service.nativeassets.BlockInfo;
import iog.psg.service.nativeassets.NativeAsset;
import iog.psg.service.nativeassets.NativeAssetId;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class MintMultisigScenarioApp {
    public static void main(String[] args) throws CborSerializationException, ExecutionException, InterruptedException {
        Config config = ConfigFactory.defaultApplication()
                .getConfig("akka.grpc.client.test")
                .resolve();
        NativeAssetsMultisigApi client = NativeAssetsBuilder
                .create(config)
                .buildMultisig();

        Keys policyKeys1 = client.generateKeys();
        Keys policyKeys2 = client.generateKeys();

        SecretKey paymentSKey = SecretKey.create(HexUtil.decodeHexString(config.getString("sKeyHex")));
        VerificationKey paymentVKey = client.generateVerificationKey(paymentSKey);
        Keys paymentKeys = new Keys(paymentSKey, paymentVKey);
        String paymentAddress = client.generateAddress(paymentVKey, Networks.preprod()).getAddress();

        CompletionStage<BlockInfo> futTx = client.createPolicy("TestPolicy2", List.of(policyKeys1.getVkey(), policyKeys2.getVkey()))
                .thenCompose(createPolicyResponse ->
                        createMintTx(
                                paymentAddress,
                                createPolicyResponse.getPolicy().getPolicyId(),
                                client)
                )
                .thenCompose(unsignedTxResponse -> {
                            String txId = unsignedTxResponse.getTx().getTxId();
                            System.out.println("txId: " + txId);
                            return addSignature(txId, policyKeys1, client)
                                    .thenCompose(emptyResponse -> addSignature(txId, policyKeys2, client))
                                    .thenCompose(emptyResponse -> addSignature(txId, paymentKeys, client))
                                    .thenApply(emptyResponse -> txId);
                        }
                ).thenCompose(txId -> {
                    CompletableFuture<BlockInfo> sendTxResult = new CompletableFuture();
                    client.sendTransaction(
                            txId,
                            0,
                            lastResult(
                                    sendTxResult,
                                    sendTxResponse -> sendTxResponse.getSendTx().getTxInfo().getBlock()
                            )
                    );
                    return sendTxResult;
                });


        System.out.println(futTx.toCompletableFuture().get().getHeight());
    }

    private static CompletionStage<EmptyResponse> addSignature(String txId, Keys keys, NativeAssetsMultisigApi client) {
        byte[] key1Sig = client.sign(txId, keys.getSkey());
        return client.addWitness(txId, keys.getVkey(), key1Sig);
    }

    private static CompletionStage<UnsignedTxResponse> createMintTx(String paymentAddress, String policyId, NativeAssetsMultisigApi client) {
        NativeAssetId nativeAssetId = NativeAssetId.newBuilder()
                .setPolicyId(policyId)
                .setName("TestAsset")
                .build();
        NativeAsset nativeAsset = NativeAsset.newBuilder()
                .setAmount(1)
                .setId(nativeAssetId)
                .build();
        AddressedNativeAsset nativeAssetWithAddress = AddressedNativeAsset.newBuilder()
                // minting to itself, but it can be any valid Cardano Address
                .setAddress(paymentAddress)
                .setNativeAsset(nativeAsset)
                .build();

        AddressedNativeAssets nativeAssets = AddressedNativeAssets.newBuilder()
                .setPaymentAddress(paymentAddress)
                .addNativeAssets(nativeAssetWithAddress)
                // TODO supply non empty metadata
                .setMetadata(Struct.newBuilder().build())
                .build();

        return client.createMintTransactionWithArbitraryMetadata(nativeAssets);
    }

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
}
