package dto.clientProcessing;

import enums.clientProcessing.ClientProductStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import jakarta.validation.constraints.AssertTrue;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientProductDto {

    @NotNull(message = "clientId обязателен")
    @Positive(message = "clientId должен быть положительным")
    private Long clientId;

    @NotNull(message = "productId обязателен")
    @Positive(message = "productId должен быть положительным")
    private Long productId;

    @NotNull(message = "openDate обязателен")
    private LocalDate openDate;

    private LocalDate closeDate;

    @NotNull(message = "status обязателен")
    private ClientProductStatus status;

    @AssertTrue(message = "closeDate должен быть не раньше openDate") // надеюсь это тоже имеет место быть
    private boolean isCloseDateValid() {
        if (closeDate == null) return true;
        if (openDate == null) return true;
        return !closeDate.isBefore(openDate);
    }
}
