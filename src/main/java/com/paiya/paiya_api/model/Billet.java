package com.paiya.paiya_api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Billet généré après confirmation d'une transaction
 * Chaque billet a un QR code unique
 */
@Entity
@Table(name = "billet", indexes = {
    @Index(name = "idx_billet_transaction", columnList = "transaction_id"),
    @Index(name = "idx_billet_code", columnList = "code_unique"),
    @Index(name = "idx_billet_utilisateur", columnList = "utilisateur_actuel_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Billet extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @NotBlank
    @Column(name = "code_unique", unique = true, nullable = false, length = 100)
    private String codeUnique;

    @Column(name = "qr_code_url")
    private String qrCodeUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_actuel_id", nullable = false)
    private User utilisateurActuel; // Propriétaire actuel (peut changer via transfert)

    @Column(nullable = false)
    private Boolean utilise = false;

    @Column(name = "date_utilisation")
    private LocalDateTime dateUtilisation;

    @Column(name = "date_generation", nullable = false)
    private LocalDateTime dateGeneration;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (codeUnique == null) {
            codeUnique = generateUniqueCode();
        }
        if (dateGeneration == null) {
            dateGeneration = LocalDateTime.now();
        }
    }

    private String generateUniqueCode() {
        return "PAIYA-" + UUID.randomUUID().toString().toUpperCase();
    }

    // Helper methods
    public boolean isValid() {
        return !utilise && transaction.isConfirmed();
    }

    public boolean canBeUsed() {
        if (utilise) return false;
        if (!transaction.isConfirmed()) return false;
        
        // Vérifier que l'événement est aujourd'hui
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime eventDateTime = transaction.getEvenement().getDateHeureDebut();
        
        return now.toLocalDate().equals(eventDateTime.toLocalDate());
    }

    public void use() {
        this.utilise = true;
        this.dateUtilisation = LocalDateTime.now();
    }

    public boolean canBeTransferred() {
        return !utilise && transaction.isConfirmed();
    }
}
