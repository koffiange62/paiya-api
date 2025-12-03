package com.paiya.paiya_api.model.enums;

/**
 * Statuts d'une demande de remboursement
 */
public enum RemboursementStatus {
    EN_ATTENTE("En attente de traitement"),
    ACCEPTE("Accepté"),
    REFUSE("Refusé"),
    TRAITE("Traité");

    private final String displayName;

    RemboursementStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}