//package io.psg.nativeassets.serialization;
//
//import com.fasterxml.jackson.core.JacksonException;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.TreeNode;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.node.ArrayNode;
//import com.fasterxml.jackson.databind.node.TextNode;
//import io.psg.nativeassets.model.NftFile;
//import org.springframework.boot.jackson.JsonComponent;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//@JsonComponent
//public class NftFileDeserializer extends JsonDeserializer<NftFile> {
//
//
//    @Override
//    public NftFile deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
//        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
//        String name = ((TextNode) treeNode.get("name")).asText();
//        String mediaType = ((TextNode) treeNode.get("mediaType")).asText();
//        List<String> srcs = new ArrayList<String>();
//        for (Iterator<JsonNode> it = ((ArrayNode) treeNode.get("srcs")).elements(); it.hasNext(); ) {
//            JsonNode src = it.next();
//            srcs.add(src.asText());
//        }
//        return NftFile
//                .builder()
//                .name(name)
//                .mediaType(mediaType)
//                .srcs(srcs)
//                .build();
//    }
//}
