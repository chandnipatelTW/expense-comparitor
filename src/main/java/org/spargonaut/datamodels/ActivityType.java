package org.spargonaut.datamodels;

public enum ActivityType {
    SALE("Sale"),
    PAYMENT("Payment");

    private String value;

    ActivityType(String typeString) {
        this.value = typeString;
    }

    public String getValue() {
        return this.value;
    };

    public static ActivityType fromString(String typeString) {
        for (ActivityType activityType : ActivityType.values()) {
            if (activityType.getValue().equalsIgnoreCase(typeString)) {
                return activityType;
            }
        }
        throw new IllegalArgumentException("Failed parsing the Argument: ->" + typeString + "<-");
    }
}