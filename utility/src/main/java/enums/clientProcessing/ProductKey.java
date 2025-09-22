package enums.clientProcessing;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum ProductKey {
    DC("DC"),
    CC("CC"),
    AC("AC"),
    IPO("IPO"),
    PC("PC"),
    PENS("PENS"),
    NS("NS"),
    INS("INS"),
    BS("BS");

    private final String value;

    ProductKey(String value) {
        this.value = value;
    }

    public static ProductKey fromValue(String value) {
        return Arrays.stream(values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown ProductKey: " + value));
    }
}
