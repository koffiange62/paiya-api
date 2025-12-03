package com.paiya.paiya_api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Avis et note sur un événement
 */
@Entity
@Table(name = "avis",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_avis_user_event",
        columnNames = {"utilisateur_id", "evenement_id"}
    ),
    indexes = {
        @Index(name = "idx_avis_event", columnList = "evenement_id"),
        @Index(name = "idx_avis_user", columnList = "utilisateur_id"),
        @Index(name = "idx_avis_note", columnList = "note")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avis extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private User utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evenement_id", nullable = false)
    private Event evenement;

    @NotNull
    @Min(value = 1, message = "La note minimum est 1")
    @Max(value = 5, message = "La note maximum est 5")
    @Column(nullable = false)
    private Integer note; // 1 à 5 étoiles

    @Size(max = 2000)
    @Column(columnDefinition = "TEXT")
    private String commentaire;

    @Column(name = "date_publication", nullable = false)
    private LocalDateTime datePublication;

    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @Column(nullable = false)
    private Boolean modere = false; // Modéré par un admin ?

    @Column(nullable = false)
    private Boolean visible = true;

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (datePublication == null) {
            datePublication = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        super.onUpdate();
        dateModification = LocalDateTime.now();
    }

    // Helper methods
    public boolean canBeEditedBy(User user) {
        return utilisateur.equals(user) && LocalDateTime.now().minusHours(24).isBefore(datePublication);
    }
}
