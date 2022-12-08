package io.psg.nativeassets.controller;

import io.psg.nativeassets.service.NativeAssetsService;
import iog.psg.service.nativeassets.native_assets.NativeAsset;
import iog.psg.service.nativeassets.native_assets_service.Policy;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import scala.collection.immutable.List;
import java.util.Optional;

@RestController
@Log
public class NativeAssetsController {
    @Autowired
    private NativeAssetsService nativeAssetsService;

    @Autowired
    private ConversionService conversionService;

    @PostMapping("/policies/{name}")
    @ResponseStatus(HttpStatus.CREATED)
    public Policy createPolicy(
            @PathVariable String name,
            @RequestParam(required = false) Integer beforeSlot,
            @RequestParam(required = false) Integer afterSlot) {
        return nativeAssetsService.createPolicy(name, Optional.ofNullable(beforeSlot), Optional.ofNullable(afterSlot));
    }

    @GetMapping("/policies/{policyId}")
    public Policy getPolicy(@PathVariable String policyId) {
        return nativeAssetsService.getPolicy(policyId);
    }

    @GetMapping("/policies")
    public List<Policy> listPolicies() {
        return nativeAssetsService.listPolicies();

    }

    @DeleteMapping("/policies/{policyId}")
    public void deletePolicy(@PathVariable String policyId) {
        nativeAssetsService.deletePolicy(policyId);
    }

    @PostMapping("/assets/{name}/policy/{policyId}")
    @ResponseStatus(HttpStatus.CREATED)
    public NativeAsset createAsset(
            @PathVariable String name,
            @PathVariable String policyId) {
        return nativeAssetsService.createNativeAsset(name, policyId);
    }

    @GetMapping("/assets/{name}/policy/{policyId}")
    public NativeAsset getAsset(
            @PathVariable String name,
            @PathVariable String policyId) {
        return nativeAssetsService.getNativeAsset(name, policyId);
    }

    @GetMapping("/assets")
    public List<NativeAsset> listAssets() {
        return nativeAssetsService.listNativeAssets();
    }

    @DeleteMapping("/assets/{name}/policy/{policyId}")
    public void deleteAsset(
            @PathVariable String name,
            @PathVariable String policyId) {
        nativeAssetsService.deleteNativeAsset(name, policyId);
    }
}