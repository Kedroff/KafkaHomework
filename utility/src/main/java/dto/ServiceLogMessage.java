package dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceLogMessage {
    private String timestamp;
    private String methodSignature;
    private String exceptionText;
    private String stacktrace;
    private String methodArgs;
}
