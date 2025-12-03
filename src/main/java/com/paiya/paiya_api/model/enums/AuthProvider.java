package com.paiya.paiya_api.model.enums;

/**
 * Enumération des fournisseurs d'authentification
 */
public enum AuthProvider {
    LOCAL("Authentification locale"),
    GOOGLE("Google OAuth"),
    FACEBOOK("Facebook OAuth"),
    APPLE("Apple Sign In");

    private final String displayName;

    AuthProvider(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
