package iog.psg.assets.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NativeTransferDetails {
    private String name;
    private String policyId;
    private String toAddress;
    private Long amount;
    private Integer depth;
}
