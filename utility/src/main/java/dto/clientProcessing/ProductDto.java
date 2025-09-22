package dto.clientProcessing;

import enums.clientProcessing.ProductKey;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDto {

    @NotBlank(message = "name не может быть пустым")
    @Size(max = 50)
    private String name;

    @NotNull(message = "key обязателен")
    private ProductKey key;

    @NotNull(message = "createDate обязателен")
    private LocalDate createDate;

    @Size(max = 50)
    private String productId;
}
