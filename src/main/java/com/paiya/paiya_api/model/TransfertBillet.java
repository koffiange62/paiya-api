package com.paiya.paiya_api.model;

import java.time.LocalDateTime;

import com.paiya.paiya_api.model.enums.TransfertStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Transfert d'un billet d'un utilisateur à un autre
 */
@Entity
@Table(name = "transfert_billet", indexes = {
    @Index(name = "idx_transfert_billet", columnList = "billet_id"),
    @Index(name = "idx_transfert_expediteur", columnList = "expediteur_id"),
    @Index(name = "idx_transfert_token", columnList = "token_validation")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransfertBillet extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billet_id", nullable = false)
    private Billet billet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expediteur_id", nullable = false)
    private User expediteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinataire_id")
    private User destinataire; // Null jusqu'à acceptation

    @NotBlank
    @Email
    @Column(name = "email_destinataire", nullable = false)
    private String emailDestinataire;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransfertStatus statut = TransfertStatus.EN_ATTENTE;

    @Column(name = "date_transfert", nullable = false)
    private LocalDateTime dateTransfert;

    @Column(name = "date_reponse")
    private LocalDateTime dateReponse;

    @Column(columnDefinition = "TEXT")
    private String message; // Message de l'expéditeur

    @Column(name = "token_validation", unique = true, nullable = false, length = 100)
    private String tokenValidation;

    @Column(name = "date_expiration", nullable = false)
    private LocalDateTime dateExpiration;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (dateTransfert == null) {
            dateTransfert = LocalDateTime.now();
        }
        if (tokenValidation == null) {
            tokenValidation = UUID.randomUUID().toString();
        }
        if (dateExpiration == null) {
            // 7 jours par défaut
            dateExpiration = LocalDateTime.now().plusDays(7);
        }
    }

    // Helper methods
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(dateExpiration);
    }

    public boolean isPending() {
        return statut == TransfertStatus.EN_ATTENTE && !isExpired();
    }

    public void accepter(User destinataire) {
        this.statut = TransfertStatus.ACCEPTE;
        this.destinataire = destinataire;
        this.dateReponse = LocalDateTime.now();
    }

    public void refuser() {
        this.statut = TransfertStatus.REFUSE;
        this.dateReponse = LocalDateTime.now();
    }

    public void expirer() {
        this.statut = TransfertStatus.EXPIRE;
    }
}
