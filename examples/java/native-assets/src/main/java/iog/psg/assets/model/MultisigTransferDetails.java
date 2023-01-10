package iog.psg.assets.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MultisigTransferDetails {
    private String name;
    private String policyId;

    private String fromAddress;
    private String toAddress;
    private Long amount;
    private Integer depth;

}
