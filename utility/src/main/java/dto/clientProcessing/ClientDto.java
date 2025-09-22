package dto.clientProcessing;

import enums.clientProcessing.DocumentType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {

    @NotBlank(message = "clientId обязателен")
    @Pattern(regexp = "^(\\d{2})(\\d{2})(\\d{8})$",    // регулярки это ужас конечно, надеюсь такой формат подойдет? :)
            message = "clientId должен иметь формат XXFFNNNNNNNN (12 цифр)")
    private String clientId;

    @NotNull(message = "userId обязателен")
    @Positive(message = "userId должен быть положительным")
    private Long userId;

    @NotBlank(message = "firstName не может быть пустым")
    @Size(max = 20)
    private String firstName;

    @Size(max = 20)
    private String middleName;

    @NotBlank(message = "lastName не может быть пустым")
    @Size(max = 20)
    private String lastName;

    @NotNull(message = "dateOfBirth обязателен")
    @Past(message = "dateOfBirth должен быть в прошлом")
    private LocalDate dateOfBirth;

    @NotNull(message = "documentType обязателен")
    private DocumentType documentType;

    @NotBlank(message = "documentId обязателен")
    @Size(max = 30, message = "documentId не должен быть длиннее 30 символов")
    @Pattern(regexp = "^[A-Za-z0-9\\- ]+$", message = "documentId содержит недопустимые символы")
    private String documentId;

    @Size(max = 10, message = "documentPrefix не более 10 символов")
    private String documentPrefix;

    @Size(max = 10, message = "documentSuffix не более 10 символов")
    private String documentSuffix;
}

