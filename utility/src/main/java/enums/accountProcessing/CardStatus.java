package enums.accountProcessing;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum CardStatus {
    OPENED("OPENED"),
    ACTIVE("ACTIVE"),
    CLOSED("CLOSED"),
    FROZEN("FROZEN"),
    ARRESTED("ARRESTED");

    private final String value;

    CardStatus(String value) {
        this.value = value;
    }

    public static CardStatus fromValue(String value) {
        return Arrays.stream(values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown CardStatus: " + value));
    }
}
