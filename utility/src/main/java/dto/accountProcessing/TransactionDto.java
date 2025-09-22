package dto.accountProcessing;

import enums.accountProcessing.TransactionStatus;
import enums.accountProcessing.TransactionType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {

    @NotNull(message = "accountId не может быть null")
    @Positive(message = "accountId должен быть положительным")
    private Long accountId;

    @Positive(message = "cardId должен быть положительным")
    private Long cardId;

    @NotNull(message = "type не может быть null")
    private TransactionType type;

    @NotNull(message = "amount не может быть null")
    @DecimalMin(value = "0.01", message = "amount должен быть больше 0")
    @Digits(integer = 19, fraction = 2, message = "amount должен быть с точностью до копеек")
    private BigDecimal amount;

    @NotNull(message = "status не может быть null")
    private TransactionStatus status;

    @NotNull(message = "timestamp не может быть null")
    @PastOrPresent(message = "timestamp не может быть в будущем")
    private LocalDateTime timestamp;
}

