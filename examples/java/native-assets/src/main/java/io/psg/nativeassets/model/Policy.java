package io.psg.nativeassets.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Policy {
    private String name;
    private String policyId;
    private String paymentAddress;
    private Long paymentAddressAmount;
}
