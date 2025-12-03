package com.paiya.paiya_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.paiya.paiya_api.model.enums.TransactionStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Transaction d'achat de billets
 */
@Entity
@Table(name = "transaction", indexes = {
    @Index(name = "idx_transaction_participant", columnList = "participant_id"),
    @Index(name = "idx_transaction_event", columnList = "evenement_id"),
    @Index(name = "idx_transaction_statut", columnList = "statut"),
    @Index(name = "idx_transaction_reference", columnList = "reference_unique")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction extends BaseEntity{

    @Column(name = "reference_unique", unique = true, nullable = false, length = 100)
    private String referenceUnique;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private User participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evenement_id", nullable = false)
    private Event evenement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_billet_id", nullable = false)
    private TypeBillet typeBillet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "methode_paiement_id", nullable = false)
    private MethodePaiementEvenement methodePaiement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_promo_id")
    private CodePromo codePromo;

    @NotNull
    @Min(value = 1)
    @Column(nullable = false)
    private Integer quantite;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "montant_unitaire", nullable = false, precision = 10, scale = 2)
    private BigDecimal montantUnitaire;

    @DecimalMin(value = "0.0")
    @Column(name = "reduction_promo", precision = 10, scale = 2)
    private BigDecimal reductionPromo = BigDecimal.ZERO;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "montant_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal montantTotal;

    @DecimalMin(value = "0.0")
    @Column(name = "commission_plateforme", nullable = false, precision = 10, scale = 2)
    private BigDecimal commissionPlateforme;

    @DecimalMin(value = "0.0")
    @Column(name = "montant_promoteur", nullable = false, precision = 10, scale = 2)
    private BigDecimal montantPromoteur;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionStatus status = TransactionStatus.EN_ATTENTE;

    @Column(name = "date_transaction", nullable = false)
    private LocalDateTime dateTransaction;

    @Column(name = "details_paiement", columnDefinition = "jsonb")
    private String detailsPaiement; // JSON avec infos spécifiques

    @Column(name = "reference_paiement_externe")
    private String referencePaiementExterne;

    @Column(name = "payment_url")
    private String paymentUrl; // URL de paiement générée

    @Column(name = "date_confirmation")
    private LocalDateTime dateConfirmation;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (referenceUnique == null) {
            referenceUnique = generateReference();
        }
        if (dateTransaction == null) {
            dateTransaction = LocalDateTime.now();
        }
    }

    private String generateReference() {
        return "TRX-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Helper methods
    public boolean isPending() {
        return status == TransactionStatus.EN_ATTENTE;
    }

    public boolean isConfirmed() {
        return status == TransactionStatus.CONFIRME;
    }

    public boolean isFailed() {
        return status == TransactionStatus.ECHOUE;
    }

    public boolean isRefunded() {
        return status == TransactionStatus.REMBOURSE;
    }

    public void confirm() {
        this.status = TransactionStatus.CONFIRME;
        this.dateConfirmation = LocalDateTime.now();
    }

    public void fail() {
        this.status = TransactionStatus.ECHOUE;
    }

    public void refund() {
        this.status = TransactionStatus.REMBOURSE;
    }

    /**
     * Calcule les montants (commission et part promoteur)
     */
    public void calculateAmounts(BigDecimal commissionRate) {
        // Commission de la plateforme
        this.commissionPlateforme = montantTotal
            .multiply(commissionRate.divide(BigDecimal.valueOf(100)));
        
        // Part du promoteur
        this.montantPromoteur = montantTotal.subtract(commissionPlateforme);
    }
}
