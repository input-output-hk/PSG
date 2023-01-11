package iog.psg.assets.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MultisigPolicy {
    private String name;
    private String policyId;
    private String paymentAddress;
    private Long paymentAddressAmount;
}
