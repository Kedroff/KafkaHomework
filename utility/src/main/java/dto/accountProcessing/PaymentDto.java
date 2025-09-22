package dto.accountProcessing;

import enums.accountProcessing.PaymentType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDto {

    @NotNull(message = "accountId не может быть null")
    @Positive(message = "accountId должен быть положительным")
    private Long accountId;

    @NotNull(message = "paymentDate не может быть null")
    @PastOrPresent(message = "paymentDate не может быть в будущем")
    private LocalDate paymentDate;

    @NotNull(message = "amount не может быть null")
    @DecimalMin(value = "0.01", message = "amount должен быть больше 0")
    @Digits(integer = 17, fraction = 2, message = "amount должен быть с точностью до копеек")
    private BigDecimal amount;

    @NotNull(message = "isCredit не может быть null")
    private Boolean isCredit;

    @PastOrPresent(message = "payedAt не может быть в будущем")
    private LocalDateTime payedAt;

    @NotNull(message = "type не может быть null")
    private PaymentType type;
}

