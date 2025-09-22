package enums.clientProcessing;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum ClientProductStatus {
    ACTIVE("ACTIVE"),
    CLOSED("CLOSED"),
    BLOCKED("BLOCKED"),
    ARRESTED("ARRESTED");

    private final String value;

    ClientProductStatus(String value) {
        this.value = value;
    }

    public static ClientProductStatus fromValue(String value) {
        return Arrays.stream(values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ClientProductStatus: " + value));
    }
}
