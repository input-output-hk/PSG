package iog.psg.assets.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class NativeMintDetails {
    private String name;
    private String policyId;
    private Long amount;
    private Integer depth;
    private List<NativeNft> nativeNfts;

}
