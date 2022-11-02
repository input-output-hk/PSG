//package io.psg.nativeassets.serialization;
//
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.JsonSerializer;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import io.psg.nativeassets.model.Nft;
//import io.psg.nativeassets.model.NftFile;
//import org.springframework.boot.jackson.JsonComponent;
//
//import java.io.IOException;
//
//import static io.psg.nativeassets.serialization.SerializationUtil.TRANSACTION_META_DATUM_LABEL;
//@JsonComponent
//public class NftSerializer extends JsonSerializer<Nft> {
//
//
//    @Override
//    public void serialize(Nft nft,
//                          JsonGenerator jsonGenerator,
//                          SerializerProvider serializerProvider) throws IOException {
//        jsonGenerator.writeStartObject();
//        jsonGenerator.writeObjectFieldStart(TRANSACTION_META_DATUM_LABEL);
//        jsonGenerator.writeObjectFieldStart(nft.getAssetName());
//        jsonGenerator.writeStringField("name", nft.getName());
//        jsonGenerator.writeArrayFieldStart("image");
//        for (String image: nft.getImage()) {
//            jsonGenerator.writeString(image);
//        }
//
//        jsonGenerator.writeEndArray();
//        jsonGenerator.writeStringField("mediaType", nft.getMediaType());
//        jsonGenerator.writeArrayFieldStart("description");
//        for (String desc: nft.getDescription()) {
//            jsonGenerator.writeString(desc);
//        }
//
//        jsonGenerator.writeEndArray();
//        jsonGenerator.writeArrayFieldStart("files");
//        for (NftFile nftFile: nft.getNftFiles()) {
//            jsonGenerator.writeObject(nftFile);
//        }
//
//        jsonGenerator.writeEndArray();
//        jsonGenerator.writeEndObject();
//        jsonGenerator.writeEndObject();
//        jsonGenerator.writeEndObject();
//        jsonGenerator.writeEndObject();
//    }
//}
