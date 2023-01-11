package iog.psg.assets.converters;

import iog.psg.assets.model.NativeNft;
import org.springframework.core.convert.converter.Converter;
import scala.jdk.CollectionConverters;

import java.util.stream.Collectors;

@ConverterComponent
public class NativeNftConverter implements Converter<NativeNft, iog.psg.service.nativeassets.native_assets.Nft> {

    NativeNftFileConverter nativeNftFileConverter = new NativeNftFileConverter();
    @Override
    public iog.psg.service.nativeassets.native_assets.Nft convert(NativeNft nativeNft) {
        return iog.psg.service.nativeassets.native_assets.Nft.of(
                nativeNft.getAssetName(),
                nativeNft.getName(),
                CollectionConverters.CollectionHasAsScala(nativeNft.getImage()).asScala().toSeq(),
                nativeNft.getMediaType(),
                CollectionConverters.CollectionHasAsScala(nativeNft.getDescription()).asScala().toSeq(),
                CollectionConverters.CollectionHasAsScala(
                        nativeNft.getNativeNftFiles()
                                .stream()
                                .map(file -> nativeNftFileConverter.convert(file)).collect(Collectors.toList())
                ).asScala().toSeq()
        );
    }
}
