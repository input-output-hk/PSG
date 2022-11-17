package io.psg.nativeassets.model;

import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Data
@Builder
public class NativeAsset {
    private String name;
    private String policyId;
    private Long amount;
}
