package com.Vhytor.SmartRent.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "homes")
@AllArgsConstructor
@NoArgsConstructor
public class Home {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "home_id")
    private Long homeId;

    private String address;
    private String description;
    private BigDecimal pricePerMonth;
    private BigDecimal viewingFee; // The "little token" debited to view

    private String smartLockCode; // The current code to the door

    // Manual Getters and Setters
    public Long getHomeId() { return homeId; }
    public void setHomeId(Long homeId) { this.homeId = homeId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public BigDecimal getViewingFee() { return viewingFee; }
    public void setViewingFee(BigDecimal viewingFee) { this.viewingFee = viewingFee; }

    public String getSmartLockCode() { return smartLockCode; }
    public void setSmartLockCode(String smartLockCode) { this.smartLockCode = smartLockCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPricePerMonth() { return pricePerMonth; }
    public void setPricePerMonth(BigDecimal pricePerMonth) { this.pricePerMonth = pricePerMonth; }
}
