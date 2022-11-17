package io.psg.nativeassets.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Nft {
    private String assetName;
    private String name;
    private List<String> image;
    private String mediaType;
    private List<String> description;
    private List<NftFile> nftFiles;


}
