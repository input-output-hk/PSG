package io.psg.nativeassets.service;

import com.google.protobuf.struct.Struct;
import io.grpc.stub.StreamObserver;
import io.psg.nativeassets.exception.NativeAssetException;
import io.psg.nativeassets.model.MintDetails;
import iog.psg.client.nativeassets.NativeAssetsApi;
import iog.psg.service.nativeassets.native_assets.NativeAsset;
import iog.psg.service.nativeassets.native_assets.NativeAssetId;
import iog.psg.service.nativeassets.native_assets.Nft;
import iog.psg.service.nativeassets.native_assets.TimeBounds;
import iog.psg.service.nativeassets.native_assets_service.AirDropStatusResponse;
import iog.psg.service.nativeassets.native_assets_service.Policy;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import scala.$less$colon$less$;
import scala.collection.immutable.HashMap;
import scala.jdk.CollectionConverters;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static io.psg.nativeassets.service.NativeAssetUtils.createTimeBounds;

@Service
@Log

public class NativeAssetsService {
    @Autowired
    private NativeAssetsApi nativeAssetsApi;
    @Autowired
    private ConversionService conversionService;

    public CompletableFuture<Policy> createPolicy(String name, Optional<Integer> before, Optional<Integer> after) {
        TimeBounds timeBounds = createTimeBounds(before, after);
        return nativeAssetsApi.createPolicy(name, timeBounds)
                .toCompletableFuture()
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getPolicy();
                    else {
                        throw new NativeAssetException(response.getProblem());
                    }

                });
    }

    public CompletableFuture<Policy> getPolicy(String policyId) {
        return nativeAssetsApi.getPolicy(policyId).toCompletableFuture()
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getPolicy();

                    else {
                        throw new NativeAssetException(response.getProblem());
                    }

                });
    }

    public CompletableFuture<List<Policy>> listPolicies() {
        return nativeAssetsApi.getPolices().toCompletableFuture()
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return CollectionConverters.SeqHasAsJava(response.policies()).asJava();
                    else {
                        throw new NativeAssetException(response.getProblem());
                    }
                });

    }

    public CompletableFuture<String> deletePolicy(String policyId) {
        return nativeAssetsApi
                .deletePolicy(policyId)
                .toCompletableFuture()
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return "the policy policyId=" + policyId + " successfully deleted";
                    else {
                        throw new NativeAssetException(response.getProblem());
                    }
                });

    }


    public CompletableFuture<NativeAsset> createNativeAsset(String name, String policyId) {
        return nativeAssetsApi.createAsset(NativeAssetId.of(name, policyId))
                .toCompletableFuture()
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getAsset();
                    else {
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }

    public CompletableFuture<NativeAsset> getNativeAsset(String name, String policyId) {
        return nativeAssetsApi.getAsset(NativeAssetId.of(name, policyId))
                .toCompletableFuture()
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.getAsset();
                    else {
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }

    public CompletableFuture<List<NativeAsset>> listNativeAssets() {
        return nativeAssetsApi.listAssets().toCompletableFuture()
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return CollectionConverters.SeqHasAsJava(response.assets()).asJava()
                                .stream()
                                .collect(Collectors.toList());
                    else {
                        throw new NativeAssetException(response.getProblem());

                    }
                });

    }

    public CompletableFuture<String> deleteNativeAsset(String name, String policyId) {
        return nativeAssetsApi.deleteAsset(NativeAssetId.of(name, policyId))
                .toCompletableFuture()
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return "the asset assetName= " + name + " policyId=" + policyId + " successfully deleted";
                    else {
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }

    public CompletableFuture<String> mintNativeAsset(String name, String policyId, MintDetails mintDetails) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        nativeAssetsApi.mintAsset(
                NativeAssetId.of(name, policyId),
                mintDetails.getAmount(),
                mintDetails.getNfts().stream().map(nft -> conversionService.convert(nft, Nft.class)).collect(Collectors.toList()),
                mintDetails.getDepth(), streamObserver(completableFuture));
        return completableFuture;
    }


    public CompletableFuture<String> mintNativeAssetWithArbitraryMetadata(String name, String policyId, Long amount, Integer depth) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        nativeAssetsApi.mintAssetWithArbitraryMetadata(NativeAssetId.of(name, policyId), amount, Struct.of(new HashMap<>()), depth, streamObserver(completableFuture));
        return completableFuture;

    }


    public CompletableFuture<String> transferNativeAsset(String name, String policyId, String toAddress, Long amount, Integer depth) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        nativeAssetsApi.transferAsset(NativeAssetId.of(name, policyId), toAddress, amount, depth, streamObserver(completableFuture));
        return completableFuture;
    }


    public CompletableFuture<String> burnNativeAsset(String name, String policyId, Long amount, Integer depth) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();
        nativeAssetsApi.burnAsset(NativeAssetId.of(name, policyId), amount, depth, streamObserver(completableFuture));
        return completableFuture;
    }

    public CompletableFuture sendAirDropBatch(Map struct, List airdrops) {
        return nativeAssetsApi.sendAirDropBatch(
                        Struct.of(CollectionConverters.MapHasAsScala(struct).asScala().toMap($less$colon$less$.MODULE$.refl())),
                        airdrops)
                .toCompletableFuture();
    }

    public CompletableFuture<AirDropStatusResponse> getAirDropStatus(String batchId) {
        return nativeAssetsApi.getAirDropStatus(batchId)
                .toCompletableFuture();
    }

    private <V> StreamObserver<V> streamObserver(CompletableFuture<String> completableFuture) {
        return new StreamObserver<V>() {
            List<String> buffer = new ArrayList();

            @Override
            public void onNext(V res) {
                buffer.add(res.toString());
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
                completableFuture.complete(res);
            }
        };
    }

}
