package com.paiya.paiya_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité Catégorie d'événement
 * (Concert, Sport, Conférence, etc.)
 */
@Entity
@Table(name = "categorie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends BaseEntity {

    @NotBlank
    @Size(max = 100)
    @Column(unique = true, nullable = false, length = 100)
    private String nom; // Concert, Sport, Conférence

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "icone_url")
    private String iconeUrl; // URL de l'icône

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Builder.Default
    @Column(name = "ordre_affichage")
    private Integer ordreAffichage = 0;

}
