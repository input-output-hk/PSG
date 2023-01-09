package io.psg.nativeassets.converters;

import iog.psg.service.nativeassets.native_assets.NativeAsset;
import org.springframework.core.convert.converter.Converter;

@ConverterComponent
public class NativeAssetConverter implements Converter<NativeAsset, io.psg.nativeassets.model.NativeAsset> {

    @Override
    public io.psg.nativeassets.model.NativeAsset convert(NativeAsset asset) {
        return io.psg.nativeassets.model.NativeAsset.builder()
                .name(asset.id().map(id -> id.name()).get())
                .policyId(asset.id().map(id -> id.policyId()).get())
                .amount(asset.amount())
                .build();
    }
}
