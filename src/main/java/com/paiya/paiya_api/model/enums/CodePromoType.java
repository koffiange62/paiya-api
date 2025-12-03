package com.paiya.paiya_api.model.enums;

/**
 * Types de code promo
 */
public enum CodePromoType {
    POURCENTAGE("Réduction en pourcentage"),
    MONTANT_FIXE("Montant fixe de réduction");

    private final String displayName;

    CodePromoType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
