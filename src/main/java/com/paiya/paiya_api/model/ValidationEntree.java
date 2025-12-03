package com.paiya.paiya_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Enregistrement de validation d'un billet à l'entrée
 */
@Entity
@Table(name = "validation_entree", indexes = {
    @Index(name = "idx_validation_billet", columnList = "billet_id"),
    @Index(name = "idx_validation_date", columnList = "date_validation")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationEntree extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billet_id", nullable = false)
    private Billet billet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validateur_id", nullable = false)
    private User validateur; // Personne qui a scanné le QR code

    @Column(name = "date_validation", nullable = false)
    private LocalDateTime dateValidation;

    @Column(name = "lieu_validation")
    private String lieuValidation;

    @Column(name = "appareil_info", columnDefinition = "jsonb")
    private String appareilInfo; // JSON avec infos de l'appareil

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (dateValidation == null) {
            dateValidation = LocalDateTime.now();
        }
    }
}
