package iog.psg.assets.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NativeBurnDetails {
    private String name;
    private String policyId;
    private Long amount;
    private Integer depth;
}
