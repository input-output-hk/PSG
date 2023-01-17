package iog.psg.assets.model;

import com.google.protobuf.Value;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class MultisigMintDetails {
    private String name;
    private String policyId;
    private Long amount;
    private String paymentAddress;
    private String mintTargetAddress;

    private Map<String, Value> metadata;

}
