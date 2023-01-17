package iog.psg.assets.service;


import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.common.model.Networks;
import com.bloxbean.cardano.client.crypto.Keys;
import com.bloxbean.cardano.client.crypto.SecretKey;
import com.bloxbean.cardano.client.crypto.VerificationKey;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.util.HexUtil;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import io.grpc.stub.StreamObserver;

import iog.psg.client.nativeassets.multisig.v1.NativeAssetsMultisigApi;
import iog.psg.service.nativeassets.NativeAsset;
import iog.psg.service.nativeassets.NativeAssetId;
import iog.psg.service.nativeassets.Nft;
import iog.psg.service.nativeassets.TimeBounds;
import iog.psg.service.nativeassets.multisig.proto.v1.AddressedNativeAsset;
import iog.psg.service.nativeassets.multisig.proto.v1.AddressedNativeAssets;
import iog.psg.service.nativeassets.multisig.proto.v1.AddressedNft;
import iog.psg.service.nativeassets.multisig.proto.v1.Policy;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

import static iog.psg.assets.NativeAssetUtils.createTimeBounds;

@Service
@Log
public class MultisigNativeAssetService {
    @Autowired
    private NativeAssetsMultisigApi nativeAssetsMultisigApi;
    @Autowired
    private ConversionService conversionService;

    public CompletionStage<Policy> createPolicyWithPublicKeys(String policyName,
                                                              String hexPubKey,
                                                              Optional<Integer> beforeSlot,
                                                              Optional<Integer> afterSlot) throws CborSerializationException {
        VerificationKey pubKey = VerificationKey.create(HexUtil.decodeHexString(hexPubKey));
        TimeBounds timeBounds = createTimeBounds(beforeSlot, afterSlot);
        return nativeAssetsMultisigApi.createPolicy(policyName, timeBounds, Arrays.asList(pubKey))
                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }



    public CompletionStage<Policy> getPolicyById(String policyId) {
        return nativeAssetsMultisigApi.getPolicyById(policyId)

                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }


    public CompletionStage<Policy> getPolicyByName(String policyByName) {
        return nativeAssetsMultisigApi.getPolicyByName(policyByName)

                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }


    public CompletionStage<List<Policy>> listPolices() {
        return nativeAssetsMultisigApi.listPolices()
                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getPoliciesList();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }


    public CompletionStage<String> createMintTransaction(String policyId, String assetName, String paymentAddress, String mintTargetAddress) {
        Nft nft = Nft.newBuilder()
                .setName(assetName)
                .setAssetName(assetName)
                .build();

        AddressedNft addressedNft = AddressedNft.newBuilder()
                .setNft(nft)
                .setAddress(mintTargetAddress)
                .build();


        return nativeAssetsMultisigApi.createMintTransaction(policyId, paymentAddress, addressedNft)

                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getTx().getTx();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }

    public CompletionStage<String> createMintTransactionWithArbitraryMetadata(String policyId,
                                                                              String assetName,
                                                                              String paymentAddress,
                                                                              String mintTargetAddress,
                                                                              Map<String, Value> metadata,
                                                                              Long amount) {

        Struct struct = com.google.protobuf.Struct.newBuilder()
                .putAllFields(metadata)
                .build();

        NativeAssetId nativeAssetId = NativeAssetId.newBuilder()
                .setName(assetName)
                .setPolicyId(policyId)
                .build();
        NativeAsset nativeAsset = NativeAsset.newBuilder()
                .setAmount(amount)
                .setId(nativeAssetId)
                .build();

        AddressedNativeAsset addressedNativeAsset = AddressedNativeAsset.newBuilder()
                .setAddress(mintTargetAddress)
                .setNativeAsset(nativeAsset)
                .build();

        AddressedNativeAssets assets = AddressedNativeAssets.newBuilder()
                .setPaymentAddress(paymentAddress)
                .setMetadata(struct)
                .addAllNativeAssets(Arrays.asList(addressedNativeAsset))
                .build();


        return nativeAssetsMultisigApi.createMintTransaction(assets)

                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getTx().getTx();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                }).toCompletableFuture();
    }

    public CompletionStage<String> createTransferTransaction(String policyId, String assetName, String fromAddress, String toAddress, Long amount) {
        return nativeAssetsMultisigApi.createTransferTransaction(policyId, assetName, fromAddress, toAddress, amount)

                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getTx().getTx();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }

    public CompletionStage<String> createBurnTransaction(String policyId,
                                                         String assetName,
                                                         String targetAddress,
                                                         Long amount) {

        return nativeAssetsMultisigApi.createBurnTransaction(policyId, assetName, targetAddress, amount)

                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getTx().getTx();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }


    public CompletionStage<String> addWitnessWithPublicKey(String txId,
                                                           String publicKeyHex,
                                                           String signatureHex) throws CborSerializationException {

        VerificationKey publicKey = VerificationKey.create(HexUtil.decodeHexString(publicKeyHex));

        return nativeAssetsMultisigApi.addWitness(txId, publicKey, HexUtil.decodeHexString(signatureHex))

                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return "signature added";
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }


    public CompletionStage<Long> getTx(String txId) throws CborSerializationException {
        return nativeAssetsMultisigApi.getTx(txId)

                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getTx().getConfirmations();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }


    public CompletionStage<List<String>> listTxs() {
        return nativeAssetsMultisigApi.listTxs()

                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getTransactionsList().stream().map(ur -> ur.getTxId()).collect(Collectors.toList());
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }


    public CompletionStage<List<String>> listTxsByPolicyId(String policyId) {
        return nativeAssetsMultisigApi.listTxs(policyId)

                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getTransactionsList().stream().map(ur -> ur.getTxId()).collect(Collectors.toList());
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }


    public CompletionStage<List<String>> listWitnesses(String txId) {
        return nativeAssetsMultisigApi.listWitnesses(txId)

                .thenApply(response -> {
                    if (response.getProblem().getMsg().isEmpty())
                        return response.getVerKeyHashesList();
                    else {
                        throw new RuntimeException(response.getProblem().getMsg());
                    }
                });
    }


    public CompletionStage<String> sendTransaction(String txId, Integer depth) {
        CompletableFuture<String> CompletionStage = new CompletableFuture<>();
        nativeAssetsMultisigApi.sendTransaction(txId, depth, streamObserver(CompletionStage, (r -> r.getSendTx().toString())));
        return CompletionStage;
    }

    private <V> StreamObserver<V> streamObserver(CompletableFuture<String> completable,
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
                completable.complete(res);
            }
        };
    }


    public Address generateAddress(String hexPubKey) throws CborSerializationException {
        return nativeAssetsMultisigApi.generateAddress(VerificationKey.create(HexUtil.decodeHexString(hexPubKey)), Networks.preprod());
    }


    public VerificationKey generateVerKey(String hexSecKey) throws CborSerializationException {
        return nativeAssetsMultisigApi.generateVerificationKey(SecretKey.create(HexUtil.decodeHexString(hexSecKey)));
    }

    public Keys generateKeys() throws CborSerializationException {
        return nativeAssetsMultisigApi.generateKeys();
    }

}
