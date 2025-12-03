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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Partage d'un événement sur les réseaux sociaux
 */
@Entity
@Table(name = "partage_social", indexes = {
    @Index(name = "idx_partage_user", columnList = "utilisateur_id"),
    @Index(name = "idx_partage_event", columnList = "evenement_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartageSocial extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private User utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evenement_id", nullable = false)
    private Event evenement;

    @Column(nullable = false, length = 20)
    private String plateforme; // facebook, twitter, whatsapp, instagram

    @Column(name = "date_partage", nullable = false)
    private LocalDateTime datePartage;

    @Column(name = "url_partage", length = 500)
    private String urlPartage;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (datePartage == null) {
            datePartage = LocalDateTime.now();
        }
    }
}
