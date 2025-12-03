package com.paiya.paiya_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.paiya.paiya_api.model.enums.CodePromoType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
 * Code promo pour réduction sur un événement
 */
@Entity
@Table(name = "code_promo", indexes = {
    @Index(name = "idx_promo_event", columnList = "evenement_id"),
    @Index(name = "idx_promo_code", columnList = "code")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodePromo {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evenement_id", nullable = false)
    private Event evenement;

    @NotBlank
    @Size(max = 50)
    @Column(unique = true, nullable = false, length = 50)
    private String code; // PROMO2024, EARLYBIRD, etc.

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CodePromoType type; // POURCENTAGE ou MONTANT_FIXE

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valeur; // 10 pour 10% ou 5000 pour 5000 FCFA

    @Min(value = 1)
    @Column(name = "utilisation_max")
    private Integer utilisationMax;

    @Min(value = 0)
    @Column(name = "utilisation_actuelle", nullable = false)
    private Integer utilisationActuelle = 0;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private LocalDateTime dateDebut;

    @NotNull
    @Column(name = "date_fin", nullable = false)
    private LocalDateTime dateFin;

    @Column(nullable = false)
    private Boolean actif = true;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Min(value = 1)
    @Column(name = "limite_par_utilisateur")
    private Integer limiteParUtilisateur = 1;

    // Helper methods
    public boolean isValid() {
        if (!actif) return false;
        if (utilisationMax != null && utilisationActuelle >= utilisationMax) {
            return false;
        }
        
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(dateDebut) && now.isBefore(dateFin);
    }

    public BigDecimal calculateReduction(BigDecimal montant) {
        if (type == CodePromoType.POURCENTAGE) {
            return montant.multiply(valeur.divide(BigDecimal.valueOf(100)));
        } else {
            return valeur.min(montant); // Ne pas réduire plus que le montant total
        }
    }

    public void incrementerUtilisation() {
        this.utilisationActuelle++;
    }

    public int getUtilisationsRestantes() {
        if (utilisationMax == null) {
            // TODO: ajouter une valeur par défaut dans ce cas.
            return Integer.MAX_VALUE;
        }
        return Math.max(0, utilisationMax - utilisationActuelle);
    }
}
