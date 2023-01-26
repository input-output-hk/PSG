package iog.psg.assets.model;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NativeNftFile {
    private String name;
    private String mediaType;
    private List<String> srcs;

}
