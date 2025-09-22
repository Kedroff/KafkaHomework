package enums.clientProcessing;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum DocumentType {
    PASSPORT("PASSPORT"),
    INT_PASSPORT("INT_PASSPORT"),
    BIRTH_CERT("BIRTH_CERT");

    private final String value;

    DocumentType(String value) {
        this.value = value;
    }

    public static DocumentType fromValue(String value) {
        return Arrays.stream(values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown DocumentType: " + value));
    }
}
