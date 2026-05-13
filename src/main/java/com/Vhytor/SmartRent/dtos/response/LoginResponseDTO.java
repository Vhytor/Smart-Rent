package com.Vhytor.SmartRent.dtos.response;

import com.Vhytor.SmartRent.enums.Role;

/**
 * Response returned after a successful login.
 * Returns the JWT token alongside user details so the frontend
 * can route to the correct dashboard without a second API call.
 */
public class LoginResponseDTO {

    private String token;
    private Long userId;
    private String fullName;
    private String userEmail;
    private Role role;

    public LoginResponseDTO(String token, Long userId, String fullName, String userEmail, Role role) {
        this.token = token;
        this.userId = userId;
        this.fullName = fullName;
        this.userEmail = userEmail;
        this.role = role;
    }

    public String getToken() { return token; }
    public Long getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getUserEmail() { return userEmail; }
    public Role getRole() { return role; }
}
