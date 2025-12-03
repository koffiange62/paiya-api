package com.paiya.paiya_api.model;

import java.time.LocalDateTime;

import com.paiya.paiya_api.model.enums.NotificationCanal;
import com.paiya.paiya_api.model.enums.NotificationType;

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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Notification envoyée à un utilisateur
 */
@Entity
@Table(name = "notification", indexes = {
    @Index(name = "idx_notification_user", columnList = "utilisateur_id"),
    @Index(name = "idx_notification_lue", columnList = "lue"),
    @Index(name = "idx_notification_date", columnList = "date_envoi")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private User utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evenement_id")
    private Event evenement; // Peut être null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false)
    private String titre;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private Boolean lue = false;

    @Column(name = "date_envoi", nullable = false)
    private LocalDateTime dateEnvoi;

    @Column(name = "date_lecture")
    private LocalDateTime dateLecture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NotificationCanal canal; // EMAIL, SMS, PUSH, IN_APP

    @Column(nullable = false)
    private Boolean envoyee = false;

    @Column(columnDefinition = "jsonb")
    private String metadata; // JSON avec données additionnelles

    @PrePersist
    protected void onCreate() {
        super.onCreate();
        if (dateEnvoi == null) {
            dateEnvoi = LocalDateTime.now();
        }
    }

    // Helper methods
    public void marquerCommeLue() {
        this.lue = true;
        this.dateLecture = LocalDateTime.now();
    }

    public void marquerCommeEnvoyee() {
        this.envoyee = true;
    }

    public boolean isPending() {
        return !envoyee;
    }
}
