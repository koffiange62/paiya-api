package com.paiya.paiya_api.model.enums;

/**
 * Statuts d'un événement
 */
public enum EventStatus {
    BROUILLON("En cours de création"),
    PUBLIE("Publié et visible"),
    ANNULE("Annulé"),
    TERMINE("Terminé"),
    ARCHIVE("Archivé");

    private final String displayName;

    EventStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
