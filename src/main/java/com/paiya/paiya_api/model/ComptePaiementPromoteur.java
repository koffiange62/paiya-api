package com.paiya.paiya_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Compte de paiement d'un promoteur
 * Un promoteur peut avoir plusieurs comptes (Orange Money, Wave, etc.)
 */
@Entity
@Table(name = "compte_paiement_promoteur", indexes = {
    @Index(name = "idx_compte_utilisateur", columnList = "utilisateur_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComptePaiementPromoteur extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private User utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plateforme_id", nullable = false)
    private PlateformePaiement plateforme;

    @NotBlank
    @Column(nullable = false)
    private String identifiant; // Numéro de téléphone ou email

    @Builder.Default
    @Column(nullable = false)
    private Boolean principal = false; // Compte principal ?

    @Builder.Default
    @Column(nullable = false)
    private Boolean actif = true;

    @Builder.Default
    @Column(name = "verifie")
    private Boolean verifie = false;
}
