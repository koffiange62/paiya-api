package com.paiya.paiya_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Méthode de paiement acceptée pour un événement
 * Lie un événement aux comptes de paiement du promoteur
 */
@Entity
@Table(name = "methode_paiement_evenement", indexes = {
    @Index(name = "idx_methode_event", columnList = "evenement_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MethodePaiementEvenement extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evenement_id", nullable = false)
    private Event evenement;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "compte_paiement_id", nullable = false)
    private ComptePaiementPromoteur comptePaiement;

    @Column(nullable = false)
    private Boolean active = true;
}
