package dto.accountProcessing;

import enums.accountProcessing.CardPaymentSystem;
import enums.accountProcessing.CardStatus;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardDto {

    @NotNull(message = "accountId не может быть null")
    @Positive(message = "accountId должен быть положительным")
    private Long accountId;

    @NotBlank(message = "cardId не может быть пустым")  // cardId это же номер карты как я понимаю? :)
    @Pattern(
            regexp = "^[0-9]{4}-[0-9]{4}-[0-9]{4}-[0-9]{4}$",
            message = "cardId должен быть в формате XXXX-XXXX-XXXX-XXXX"
    )
    private String cardId;

    @NotNull(message = "paymentSystem не может быть null")
    private CardPaymentSystem paymentSystem;

    @NotNull(message = "status не может быть null")
    private CardStatus status;
}

