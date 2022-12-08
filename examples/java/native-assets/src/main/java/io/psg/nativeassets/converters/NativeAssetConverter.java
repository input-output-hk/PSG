package io.psg.nativeassets.converters;


import iog.psg.service.nativeassets.native_assets.NativeAsset;
import iog.psg.service.nativeassets.native_assets.NativeAssetId;
import org.springframework.core.convert.converter.Converter;

@ConverterComponent
public class NativeAssetConverter implements Converter<NativeAsset, io.psg.nativeassets.model.NativeAsset> {

    @Override
    public io.psg.nativeassets.model.NativeAsset convert(NativeAsset asset) {
        return io.psg.nativeassets.model.NativeAsset.builder()
                .name(asset.id().map(NativeAssetId::name).get())
                .policyId(asset.id().map(NativeAssetId::policyId).get())
                .amount(asset.amount())
                .build();
    }
}
