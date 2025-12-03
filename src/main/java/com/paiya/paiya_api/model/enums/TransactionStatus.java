package com.paiya.paiya_api.model.enums;

/**
 * Statuts d'une transaction
 */
public enum TransactionStatus {
    EN_ATTENTE("En attente de paiement"),
    CONFIRME("Paiement confirmé"),
    ECHOUE("Paiement échoué"),
    REMBOURSE("Remboursé"),
    ANNULE("Annulé");

    private final String displayName;

    TransactionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
