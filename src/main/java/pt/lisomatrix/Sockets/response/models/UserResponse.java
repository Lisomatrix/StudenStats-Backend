package pt.lisomatrix.Sockets.response.models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserResponse {

    private String name;

    private long userId;

    private Long roleEntityId;

    private Long Id;

    private String userThemeSettings;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isClassDirector;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String address;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phoneNumber;

    private String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserThemeSettings() {
        return userThemeSettings;
    }

    public void setUserThemeSettings(String userThemeSettings) {
        this.userThemeSettings = userThemeSettings;
    }

    public Boolean getClassDirector() {
        return isClassDirector;
    }

    public void setClassDirector(Boolean classDirector) {
        isClassDirector = classDirector;
    }

    public Long getRoleEntityId() {
        return roleEntityId;
    }

    public void setRoleEntityId(Long roleEntityId) {
        this.roleEntityId = roleEntityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
}
