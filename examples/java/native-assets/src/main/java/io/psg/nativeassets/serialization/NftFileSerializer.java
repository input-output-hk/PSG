//package io.psg.nativeassets.serialization;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import io.psg.nativeassets.model.NftFile;
//import org.springframework.boot.jackson.JsonComponent;
//
//import java.io.IOException;
//
//@JsonComponent
//public class NftFileSerializer extends JsonSerializer<NftFile> {
//
//    @Override
//    public void serialize(NftFile nftFile, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
//        jgen.writeStartObject();
//        jgen.writeStringField("name", nftFile.getName());
//        jgen.writeStringField("mediaType", nftFile.getMediaType());
//        jgen.writeArrayFieldStart("src");
//        for (String src : nftFile.getSrcs()) {
//            jgen.writeString(src);
//        }
//
//        jgen.writeEndArray();
//        jgen.writeEndObject();
//    }
//}
