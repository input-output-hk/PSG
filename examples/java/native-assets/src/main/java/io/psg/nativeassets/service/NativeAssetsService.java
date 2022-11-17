package io.psg.nativeassets.service;

import akka.Done;
import akka.actor.ActorSystem;
import akka.stream.javadsl.Sink;
import com.google.protobuf.struct.Struct;
import io.psg.nativeassets.exception.NativeAssetException;
import io.psg.nativeassets.model.MintDetails;
import iog.psg.client.nativeassets.NativeAssetsApi;
import iog.psg.service.nativeassets.*;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import scala.$less$colon$less$;
import scala.collection.immutable.HashMap;
import scala.jdk.CollectionConverters;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Log
public class NativeAssetsService {
    @Autowired
    private NativeAssetsApi nativeAssetsApi;
    @Autowired
    private ConversionService conversionService;
    final ActorSystem system = ActorSystem.create("demo");

    public CompletableFuture<Policy> createPolicy(String name, Optional<Integer> before, Optional<Integer> after) {
        return nativeAssetsApi.createPolicy(name, before, after)
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
        return nativeAssetsApi.mintAsset(
                        NativeAssetId.of(name, policyId),
                        mintDetails.getAmount(),
                        mintDetails.getNfts().stream().map(nft -> conversionService.convert(nft, Nft.class)).collect(Collectors.toList()),
                        mintDetails.getDepth())
                .completionTimeout(Duration.ofMinutes(2))
                .runWith(Sink.last(), system)
                .toCompletableFuture()
                .thenApply(response -> {
                    if (response.getProblem().msg().isEmpty())
                        return response.toString();
                    else {
                        throw new NativeAssetException(response.getProblem());
                    }
                });
    }

    //TODO to be finished
    public CompletableFuture<Done> mintNativeAssetWithArbitraryMetadata(String name, String policyId, Long amount, Integer depth) {
        return nativeAssetsApi.mintAssetWithArbitraryMetadata(NativeAssetId.of(name, policyId), amount, Struct.of(new HashMap<>()), depth)
                .run(system)
                .toCompletableFuture();
    }

    //TODO to be finished
    public CompletableFuture<Done> transferNativeAsset(String name, String policyId, String toAddress, Long amount, Integer depth) {
        return nativeAssetsApi.transferAsset(NativeAssetId.of(name, policyId), toAddress, amount, depth)
                .run(system)
                .toCompletableFuture();
    }

    //TODO to be finished
    public CompletableFuture<Done> burnNativeAsset(String name, String policyId, Long amount, Integer depth) {
        return nativeAssetsApi.burnAsset(NativeAssetId.of(name, policyId), amount, depth)
                .run(system)
                .toCompletableFuture();
    }

    //TODO to be finished
    public CompletableFuture sendAirDropBatch(Map struct, List airdrops) {
        return nativeAssetsApi.sendAirDropBatch(
                        Struct.of(CollectionConverters.MapHasAsScala(struct).asScala().toMap($less$colon$less$.MODULE$.refl())),
                        airdrops)
                .toCompletableFuture();
    }

    //TODO to be finished
    public CompletableFuture<AirDropStatusResponse> getAirDropStatus(String batchId) {
        return nativeAssetsApi.getAirDropStatus(batchId)
                .toCompletableFuture();
    }

}
