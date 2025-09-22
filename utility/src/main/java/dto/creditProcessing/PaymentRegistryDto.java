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
public class PaymentRegistryDto {

    @NotNull(message = "productRegistryId не может быть null")
    @Positive(message = "productRegistryId должен быть положительным")
    private Long productRegistryId;

    @NotNull(message = "paymentDate не может быть null")
    @PastOrPresent(message = "paymentDate не может быть в будущем")
    private LocalDate paymentDate;

    @NotNull(message = "amount не может быть null")
    @DecimalMin(value = "0.01", message = "amount должен быть больше 0")
    @Digits(integer = 17, fraction = 2, message = "amount должен быть с точностью до копеек")
    private BigDecimal amount;

    @NotNull(message = "interestRateAmount не может быть null")
    @DecimalMin(value = "0.00", message = "interestRateAmount не может быть отрицательной")
    @Digits(integer = 17, fraction = 2, message = "interestRateAmount должен быть с точностью до копеек")
    private BigDecimal interestRateAmount;

    @NotNull(message = "debtAmount не может быть null")
    @DecimalMin(value = "0.00", message = "debtAmount не может быть отрицательной")
    @Digits(integer = 17, fraction = 2, message = "debtAmount должен быть с точностью до копеек")
    private BigDecimal debtAmount;

    @NotNull(message = "expired не может быть null")
    private Boolean expired;

    @NotNull(message = "paymentExpirationDate не может быть null")
    @FutureOrPresent(message = "paymentExpirationDate должна быть в будущем или сегодня")
    private LocalDate paymentExpirationDate;
}
