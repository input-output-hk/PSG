package main;

import com.bloxbean.cardano.client.common.model.Networks;
import com.bloxbean.cardano.client.crypto.Keys;
import com.bloxbean.cardano.client.crypto.SecretKey;
import com.bloxbean.cardano.client.crypto.VerificationKey;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.util.HexUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.grpc.stub.StreamObserver;
import iog.psg.client.nativeassets.NativeAssetsBuilder;
import iog.psg.client.nativeassets.multisig.v1.NativeAssetsMultisigApi;
import iog.psg.service.nativeassets.NativeAsset;
import iog.psg.service.nativeassets.NativeAssetId;
import iog.psg.service.nativeassets.multisig.proto.v1.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

public class MintBurnTransferMultisig {
    public static void main(String[] args) throws CborSerializationException, ExecutionException, InterruptedException, InvalidProtocolBufferException {
        Config config = ConfigFactory.defaultApplication()
                .getConfig("grpc.client")
                .resolve();
        NativeAssetsMultisigApi client = NativeAssetsBuilder
                .create(config)
                .buildMultisig();

        // You can't have 2 policies with the same name
        String policyName = "TestPolicy" + new Random().nextInt();
        Keys policyKeys1 = client.generateKeys();
        Keys policyKeys2 = client.generateKeys();
        String assetName = "TestAsset";

        Keys paymentKeys = createPaymentKeys(config, client);
        String paymentAddress = client.generateAddress(paymentKeys.getVkey(), Networks.preprod()).getAddress();

        String policyId = createPolicy(client, policyName, policyKeys1, policyKeys2);

        mint(client, policyKeys1, policyKeys2, assetName, paymentKeys, paymentAddress, policyId, 5);

        burn(client, policyKeys1, policyKeys2, assetName, paymentKeys, paymentAddress, policyId, 2);

        transfer(config, client, assetName, paymentKeys, paymentAddress, policyId, 2);
    }

    private static Keys createPaymentKeys(Config config, NativeAssetsMultisigApi client) throws CborSerializationException {
        SecretKey paymentSKey = SecretKey.create(HexUtil.decodeHexString(config.getString("sKeyHex")));
        VerificationKey paymentVKey = client.generateVerificationKey(paymentSKey);
        return new Keys(paymentSKey, paymentVKey);
    }

    private static SendTxResponse transfer(Config config, NativeAssetsMultisigApi client, String assetName, Keys paymentKeys, String paymentAddress, String policyId, long amount) throws ExecutionException, InterruptedException {
        CompletionStage<UnsignedTxResponse> transferTxFut = client.createTransferTransaction(policyId, assetName, paymentAddress, config.getString("transferAddress"), amount);
        String transferTxId = getTxId(transferTxFut);
        // Notice that you don't need to supply policy keys signatures for transfer transactions
        return signAndSendTx(transferTxId, List.of(paymentKeys), client).toCompletableFuture().get();
    }

    private static SendTxResponse burn(NativeAssetsMultisigApi client, Keys policyKeys1, Keys policyKeys2, String assetName, Keys paymentKeys, String paymentAddress, String policyId, long amount) throws ExecutionException, InterruptedException {
        CompletionStage<UnsignedTxResponse> burnTxFut = client.createBurnTransaction(assetName, policyId, paymentAddress, amount);
        String burnTxId = getTxId(burnTxFut);
        return signAndSendTx(burnTxId, List.of(policyKeys1, policyKeys2, paymentKeys), client).toCompletableFuture().get();
    }

    private static SendTxResponse mint(NativeAssetsMultisigApi client, Keys policyKeys1, Keys policyKeys2, String assetName, Keys paymentKeys, String paymentAddress, String policyId, long amount) throws InvalidProtocolBufferException, ExecutionException, InterruptedException {
        CompletionStage<UnsignedTxResponse> mintTxFut = createMintTx(paymentAddress, policyId, assetName, client, amount);
        String mintTxId = getTxId(mintTxFut);
        return signAndSendTx(mintTxId, List.of(policyKeys1, policyKeys2, paymentKeys), client).toCompletableFuture().get();
    }

    private static String createPolicy(NativeAssetsMultisigApi client, String policyName, Keys policyKeys1, Keys policyKeys2) throws InterruptedException, ExecutionException {
        CreatePolicyResponse createPolicyResponse = client.createPolicy(policyName, List.of(policyKeys1.getVkey(), policyKeys2.getVkey())).toCompletableFuture().get();
        if (createPolicyResponse.hasProblem()) throw new RuntimeException(createPolicyResponse.getProblem().getMsg());
        return createPolicyResponse.getPolicy().getPolicyId();
    }

    private static String getTxId(CompletionStage<UnsignedTxResponse> txResponse) throws ExecutionException, InterruptedException {
        UnsignedTxResponse tx = txResponse.toCompletableFuture().get();
        if (tx.hasProblem())
            throw new RuntimeException(tx.getProblem().getMsg());
        else {
            String txId = tx.getTx().getTxId();
            System.out.println("txId: " + txId);
            return txId;
        }
    }

    private static CompletionStage<SendTxResponse> signAndSendTx(String txId, List<Keys> policyKeys, NativeAssetsMultisigApi client) throws InterruptedException, ExecutionException {
        for (Keys key : policyKeys) {
            addSignature(txId, key, client).toCompletableFuture().get();
        }

        CompletableFuture<SendTxResponse> sendTxResult = new CompletableFuture<>();
        client.sendTransaction(
                txId,
                0,
                lastResult(sendTxResult)
        );
        return sendTxResult;
    }

    private static CompletionStage<EmptyResponse> addSignature(String txId, Keys keys, NativeAssetsMultisigApi
            client) {
        byte[] key1Sig = client.sign(txId, keys.getSkey());
        return client.addWitness(txId, keys.getVkey(), key1Sig);
    }

    private static CompletionStage<UnsignedTxResponse> createMintTx(String paymentAddress, String policyId, String assetName, NativeAssetsMultisigApi client, long amount) throws InvalidProtocolBufferException {
        NativeAssetId nativeAssetId = NativeAssetId.newBuilder()
                .setPolicyId(policyId)
                .setName(assetName)
                .build();
        NativeAsset nativeAsset = NativeAsset.newBuilder()
                .setAmount(amount)
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
                .setMetadata(buildMetadata())
                .build();

        return client.createMintTransaction(nativeAssets);
    }

    private static Struct buildMetadata() throws InvalidProtocolBufferException {
        String randomJsonMetadata = """
                {
                  "81": {
                    "name": "arbitraryTest",
                    "randomKey": 38,
                    "arrTest": [
                      {"1": 3},
                      {"2": "tada"}
                    ],
                    "mapTest": {
                      "eight": "eightItis",
                      "nested": {
                        "nestedValue": "I'm not empty"
                      }
                    }
                  }
                }
                """;
        Struct.Builder structBuilder = Struct.newBuilder();
        JsonFormat.parser().ignoringUnknownFields().merge(randomJsonMetadata, structBuilder);
        return structBuilder.build();
    }

    private static StreamObserver<SendTxResponse> lastResult(CompletableFuture<SendTxResponse> result) {
        return new StreamObserver<>() {
            SendTxResponse last;

            @Override
            public void onNext(SendTxResponse event) {
                if (event.hasProblem()) {
                    System.out.println(event.getProblem().getMsg());
                } else if (event.hasSubmitted()) {
                    System.out.println("Transaction was submitted to blockchain");
                } else {
                    System.out.println("Transaction is in blockchain: \n" + event.getSendTx());
                }
                last = event;
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Got error: " + throwable.getCause());
            }

            @Override
            public void onCompleted() {
                result.complete(last);
            }
        };

    }
}
