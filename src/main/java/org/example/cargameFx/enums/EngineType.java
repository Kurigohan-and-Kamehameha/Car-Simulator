package org.example.cargameFx.enums;

public enum EngineType {
    FUEL("Fuel"),
    ELECTRIC("Electric");

    private final String displayName;

    EngineType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static EngineType fromDisplayName(String text) {
        for (EngineType type : values()) {
            if (type.toString().equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }
}
