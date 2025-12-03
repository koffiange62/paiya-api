package com.paiya.paiya_api.model;

import com.paiya.paiya_api.model.enums.TypeIdentifiant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Plateforme de paiement mobile
 * (Orange Money, Wave, MTN, PayPal, etc.)
 */
@Entity
@Table(name = "plateforme_paiement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlateformePaiement extends BaseEntity{
    
    @NotBlank
    @Size(max = 50)
    @Column(unique = true, nullable = false, length = 50)
    private String nom; // Orange Money, Wave, MTN, PayPal

    @Enumerated(EnumType.STRING)
    @Column(name = "type_identifiant", nullable = false, length = 20)
    private TypeIdentifiant typeIdentifiant; // TELEPHONE ou EMAIL

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "code_pays", length = 2)
    private String codePays; // CI, SN, BF pour filtrer par pays
}
