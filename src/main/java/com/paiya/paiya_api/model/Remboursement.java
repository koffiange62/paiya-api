package com.paiya.paiya_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.paiya.paiya_api.model.enums.RemboursementMotif;
import com.paiya.paiya_api.model.enums.RemboursementStatus;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Demande de remboursement
 */
@Entity
@Table(name = "remboursement", indexes = {
    @Index(name = "idx_remboursement_transaction", columnList = "transaction_id"),
    @Index(name = "idx_remboursement_statut", columnList = "statut")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Remboursement extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @NotNull
    @DecimalMin(value = "0.0")
    @Column(name = "montant_rembourse", nullable = false, precision = 10, scale = 2)
    private BigDecimal montantRembourse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private RemboursementMotif motif;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RemboursementStatus statut = RemboursementStatus.DEMANDE;

    @Column(name = "date_demande", nullable = false)
    private LocalDateTime dateDemande;

    @Column(name = "date_traitement")
    private LocalDateTime dateTraitement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "traite_par")
    private User traitePar; // Admin qui a traité

    @Column(name = "commentaire_admin", columnDefinition = "TEXT")
    private String commentaireAdmin;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (dateDemande == null) {
            dateDemande = LocalDateTime.now();
        }
    }

    // Helper methods
    public boolean isPending() {
        return statut == RemboursementStatus.DEMANDE;
    }

    public boolean isApproved() {
        return statut == RemboursementStatus.APPROUVE;
    }

    public void approuver(User admin) {
        this.statut = RemboursementStatus.APPROUVE;
        this.traitePar = admin;
        this.dateTraitement = LocalDateTime.now();
    }

    public void refuser(User admin, String commentaire) {
        this.statut = RemboursementStatus.REFUSE;
        this.traitePar = admin;
        this.commentaireAdmin = commentaire;
        this.dateTraitement = LocalDateTime.now();
    }

    public void traiter() {
        this.statut = RemboursementStatus.TRAITE;
        if (this.dateTraitement == null) {
            this.dateTraitement = LocalDateTime.now();
        }
    }
}
