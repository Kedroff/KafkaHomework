package dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpLogMessage {
    private String timestamp;
    private String methodSignature;
    private String uri;
    private String parameters;
    private String body;
}
