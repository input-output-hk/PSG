package iog.psg.assets.converters;

import iog.psg.service.nativeassets.native_assets.NativeAsset;
import org.springframework.core.convert.converter.Converter;

@ConverterComponent
public class NativeAssetConverter implements Converter<NativeAsset, iog.psg.assets.model.NativeAsset> {
    @Override
    public iog.psg.assets.model.NativeAsset convert(NativeAsset asset) {
        return iog.psg.assets.model.NativeAsset.builder()
                .name(asset.id().map(id -> id.name()).get())
                .policyId(asset.id().map(id -> id.policyId()).get())
                .amount(asset.amount())
                .build();
    }
}
