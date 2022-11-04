package io.psg.nativeassets.converters.request;

import io.psg.nativeassets.converters.ConverterComponent;
import io.psg.nativeassets.model.NftFile;
import org.springframework.core.convert.converter.Converter;
import scala.jdk.CollectionConverters;

@ConverterComponent
public class NftFileConverter implements Converter<NftFile, iog.psg.service.nativeassets.NftFile> {

    @Override
    public iog.psg.service.nativeassets.NftFile convert(NftFile nftFile) {
        return iog.psg.service.nativeassets.NftFile.of(
                nftFile.getName(),
                nftFile.getMediaType(),
                CollectionConverters.CollectionHasAsScala(nftFile.getSrcs()).asScala().toSeq()
        );
    }
}
