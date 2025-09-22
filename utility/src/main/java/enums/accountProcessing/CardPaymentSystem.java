package enums.accountProcessing;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum CardPaymentSystem {
    VISA("VISA"),
    MASTERCARD("MASTERCARD"),
    MIR("MIR");

    private final String value;

    CardPaymentSystem(String value) {
        this.value = value;
    }

    public static CardPaymentSystem fromValue(String value) {
        return Arrays.stream(values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown CardPaymentSystem: " + value));
    }
}
