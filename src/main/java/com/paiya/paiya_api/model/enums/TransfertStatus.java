package com.paiya.paiya_api.model.enums;

/**
 * Statuts d'un transfert de billet
 */
public enum TransfertStatus {
    EN_ATTENTE("En attente d'acceptation"),
    ACCEPTE("Accepté"),
    REFUSE("Refusé"),
    EXPIRE("Expiré");

    private final String displayName;

    TransfertStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
