package iog.psg.assets.converters;

import iog.psg.assets.model.NativeNftFile;
import org.springframework.core.convert.converter.Converter;
import scala.jdk.CollectionConverters;

@ConverterComponent
public class NativeNftFileConverter implements Converter<NativeNftFile, iog.psg.service.nativeassets.native_assets.NftFile> {

    @Override
    public iog.psg.service.nativeassets.native_assets.NftFile convert(NativeNftFile nativeNftFile) {
        return iog.psg.service.nativeassets.native_assets.NftFile.of(
                nativeNftFile.getName(),
                nativeNftFile.getMediaType(),
                CollectionConverters.CollectionHasAsScala(nativeNftFile.getSrcs()).asScala().toSeq()
        );
    }
}
