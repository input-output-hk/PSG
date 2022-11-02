package io.psg.nativeassets.converters.request;

import io.psg.nativeassets.converters.ConverterComponent;
import io.psg.nativeassets.model.Nft;
import iog.psg.service.nativeassets.NftFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import scala.jdk.CollectionConverters;

import java.util.stream.Collectors;

@ConverterComponent
public class NftConverter implements Converter<Nft, iog.psg.service.nativeassets.Nft> {

    NftFileConverter nftFileConverter = new NftFileConverter();
    @Override
    public iog.psg.service.nativeassets.Nft convert(Nft nft) {
        return iog.psg.service.nativeassets.Nft.of(
                nft.getAssetName(),
                nft.getName(),
                CollectionConverters.CollectionHasAsScala(nft.getImage()).asScala().toSeq(),
                nft.getMediaType(),
                CollectionConverters.CollectionHasAsScala(nft.getDescription()).asScala().toSeq(),
                CollectionConverters.CollectionHasAsScala(
                        nft.getNftFiles()
                                .stream()
                                .map(file -> nftFileConverter.convert(file)).collect(Collectors.toList())
                ).asScala().toSeq()
        );
    }
}
