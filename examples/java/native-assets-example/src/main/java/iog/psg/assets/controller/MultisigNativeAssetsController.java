package iog.psg.assets.controller;

import com.bloxbean.cardano.client.exception.CborSerializationException;
import iog.psg.assets.model.*;
import iog.psg.assets.service.MultisigNativeAssetService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
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
    public CompletableFuture<MultisigPolicy> createPolicyPubKey(
            @PathVariable String name,
            @PathVariable String vKey,
            @RequestParam(required = false) Integer beforeSlot,
            @RequestParam(required = false) Integer afterSlot) throws CborSerializationException {
        return multisigNativeAssetService.createPolicyWithPublicKeys(name, vKey, Optional.ofNullable(beforeSlot), Optional.ofNullable(afterSlot))
                .toCompletableFuture()
                .thenApply(p -> conversionService.convert(p, MultisigPolicy.class));
    }

    @GetMapping("/policies/id/{policyId}")
    public CompletableFuture<MultisigPolicy> getPolicyById(@PathVariable String policyId) {
        return multisigNativeAssetService.getPolicyById(policyId)
                .toCompletableFuture()
                .thenApply(p -> conversionService.convert(p, MultisigPolicy.class));
    }

    @GetMapping("/policies/name/{policyName}")
    public CompletableFuture<MultisigPolicy> getPolicyByName(@PathVariable String policyName) {
        return multisigNativeAssetService.getPolicyByName(policyName)
                .toCompletableFuture()
                .thenApply(p -> conversionService.convert(p, MultisigPolicy.class));
    }

    @GetMapping("/policies")
    public CompletableFuture<List<MultisigPolicy>> listPolicies() {
        return multisigNativeAssetService.listPolices()
                .toCompletableFuture()
                .thenApply(l -> l.stream()
                        .map(p -> conversionService.convert(p, MultisigPolicy.class))
                        .collect(Collectors.toList()));
    }

    @PostMapping("/mint/nft")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<String> mintNft(
            @RequestBody MultisigNftMintDetails multisigNftMintDetails) {
        return multisigNativeAssetService.createMintTransaction(
                        multisigNftMintDetails.getPolicyId(),
                        multisigNftMintDetails.getName(),
                        multisigNftMintDetails.getPaymentAddress(),
                        multisigNftMintDetails.getMintTargetAddress())
                .toCompletableFuture();
    }

    @PostMapping("/mint")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<String> mint(
            @RequestBody MultisigMintDetails mintDetails) {
        return multisigNativeAssetService.createMintTransactionWithArbitraryMetadata(
                        mintDetails.getPolicyId(),
                        mintDetails.getName(),
                        mintDetails.getPaymentAddress(),
                        mintDetails.getMintTargetAddress(),
                        mintDetails.getMetadata(),
                        mintDetails.getAmount())
                .toCompletableFuture();
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<String> transfer(
            @RequestBody MultisigTransferDetails multisigTransferDetails) {
        return multisigNativeAssetService.createTransferTransaction(
                        multisigTransferDetails.getPolicyId(),
                        multisigTransferDetails.getName(),
                        multisigTransferDetails.getFromAddress(),
                        multisigTransferDetails.getToAddress(),
                        multisigTransferDetails.getAmount())
                .toCompletableFuture();
    }

    @PostMapping("/burn")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<String> burn(
            @RequestBody MultisigBurnDetails multisigBurnDetails) {
        return multisigNativeAssetService.createBurnTransaction(
                        multisigBurnDetails.getPolicyId(),
                        multisigBurnDetails.getName(),
                        multisigBurnDetails.getTargetAddress(),
                        multisigBurnDetails.getAmount())
                .toCompletableFuture();
    }

    @PostMapping("/witnesses/{txId}/{vKey}/{sig}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<String> addWitnessWithSecretKey(
            @PathVariable String txId,
            @PathVariable String vKey,
            @PathVariable String sig)
            throws CborSerializationException {
        return multisigNativeAssetService.addWitnessWithPublicKey(txId, vKey, sig)
                .toCompletableFuture();
    }

    @GetMapping("/witnesses/{txId}")
    public CompletionStage<List<String>> witnesses(@PathVariable String txId) {
        return multisigNativeAssetService.listWitnesses(txId)
                .toCompletableFuture();
    }

    @GetMapping("/txs")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<List<String>> getTxs() {
        return multisigNativeAssetService.listTxs()
                .toCompletableFuture();
    }

    @GetMapping("/txs/txId/{txId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<Long> getTx(
            @PathVariable String txId) throws CborSerializationException {
        return multisigNativeAssetService.getTx(txId)
                .toCompletableFuture();
    }

    @GetMapping("/txs/txId/{policyId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CompletableFuture<List<String>> getTxByPolicyId(@PathVariable String policyId) {
        return multisigNativeAssetService.listTxsByPolicyId(policyId)
                .toCompletableFuture();
    }



    @PostMapping("/txs/{txId}/submit")
    public CompletionStage<String> send(@PathVariable String txId,
                                        @RequestParam Optional<Integer> depth) {
        return multisigNativeAssetService.sendTransaction(txId, depth.orElse(4))
                .toCompletableFuture();
    }

}