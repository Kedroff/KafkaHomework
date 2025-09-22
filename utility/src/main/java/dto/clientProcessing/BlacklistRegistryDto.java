package dto.clientProcessing;

import enums.clientProcessing.DocumentType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlacklistRegistryDto {

    @NotNull(message = "documentType обязателен")
    private DocumentType documentType;

    @NotBlank(message = "documentId обязателен")
    @Size(max = 30, message = "documentId не должен быть длиннее 30 символов")
    @Pattern(regexp = "^[A-Za-z0-9\\- ]+$", message = "documentId содержит недопустимые символы")
    private String documentId;

    @NotNull(message = "blacklistedAt обязателен")
    @PastOrPresent(message = "blacklistedAt не может быть в будущем")
    private LocalDateTime blacklistedAt;

    @Size(max = 128, message = "reason не более 128 символов")
    private String reason;

    private LocalDateTime blacklistExpirationDate;

    @AssertTrue(message = "blacklistExpirationDate должна быть позже blacklistedAt")
    private boolean isExpirationValid() {
        if (blacklistExpirationDate == null) return true;
        if (blacklistedAt == null) return true;
        return blacklistExpirationDate.isAfter(blacklistedAt);
    }
}
