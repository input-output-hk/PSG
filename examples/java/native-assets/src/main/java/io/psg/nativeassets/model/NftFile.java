package io.psg.nativeassets.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NftFile {
    private String name;
    private String mediaType;
    private List<String> srcs;

}
