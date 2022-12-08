package io.psg.nativeassets.model;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MintDetails {
    private String name;
    private String policyId;
    private Long amount;
    private Integer depth;
    private List<Nft> nfts;

}
