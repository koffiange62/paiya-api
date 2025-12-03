package com.paiya.paiya_api.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Type de billet pour un événement
 * (Standard, VIP, VVIP, etc.)
 */
@Entity
@Table(name = "type_billet", indexes = {
    @Index(name = "idx_type_billet_event", columnList = "evenement_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TypeBillet extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evenement_id", nullable = false)
    private Event evenement;

    @NotBlank(message = "Le nom du type de billet est obligatoire")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nom; // Standard, VIP, VVIP

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.0", message = "Le prix doit être positif")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prix;

    @Min(value = 0, message = "La quantité doit être positive")
    @Column(name = "quantite_disponible", nullable = false)
    private Integer quantiteDisponible;

    @Min(value = 0)
    @Column(name = "quantite_vendue", nullable = false)
    private Integer quantiteVendue = 0;

    @Min(value = 1, message = "La limite d'achat doit être au moins 1")
    @Column(name = "limite_achat_par_personne", nullable = false)
    private Integer limiteAchatParPersonne = 10;

    @Column(nullable = false)
    private Boolean actif = true;

    @Column(name = "ordre_affichage")
    private Integer ordreAffichage = 0;

    // Helper methods
    public int getBilletsRestants() {
        return quantiteDisponible - quantiteVendue;
    }

    public boolean isDisponible() {
        return actif && getBilletsRestants() > 0;
    }

    public boolean canBuy(int quantite) {
        return isDisponible() && getBilletsRestants() >= quantite;
    }

    public void incrementerQuantiteVendue(int quantite) {
        this.quantiteVendue += quantite;
    }

    public void decrementerQuantiteVendue(int quantite) {
        this.quantiteVendue = Math.max(0, this.quantiteVendue - quantite);
    }

    public BigDecimal calculateTotal(int quantite) {
        return prix.multiply(BigDecimal.valueOf(quantite));
    }

}
