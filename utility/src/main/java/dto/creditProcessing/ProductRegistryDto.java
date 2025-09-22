package dto.creditProcessing;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRegistryDto {

    @NotNull(message = "clientId не может быть null")
    @Positive(message = "clientId должен быть положительным")
    private Long clientId;

    @NotNull(message = "accountId не может быть null")
    @Positive(message = "accountId должен быть положительным")
    private Long accountId;

    @NotNull(message = "productId не может быть null")
    @Positive(message = "productId должен быть положительным")
    private Long productId;

    @NotNull(message = "interestRate не может быть null")
    @DecimalMin(value = "0.00", message = "interestRate не может быть отрицательной")
    @DecimalMax(value = "100.00", message = "interestRate не может быть больше 100%")
    @Digits(integer = 3, fraction = 2, message = "interestRate должно иметь максимум 3 цифры до запятой и 2 после")
    private BigDecimal interestRate;

    @NotNull(message = "openDate не может быть null")
    @PastOrPresent(message = "openDate не может быть в будущем")
    private LocalDate openDate;
}
