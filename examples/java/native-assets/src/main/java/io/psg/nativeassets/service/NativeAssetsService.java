package io.psg.nativeassets.service;


import iog.psg.client.nativeassets.NativeAssetsApi;
import iog.psg.service.nativeassets.native_assets.NativeAsset;
import iog.psg.service.nativeassets.native_assets.NativeAssetId;
import iog.psg.service.nativeassets.native_assets_service.Policy;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import scala.collection.immutable.List;

import java.util.Optional;

@Service
@Log
public class NativeAssetsService {
    @Autowired
    private NativeAssetsApi nativeAssetsApi;
    @Autowired
    private ConversionService conversionService;

    public Policy createPolicy(String name, Optional<Integer> before, Optional<Integer> after) {
        return nativeAssetsApi.createPolicy(name, before, after).getPolicy();
    }

    public Policy getPolicy(String policyId) {
        return nativeAssetsApi.getPolicy(policyId).getPolicy();
    }

    public List<Policy> listPolicies() {
        return nativeAssetsApi.getPolices().policies().toList();

    }

    public void deletePolicy(String policyId) {
        nativeAssetsApi.deletePolicy(policyId);
    }


    public NativeAsset createNativeAsset(String name, String policyId) {
        return nativeAssetsApi.createAsset(NativeAssetId.of(name, policyId)).getAsset();
    }

    public NativeAsset getNativeAsset(String name, String policyId) {
        return nativeAssetsApi.getAsset(NativeAssetId.of(name, policyId)).getAsset();
    }

    public List<NativeAsset> listNativeAssets() {
        return nativeAssetsApi.listAssets().assets().toList();

    }

    public void deleteNativeAsset(String name, String policyId) {
        nativeAssetsApi.deleteAsset(NativeAssetId.of(name, policyId));
    }
}
