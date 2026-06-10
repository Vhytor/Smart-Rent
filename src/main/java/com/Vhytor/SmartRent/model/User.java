package com.Vhytor.SmartRent.model;

import com.Vhytor.SmartRent.enums.Role;
import jakarta.persistence.*;

import lombok.*;


@Entity
@Table(name = "users")
@Data // Lombok magic for getters/setters
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String userEmail;

    @Column(nullable = false)
    @Getter
    private String password;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private Role role; // TENANT or LANDLORD

    @Column(nullable = false)
    private boolean verified = false;

    // The 6-digit code sent to the user's email at registration
    private String verificationCode;

    public User() {}

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }


}


