package io.psg.nativeassets.controller;

import com.bloxbean.cardano.client.address.Address;
import com.bloxbean.cardano.client.crypto.Keys;
import com.bloxbean.cardano.client.crypto.VerificationKey;
import com.bloxbean.cardano.client.exception.CborSerializationException;
import io.psg.nativeassets.model.MintDetails;
import io.psg.nativeassets.model.NativeAsset;
import io.psg.nativeassets.model.Policy;
import io.psg.nativeassets.service.MultisigNativeAssetService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@Log
@RequestMapping("/multisig")
public class MultisigNativeAssetsController {
    @Autowired
    private MultisigNativeAssetService multisigNativeAssetService;

    @Autowired
    private ConversionService conversionService;

    @PostMapping("/policies/{name}/vkey/{vKey}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<Policy> createPolicyPubKey(
            @PathVariable String name,
            @PathVariable String vKey,
            @RequestParam(required = false) Integer beforeSlot,
            @RequestParam(required = false) Integer afterSlot) throws CborSerializationException {
        return multisigNativeAssetService.createPolicyWithPublicKeys(name, vKey, Optional.ofNullable(beforeSlot), Optional.ofNullable(afterSlot))
                .toCompletableFuture()
                .thenApply(p -> conversionService.convert(p, Policy.class));
    }


    @PostMapping("/policies/{name}/skey/{sKey}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<Policy> createPolicySecKey(
            @PathVariable String name,
            @PathVariable String sKey,
            @RequestParam(required = false) Integer beforeSlot,
            @RequestParam(required = false) Integer afterSlot) throws CborSerializationException {
        return multisigNativeAssetService.createPolicyWithSecKeys(name, sKey, Optional.ofNullable(beforeSlot), Optional.ofNullable(afterSlot))
                .toCompletableFuture()
                .thenApply(p -> conversionService.convert(p, Policy.class));
    }

    @GetMapping("/policies/id/{policyId}")
    public CompletableFuture<Policy> getPolicyById(@PathVariable String policyId) {
        return multisigNativeAssetService.getPolicyById(policyId)
                .toCompletableFuture()
                .thenApply(p -> conversionService.convert(p, Policy.class));
    }

    @GetMapping("/policies/name/{policyName}")
    public CompletableFuture<Policy> getPolicyByName(@PathVariable String policyName) {
        return multisigNativeAssetService.getPolicyByName(policyName)
                .toCompletableFuture()
                .thenApply(p -> conversionService.convert(p, Policy.class));
    }
//
    @GetMapping("/policies")
    public CompletableFuture<List<Policy>> listPolicies() {
        return multisigNativeAssetService.listPolices()
                .toCompletableFuture()
                .thenApply(l -> l.stream()
                        .map(p -> conversionService.convert(p, Policy.class))
                        .collect(Collectors.toList()));
    }

    @PostMapping("/addresses/vkey/{vKey}")
    @ResponseStatus(HttpStatus.CREATED)
    public Address createAddress(@PathVariable String vKey) throws CborSerializationException {
        return multisigNativeAssetService.generateAddress(vKey);

    }

    @PostMapping("/public-key/{sKey}")
    @ResponseStatus(HttpStatus.CREATED)
    public VerificationKey createVKey(String sKey) throws CborSerializationException {
        return multisigNativeAssetService.generateVerKey(sKey);

    }

    @PostMapping("/keys")
    @ResponseStatus(HttpStatus.CREATED)
    public Keys createKeys() throws CborSerializationException {
        return multisigNativeAssetService.generateKeys();

    }
//
//
//    @PostMapping("/mint")
//    @ResponseStatus(HttpStatus.CREATED)
//    public Address mint(@PathVariable String vKey) throws CborSerializationException {
//        return multisigNativeAssetService.m;
//
//    }






//    @PostMapping("/assets/{name}/policy/{policyId}")
//    @ResponseStatus(HttpStatus.CREATED)
//    public CompletableFuture<NativeAsset> createAsset(
//            @PathVariable String name,
//            @PathVariable String policyId) {
//        return multisigNativeAssetService.createNativeAsset(name, policyId)
//                .thenApply(p -> conversionService.convert(p, NativeAsset.class));
//    }
//
//    @GetMapping("/assets/{name}/policy/{policyId}")
//    public CompletableFuture<NativeAsset> getAsset(
//            @PathVariable String name,
//            @PathVariable String policyId) {
//        return multisigNativeAssetService.getNativeAsset(name, policyId)
//                .thenApply(a -> conversionService.convert(a, NativeAsset.class));
//    }
//
//    @GetMapping("/assets")
//    public CompletableFuture<List<NativeAsset>> listAssets() {
//        return multisigNativeAssetService.listNativeAssets()
//                .thenApply(l -> l.stream()
//                        .map(p -> conversionService.convert(p, NativeAsset.class))
//                        .collect(Collectors.toList()));
//    }
//
//    @DeleteMapping("/assets/{name}/policy/{policyId}")
//    public CompletableFuture<String> deleteAsset(
//            @PathVariable String name,
//            @PathVariable String policyId) {
//        return multisigNativeAssetService.deleteNativeAsset(name, policyId);
//    }
//
//    @PostMapping("/mint")
//    @ResponseStatus(HttpStatus.CREATED)
//    public CompletableFuture<String> mint(
//            @RequestBody MintDetails mintDetails) {
//        return multisigNativeAssetService.mintNativeAsset(mintDetails.getName(), mintDetails.getPolicyId(), mintDetails);
//    }


}