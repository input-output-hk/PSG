package iog.psg.assets.service;


import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.common.model.Networks;
import com.bloxbean.cardano.client.crypto.Keys;
import com.bloxbean.cardano.client.crypto.SecretKey;
import com.bloxbean.cardano.client.crypto.VerificationKey;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import com.bloxbean.cardano.client.util.HexUtil;
import com.google.protobuf.struct.Struct;
import com.google.protobuf.struct.Value;
import io.grpc.stub.StreamObserver;
import iog.psg.client.nativeassets.multisig.v1.NativeAssetsMultisigApi;
import iog.psg.service.nativeassets.multisig.proto.v1.native_assets_multisig_service.*;
import iog.psg.service.nativeassets.native_assets.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import scala.$less$colon$less$;
import scala.collection.JavaConverters;
import scala.jdk.CollectionConverters;

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
                    if (response.getProblem().msg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }

    public CompletionStage<Policy> createPolicyWithSecKeys(String policyName,
                                                           String hexSecKey,
                                                           Optional<Integer> beforeSlot,
                                                           Optional<Integer> afterSlot) throws CborSerializationException {
        SecretKey secKey = SecretKey.create(HexUtil.decodeHexString(hexSecKey));
        TimeBounds timeBounds = createTimeBounds(beforeSlot, afterSlot);
        return nativeAssetsMultisigApi.createPolicyUsingPrivateKeys(policyName, timeBounds, Arrays.asList(secKey))
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }


    public CompletionStage<Policy> getPolicyById(String policyId) {
        return nativeAssetsMultisigApi.getPolicyById(policyId)

                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }


    public CompletionStage<Policy> getPolicyByName(String policyByName) {
        return nativeAssetsMultisigApi.getPolicyByName(policyByName)

                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }


    public CompletionStage<List<Policy>> listPolices() {
        return nativeAssetsMultisigApi.listPolices()
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return CollectionConverters.SeqHasAsJava(response.policies()).asJava();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }


    public CompletionStage<String> createMintTransaction(String policyId, String assetName, String paymentAddress, String mintTargetAddress) {
        Nft nft = Nft$.MODULE$.defaultInstance()
                .withName(assetName)
                .withAssetName(assetName);

        AddressedNft addressedNft = AddressedNft$.MODULE$.defaultInstance()
                .withNft(nft)
                .withAddress(mintTargetAddress);

        return nativeAssetsMultisigApi.createMintTransaction(policyId, paymentAddress, addressedNft)

                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getTx().tx();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }

    public CompletionStage<String> createMintTransactionWithArbitraryMetadata(String policyId,
                                                                              String assetName,
                                                                              String paymentAddress,
                                                                              String mintTargetAddress,
                                                                              Map<String, Value> metadata,
                                                                              Long amount) {

        Struct struct = com.google.protobuf.struct.Struct.of(
                CollectionConverters.MapHasAsScala(metadata).
                        asScala()
                        .toMap($less$colon$less$.MODULE$.refl())
        );

        NativeAssetId nativeAssetId = NativeAssetId$.MODULE$
                .defaultInstance()
                .withName(assetName)
                .withPolicyId(policyId);
        NativeAsset nativeAsset = NativeAsset$.MODULE$
                .defaultInstance()
                .withAmount(amount)
                .withId(nativeAssetId);

        AddressedNativeAsset addressedNativeAsset = AddressedNativeAsset$.MODULE$
                .defaultInstance()
                .withAddress(mintTargetAddress)
                .withNativeAsset(nativeAsset);

        AddressedNativeAssets assets = AddressedNativeAssets$.MODULE$
                .defaultInstance()
                .withPaymentAddress(paymentAddress)
                .withMetadata(struct)
                .withNativeAssets(CollectionConverters.ListHasAsScala(Arrays.asList(addressedNativeAsset)).asScala().toSeq());


        return nativeAssetsMultisigApi.createMintTransactionWithArbitraryMetadata(assets)

                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getTx().tx();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                }).toCompletableFuture();
    }

    public CompletionStage<String> createTransferTransaction(String policyId, String assetName, String fromAddress, String toAddress, Long amount) {
        return nativeAssetsMultisigApi.createTransferTransaction(policyId, assetName, fromAddress, toAddress, amount)

                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getTx().tx();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }

    public CompletionStage<String> createBurnTransaction(String policyId,
                                                         String assetName,
                                                         String targetAddress,
                                                         Long amount) {

        return nativeAssetsMultisigApi.createBurnTransaction(policyId, assetName, targetAddress, amount)

                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getTx().tx();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }


    public CompletionStage<String> addWitnessWithSecretKey(String txId, String secretKeyHex) throws CborSerializationException {

        SecretKey privateKey = SecretKey.create(HexUtil.decodeHexString(secretKeyHex));
        return nativeAssetsMultisigApi.addWitness(txId, privateKey)

                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return "signature added";
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }


    public CompletionStage<String> addWitnessWithPublicKey(String txId,
                                                           String publicKeyHex,
                                                           String signatureHex) throws CborSerializationException {

        VerificationKey publicKey = VerificationKey.create(HexUtil.decodeHexString(publicKeyHex));

        return nativeAssetsMultisigApi.addWitness(txId, publicKey, HexUtil.decodeHexString(signatureHex))

                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return "signature added";
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }


    public CompletionStage<Long> getTx(String txId) throws CborSerializationException {
        return nativeAssetsMultisigApi.getTx(txId)

                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getTx().confirmations();
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }


    public CompletionStage<List<String>> listTxs() {
        return nativeAssetsMultisigApi.listTxs()

                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return JavaConverters.asJava(response.transactions()).stream().map(ur -> ur.txId()).collect(Collectors.toList());
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }


    public CompletionStage<List<String>> listTxsByPolicyId(String policyId) {
        return nativeAssetsMultisigApi.listTxs(policyId)

                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return JavaConverters.asJava(response.transactions()).stream().map(ur -> ur.txId()).collect(Collectors.toList());
                    else {
                        throw new RuntimeException(response.getProblem().msg());
                    }
                });
    }


    public CompletionStage<List<String>> listWitnesses(String txId) {
        return nativeAssetsMultisigApi.listWitnesses(txId)

                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return JavaConverters.asJava(response.verKeyHashes());
                    else {
                        throw new RuntimeException(response.getProblem().msg());
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
