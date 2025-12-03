package com.paiya.paiya_api.model.enums;

/**
 * Rôles des utilisateurs dans la plateforme
 */
public enum UserRole {
    PARTICIPANT("Participant"),
    PROMOTEUR("Promoteur d'événements"),
    ADMIN("Administrateur"),
    VALIDATEUR("Validateur de billets");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
