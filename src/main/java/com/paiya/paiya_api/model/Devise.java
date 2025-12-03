package com.paiya.paiya_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "devise")
public class Devise extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, length = 3)
    private String code;        // "XOF", "EUR", "USD"
    
    @Column(length = 50)
    private String nom;         // "Franc CFA", "Euro", "Dollar"
    
    @Column(length = 10)
    private String symbole;     // "FCFA", "€", "$"
    
    @Column
    private Boolean active;     // true/false
}
