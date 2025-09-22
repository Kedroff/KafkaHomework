package dto.accountProcessing;

import enums.accountProcessing.AccountStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDto {

    @NotNull(message = "clientId не может быть null")
    @Positive(message = "clientId должен быть положительным")
    private Long clientId;

    @NotNull(message = "productId не может быть null")
    @Positive(message = "productId должен быть положительным")
    private Long productId;

    @NotNull(message = "balance не может быть null")
    @DecimalMin(value = "0.00", message = "balance не может быть отрицательным")
    @Digits(integer = 19, fraction = 2, message = "balance должен быть с точностью до копеек")
    private BigDecimal balance;

    @NotNull(message = "interestRate не может быть null")
    @DecimalMin(value = "0.00", message = "interestRate не может быть отрицательной")
    @DecimalMax(value = "100.00", message = "interestRate не может быть больше 100%")
    @Digits(integer = 3, fraction = 2, message = "interestRate должно иметь максимум 3 цифры до запятой и 2 после")
    private BigDecimal interestRate;

    @NotNull(message = "isRecalc не может быть null")
    private Boolean isRecalc;

    @NotNull(message = "cardExist не может быть null")
    private Boolean cardExist;

    @NotNull(message = "status не может быть null")
    private AccountStatus status;
}
