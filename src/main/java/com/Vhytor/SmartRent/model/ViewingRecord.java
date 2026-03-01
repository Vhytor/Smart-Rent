package com.Vhytor.SmartRent.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "viewing_records")
@Data
public class ViewingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewingRecordId;

    @ManyToOne
    private User user;

    @ManyToOne
    private Home home;

    private String accessCode; // The unique code generated for this user
    private boolean isPaid;
    private LocalDateTime expiryTime;

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Home getHome() { return home; }
    public void setHome(Home home) { this.home = home; }

    public String getAccessCode() { return accessCode; }
    public void setAccessCode(String accessCode) { this.accessCode = accessCode; }

    public boolean getIsPaid() { return isPaid; }
    public void setIsPaid(boolean isPaid) { this.isPaid = isPaid; }

    public LocalDateTime getExpiryTime() { return expiryTime; }
    public void setExpiryTime(LocalDateTime expiryTime) { this.expiryTime = expiryTime; }
}