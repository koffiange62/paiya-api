package com.paiya.paiya_api.model.enums;

/**
 * Types de notifications
 */
public enum NotificationType {
    NOUVEL_EVENT("Nouvel événement disponible"),
    RAPPEL("Rappel d'événement"),
    CONFIRMATION("Confirmation d'achat"),
    ANNULATION("Annulation d'événement"),
    REMBOURSEMENT("Remboursement effectué"),
    TRANSFERT("Transfert de billet"),
    PROMOTION("Promotion ou code promo");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
