package com.paiya.paiya_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Avantage associé à un type de billet
 * (Accès VIP, Boisson gratuite, Meet & Greet, etc.)
 */
@Entity
@Table(name = "avantage", indexes = {
    @Index(name = "idx_avantage_type_billet", columnList = "type_billet_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avantage extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_billet_id", nullable = false)
    private TypeBillet typeBillet;

    @NotBlank(message = "La description de l'avantage est obligatoire")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "ordre_affichage")
    private Integer ordreAffichage = 0;

    @Column(name = "icone")
    private String icone; // Nom de l'icône (ex: "gift", "star", "drink")
}
