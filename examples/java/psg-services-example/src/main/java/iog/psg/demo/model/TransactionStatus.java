package iog.psg.demo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionStatus {
    private String id;
    private String state;
    private String metadata;
    private String errorCode;
    private String errorMessage;
}
