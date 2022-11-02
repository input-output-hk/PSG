package io.psg.nativeassets.controller;

import akka.Done;
import io.psg.nativeassets.model.MintDetails;
import io.psg.nativeassets.model.NativeAsset;
import io.psg.nativeassets.model.Policy;
import io.psg.nativeassets.service.NativeAssetsService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


@RestController
@Log
public class NativeAssetsController {
    @Autowired
    private NativeAssetsService nativeAssetsService;

    @Autowired
    private ConversionService conversionService;

    @PostMapping("/policies/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<Policy> createPolicy(
            @PathVariable String name,
            @RequestParam(required = false) Integer beforeSlot,
            @RequestParam(required = false) Integer afterSlot) {
        return nativeAssetsService.createPolicy(name, Optional.ofNullable(beforeSlot), Optional.ofNullable(afterSlot))
                .thenApply(p -> conversionService.convert(p, Policy.class));
    }

    @GetMapping("/policies/{policyId}")
    public CompletableFuture<Policy> getPolicy(@PathVariable String policyId) {
        return nativeAssetsService.getPolicy(policyId)
                .thenApply(p -> conversionService.convert(p, Policy.class));
    }

    @GetMapping("/policies")
    public CompletableFuture<List<Policy>> listPolicies() {
        return nativeAssetsService.listPolicies()
                .thenApply(l -> l.stream()
                        .map(p -> conversionService.convert(p, Policy.class))
                        .collect(Collectors.toList()));
    }

//    09822cd20e5db438cc5bbc0e8678be8bff87b37baeca1a662c7bca5a
    //addr_test1vqusn9e6nc70rz4vfptuetrke0myrq0m9ul0zqwgewqgqecj5l9tl

    @DeleteMapping("/policies/{policyId}")
    public CompletableFuture<String> deletePolicy(@PathVariable String policyId) {
        return nativeAssetsService.deletePolicy(policyId);
    }

    @PostMapping("/assets/{name}/policy/{policyId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<NativeAsset> createAsset(
            @PathVariable String name,
            @PathVariable String policyId) {
        return nativeAssetsService.createNativeAsset(name, policyId)
                .thenApply(p -> conversionService.convert(p, NativeAsset.class));
    }

    @GetMapping("/assets/{name}/policy/{policyId}")
    public CompletableFuture<NativeAsset> getAsset(
            @PathVariable String name,
            @PathVariable String policyId) {
        return nativeAssetsService.getNativeAsset(name, policyId)
                .thenApply(a -> conversionService.convert(a, NativeAsset.class));
    }

    @GetMapping("/assets")
    public CompletableFuture<List<NativeAsset>> listAssets() {
        return nativeAssetsService.listNativeAssets()
                .thenApply(l -> l.stream()
                        .map(p -> conversionService.convert(p, NativeAsset.class))
                        .collect(Collectors.toList()));
    }

    @DeleteMapping("/assets/{name}/policy/{policyId}")
    public CompletableFuture<String> deleteAsset(
            @PathVariable String name,
            @PathVariable String policyId) {
        return nativeAssetsService.deleteNativeAsset(name, policyId);
    }

    @PostMapping("/mint")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<String> mint(
            @RequestBody MintDetails mintDetails) {
        return nativeAssetsService.mintNativeAsset(mintDetails.getName(), mintDetails.getPolicyId(), mintDetails);
    }


}