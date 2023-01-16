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
        String apiKey = ""; // apiKey
        String clientId = ""; // clientId

        //cardano address that will get the tokens
        String address = "";

        //private key in hex format
        String privateKey = "";

        var client = NativeAssetsBuilder.create(apiKey, clientId).buildMultisig();

        // You can't have 2 policies with the same name
        String policyName = "TestPolicy" + new Random().nextInt();
        Keys policyKeys1 = client.generateKeys();
        Keys policyKeys2 = client.generateKeys();
        String assetName = "TestAsset";

        //create keypair from your private key
        SecretKey paymentSKey = SecretKey.create(HexUtil.decodeHexString(privateKey));
        VerificationKey paymentVKey = client.generateVerificationKey(paymentSKey);
        Keys paymentKeys = new Keys(paymentSKey, paymentVKey);

        //generate address on Cardano blockchain associated with your private key
        String paymentAddress = client.generateAddress(paymentKeys.getVkey(), Networks.preprod()).getAddress();

        //create policy that requires 2 signatures for mint / burn
        String policyId = createPolicy(client, policyName, policyKeys1, policyKeys2);

        CompletionStage<UnsignedTxResponse> mintTxFut = createMintTx(paymentAddress, policyId, assetName, client, 5L);
        String mintTxId = getTxId(mintTxFut);
        signAndSendTx(mintTxId, List.of(policyKeys1, policyKeys2, paymentKeys), client).toCompletableFuture().get();

        //burn some tokens
        CompletionStage<UnsignedTxResponse> burnTxFut = client.createBurnTransaction(assetName, policyId, paymentAddress, 2L);
        String burnTxId = getTxId(burnTxFut);
        signAndSendTx(burnTxId, List.of(policyKeys1, policyKeys2, paymentKeys), client).toCompletableFuture().get();

        //transfer some tokens
        CompletionStage<UnsignedTxResponse> transferTxFut = client.createTransferTransaction(policyId, assetName, paymentAddress, address, 2L);
        String transferTxId = getTxId(transferTxFut);
        // Notice that you don't need to supply policy keys signatures for transfer transactions
        // only the signature from the key that holds native asset
        signAndSendTx(transferTxId, List.of(paymentKeys), client).toCompletableFuture().get();
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

    private static CompletionStage<EmptyResponse> addSignature(String txId, Keys keyPair, NativeAssetsMultisigApi client) {
        byte[] signature = client.sign(txId, keyPair.getSkey());
        return client.addSignature(txId, keyPair.getVkey(), signature);
    }

    private static CompletionStage<UnsignedTxResponse> createMintTx(String paymentAddress, String policyId, String assetName, NativeAssetsMultisigApi client, Long amount) throws InvalidProtocolBufferException {
        NativeAssetId nativeAssetId = NativeAssetId.newBuilder()
                .setPolicyId(policyId)
                .setName(assetName)
                .build();

        NativeAsset nativeAsset = NativeAsset.newBuilder()
                .setAmount(amount)
                .setId(nativeAssetId)
                .build();

        AddressedNativeAsset nativeAssetWithAddress = AddressedNativeAsset.newBuilder()
                // minting to address that you own, but it can be any valid Cardano Address
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

    //this is optional metadata that you can add to your native asset
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

    //for consuming results
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
