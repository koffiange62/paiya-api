package com.paiya.paiya_api.model.enums;

/**
 * Canaux de notification
 */
public enum NotificationCanal {
    EMAIL("Email"),
    SMS("SMS"),
    PUSH("Notification push"),
    IN_APP("Dans l'application");

    private final String displayName;

    NotificationCanal(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
