package com.paiya.paiya_api.model.enums;

/**
 * Type d'identifiant pour les plateformes de paiement
 */
public enum TypeIdentifiant {
    TELEPHONE("Numéro de téléphone"),
    EMAIL("Adresse email");

    private final String displayName;

    TypeIdentifiant(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
