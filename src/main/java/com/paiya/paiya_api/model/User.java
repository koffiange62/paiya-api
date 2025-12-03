package com.paiya.paiya_api.model;

import java.time.LocalDateTime;

import com.paiya.paiya_api.model.enums.AuthProvider;
import com.paiya.paiya_api.model.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité représentant un utilisateur de la plateforme
 * Peut être: PARTICIPANT, PROMOTEUR, ADMIN ou VALIDATEUR
 */
@Entity
@Table(name = "utilisateur", indexes = {
    @Index(name = "idx_user_email", columnList = "email"),
    @Index(name = "idx_user_role", columnList = "role")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Size(max = 20)
    @Column(length = 20)
    private String telephone;

    @Column(name = "mot_de_passe_hash")
    private String motDePasseHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider", nullable = false)
    private AuthProvider authProvider = AuthProvider.LOCAL;
    
    @Column(name = "provider_id")  // ID depuis Google/Facebook
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    @Column(name = "compte_active", nullable = false)
    private Boolean compteActive = true;

    @Column(name = "derniere_connexion")
    private LocalDateTime derniereConnexion;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "email_verifie")
    private Boolean emailVerifie = false;

    @Column(name = "telephone_verifie")
    private Boolean telephoneVerifie = false;

    // Helper methods
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public boolean isPromoter() {
        return role == UserRole.PROMOTEUR;
    }

    public boolean isParticipant() {
        return role == UserRole.PARTICIPANT;
    }

    public boolean isAdmin() {
        return role == UserRole.ADMIN;
    }

    public void updateLastLogin() {
        this.derniereConnexion = LocalDateTime.now();
    }

    @PrePersist
    @PreUpdate
    private void validateAuthFields() {
        if (authProvider == AuthProvider.LOCAL && 
            (motDePasseHash == null || motDePasseHash.isBlank())) {
            throw new IllegalStateException(
                "Le mot de passe est obligatoire pour l'authentification locale"
            );
        }
        
        if (authProvider != AuthProvider.LOCAL && 
            (providerId == null || providerId.isBlank())) {
            throw new IllegalStateException(
                "Le provider ID est obligatoire pour l'authentification OAuth"
            );
        }
    }
    
    // Helper methods
    public boolean hasLocalAuth() {
        return authProvider == AuthProvider.LOCAL;
    }
    
    public boolean hasOAuthAuth() {
        return authProvider != AuthProvider.LOCAL;
    }
}
