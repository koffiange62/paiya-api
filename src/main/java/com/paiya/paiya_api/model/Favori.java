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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Favori - Événement sauvegardé par un utilisateur
 */
@Entity
@Table(name = "favori", 
    uniqueConstraints = @UniqueConstraint(
        name = "uk_favori_user_event", 
        columnNames = {"utilisateur_id", "evenement_id"}
    ),
    indexes = {
        @Index(name = "idx_favori_user", columnList = "utilisateur_id"),
        @Index(name = "idx_favori_event", columnList = "evenement_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favori extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private User utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evenement_id", nullable = false)
    private Event evenement;

    @Column(name = "date_ajout", nullable = false)
    private LocalDateTime dateAjout;

    @Column(name = "notifier_changements", nullable = false)
    private Boolean notifierChangements = true;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (dateAjout == null) {
            dateAjout = LocalDateTime.now();
        }
    }
}
