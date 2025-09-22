package enums.accountProcessing;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum AccountStatus {
    ACTIVE("ACTIVE"),
    CLOSED("CLOSED"),
    BLOCKED("BLOCKED"),
    ARRESTED("ARRESTED");

    private final String value;

    AccountStatus(String value) {
        this.value = value;
    }

    public static AccountStatus fromValue(String value) {
        return Arrays.stream(values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown AccountStatus: " + value));
    }
}
