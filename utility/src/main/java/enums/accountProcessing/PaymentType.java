package enums.accountProcessing;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum PaymentType {
    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL"),
    TRANSFER("TRANSFER");

    private final String value;

    PaymentType(String value) {
        this.value = value;
    }

    public static PaymentType fromValue(String value) {
        return Arrays.stream(values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown PaymentType: " + value));
    }
}
