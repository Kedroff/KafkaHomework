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
public class RegistrationRequest {

    @NotBlank @Size(max = 20)
    private String login;

    @NotBlank @Size(max = 20)
    private String password;

    @NotBlank @Email @Size(max = 64)
    private String email;

    @NotBlank @Size(max = 20)
    private String firstName;

    @Size(max = 20)
    private String middleName;

    @NotBlank @Size(max = 20)
    private String lastName;

    @NotNull @Past
    private LocalDate dateOfBirth;

    @NotNull
    private DocumentType documentType;

    @NotBlank @Size(max = 30)
    private String documentId;

    @Size(max = 10)
    private String documentPrefix;

    @Size(max = 10)
    private String documentSuffix;
}


