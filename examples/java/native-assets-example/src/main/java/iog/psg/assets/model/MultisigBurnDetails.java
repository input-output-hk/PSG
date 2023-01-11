package iog.psg.assets.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MultisigBurnDetails {
    private String name;
    private String policyId;

    private String targetAddress;
    private Long amount;

}
