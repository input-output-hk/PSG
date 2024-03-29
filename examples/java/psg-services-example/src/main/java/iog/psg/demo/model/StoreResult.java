package iog.psg.demo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoreResult {
    private String hash;
    private String url;
    private String errorCode;
    private String errorMessage;
}
