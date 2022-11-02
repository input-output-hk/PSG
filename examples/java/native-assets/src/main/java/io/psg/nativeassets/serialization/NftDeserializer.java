//package io.psg.nativeassets.serialization;
//
//import com.fasterxml.jackson.core.JacksonException;
//import com.fasterxml.jackson.core.JsonParser;
//import com.fasterxml.jackson.core.TreeNode;
//import com.fasterxml.jackson.databind.DeserializationContext;
//import com.fasterxml.jackson.databind.JsonDeserializer;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import io.psg.nativeassets.model.Nft;
//import org.springframework.boot.jackson.JsonComponent;
//
//import java.io.IOException;
//
//import static io.psg.nativeassets.serialization.SerializationUtil.TRANSACTION_META_DATUM_LABEL;
//@JsonComponent
//public class NftDeserializer extends JsonDeserializer<Nft> {
//
//    @Override
//    public Nft deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
//        TreeNode treeNode = jsonParser.getCodec().readTree(jsonParser);
//        ObjectNode policyNode =  ((ObjectNode) treeNode.get(TRANSACTION_META_DATUM_LABEL)).objectNode();
//        System.out.println("pico" + policyNode);
//
//        return Nft.builder()
//                .build();
//    }
//}
