package enums.accountProcessing;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum TransactionStatus {
    OPENED("OPENED"),
    ACTIVE("ACTIVE"),
    CLOSED("CLOSED"),
    FROZEN("FROZEN"),
    ARRESTED("ARRESTED");

    private final String value;

    TransactionStatus(String value) {
        this.value = value;
    }

    public static TransactionStatus fromValue(String value) {
        return Arrays.stream(values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown TransactionStatus: " + value));
    }
}
