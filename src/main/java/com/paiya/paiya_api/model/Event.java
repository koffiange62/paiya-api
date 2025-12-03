package com.paiya.paiya_api.model;

import com.paiya.paiya_api.model.enums.EventStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entité représentant un événement
 */
@Entity
@Table(name = "evenement", indexes = {
    @Index(name = "idx_event_promoteur", columnList = "promoteur_id"),
    @Index(name = "idx_event_categorie", columnList = "categorie_id"),
    @Index(name = "idx_event_statut", columnList = "statut"),
    @Index(name = "idx_event_date", columnList = "date_debut")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promoteur_id", nullable = false)
    private User promoteur;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categorie_id", nullable = false)
    private Category categorie;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "devise_id", nullable = false)
    private Devise devise;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 255)
    @Column(nullable = false)
    private String titre;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Lieu
    @NotBlank(message = "Le nom du lieu est obligatoire")
    @Column(name = "lieu_nom", nullable = false)
    private String lieuNom;

    @NotBlank(message = "L'adresse est obligatoire")
    @Column(name = "lieu_adresse", nullable = false, columnDefinition = "TEXT")
    private String lieuAdresse;

    @Column(name = "lieu_latitude", precision = 10, scale = 8)
    private BigDecimal lieuLatitude;

    @Column(name = "lieu_longitude", precision = 11, scale = 8)
    private BigDecimal lieuLongitude;

    // Date et heure
    @NotNull(message = "La date de début est obligatoire")
    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @NotNull(message = "L'heure de début est obligatoire")
    @Column(name = "heure_debut", nullable = false)
    private LocalTime heureDebut;

    @Column(name = "heure_fin")
    private LocalTime heureFin;

    // Image
    @Column(name = "image_url")
    private String imageUrl;

    // Capacité
    @Min(value = 1, message = "La capacité doit être au moins 1")
    @Column(name = "capacite_totale", nullable = false)
    private Integer capaciteTotale;

    @Column(name = "date_limite_vente")
    private LocalDateTime dateLimiteVente;

    // Statut
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventStatus statut = EventStatus.BROUILLON;

    // Commission et remboursement
    @DecimalMin(value = "0.0", message = "La commission doit être positive")
    @DecimalMax(value = "100.0", message = "La commission ne peut dépasser 100%")
    @Column(name = "commission_plateforme_pct", precision = 5, scale = 2)
    private BigDecimal commissionPlateformePct = new BigDecimal("5.00");

    @Column(name = "autoriser_remboursement", nullable = false)
    private Boolean autoriserRemboursement = true;

    @Column(name = "delai_remboursement_heures")
    private Integer delaiRemboursementHeures = 48;

    // Métadonnées
    @Column(name = "nombre_vues")
    private Integer nombreVues = 0;

    @Column(name = "nombre_favoris")
    private Integer nombreFavoris = 0;

    @Column(name = "note_moyenne", precision = 3, scale = 2)
    private BigDecimal noteMoyenne = BigDecimal.ZERO;

    @Column(name = "nombre_avis")
    private Integer nombreAvis = 0;

    // Helper methods
    public LocalDateTime getDateHeureDebut() {
        return LocalDateTime.of(dateDebut, heureDebut);
    }

    public LocalDateTime getDateHeureFin() {
        if (heureFin != null) {
            return LocalDateTime.of(dateDebut, heureFin);
        }
        return null;
    }

    public boolean isPublished() {
        return statut == EventStatus.PUBLIE;
    }

    public boolean isCancelled() {
        return statut == EventStatus.ANNULE;
    }

    public boolean isFinished() {
        return statut == EventStatus.TERMINE || LocalDate.now().isAfter(dateDebut);
    }

    public boolean canRefund() {
        if (!autoriserRemboursement) return false;
        
        LocalDateTime deadline = getDateHeureDebut()
            .minusHours(delaiRemboursementHeures);
        return LocalDateTime.now().isBefore(deadline);
    }

    public void incrementVues() {
        this.nombreVues = (this.nombreVues == null ? 0 : this.nombreVues) + 1;
    }

    public void incrementFavoris() {
        this.nombreFavoris = (this.nombreFavoris == null ? 0 : this.nombreFavoris) + 1;
    }

    public void decrementFavoris() {
        this.nombreFavoris = Math.max(0, 
            (this.nombreFavoris == null ? 0 : this.nombreFavoris) - 1);
    }

    public void updateNoteEtAvis(BigDecimal nouvelleNote, int totalAvis) {
        this.noteMoyenne = nouvelleNote;
        this.nombreAvis = totalAvis;
    }
}