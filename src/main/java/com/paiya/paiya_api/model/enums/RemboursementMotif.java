package com.paiya.paiya_api.model.enums;

/**
 * Motifs de remboursement
 */
public enum RemboursementMotif {
    ANNULATION_EVENT("Annulation de l'événement"),
    DEMANDE_CLIENT("Demande du client"),
    ERREUR_PAIEMENT("Erreur de paiement"),
    AUTRE("Autre motif");

    private final String displayName;

    RemboursementMotif(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
