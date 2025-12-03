package com.paiya.paiya_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

public class PreferenceNotification extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", unique = true, nullable = false)
    private User utilisateur;

    @Column(name = "email_actif", nullable = false)
    private Boolean emailActif = true;

    @Column(name = "sms_actif", nullable = false)
    private Boolean smsActif = false;

    @Column(name = "push_actif", nullable = false)
    private Boolean pushActif = true;

    @Column(name = "notif_nouveaux_events", nullable = false)
    private Boolean notifNouveauxEvents = true;

    @Column(name = "notif_rappels", nullable = false)
    private Boolean notifRappels = true;

    @Column(name = "notif_promotions", nullable = false)
    private Boolean notifPromotions = true;

    @Column(name = "notif_confirmations", nullable = false)
    private Boolean notifConfirmations = true;

    @Column(name = "notif_annulations", nullable = false)
    private Boolean notifAnnulations = true;

    @Column(name = "frequence_rappel_heures", nullable = false)
    private Integer frequenceRappelHeures = 24; // 24h avant événement
}
