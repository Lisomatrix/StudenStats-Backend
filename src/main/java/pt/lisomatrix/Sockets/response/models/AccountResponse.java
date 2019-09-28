package pt.lisomatrix.Sockets.response.models;

import pt.lisomatrix.Sockets.models.*;

import java.util.Date;

public class AccountResponse {

    private String name;

    private String email;

    private String registrationCode;

    private boolean isCreated;

    private Date birthDate;

    private String role;

    private long roleId;

    private String address;

    private String phoneNumber;

    private long userId;

    public AccountResponse(User user, Student student) {

        setName(student.getName());
        setBirthDate(student.getBirthDate());
        setCreated(user.getCreated());
        setRegistrationCode(user.getRegistrationCode());
        setEmail(user.getUsername());
        setRole(user.getRole().getRole());
        setAddress(student.getAddress());
        setPhoneNumber(student.getMobilePhone());
        setUserId(user.getUserId());
        setRoleId(user.getRole().getId());
    }

    public AccountResponse(User user, Teacher teacher) {

        setName(teacher.getName());
        setBirthDate(teacher.getBirthDate());
        setCreated(user.getCreated());
        setRegistrationCode(user.getRegistrationCode());
        setEmail(user.getUsername());
        setRole(user.getRole().getRole());
        setAddress(teacher.getAddress());
        setPhoneNumber(teacher.getMobilePhone());
        setUserId(user.getUserId());
        setRoleId(user.getRole().getId());
    }

    public AccountResponse(User user, Parent parent) {

        setName(parent.getName());
        setBirthDate(parent.getBirthDate());
        setCreated(user.getCreated());
        setRegistrationCode(user.getRegistrationCode());
        setEmail(user.getUsername());
        setRole(user.getRole().getRole());
        setAddress(parent.getAddress());
        setPhoneNumber(parent.getMobilePhone());
        setUserId(user.getUserId());
        setRoleId(user.getRole().getId());
    }

    public AccountResponse(User user, Admin admin) {

        setName(admin.getName());
        setBirthDate(admin.getBirthDate());
        setCreated(user.getCreated());
        setRegistrationCode(user.getRegistrationCode());
        setEmail(user.getUsername());
        setRole(user.getRole().getRole());
        setAddress(admin.getAddress());
        setPhoneNumber(admin.getMobilePhone());
        setUserId(user.getUserId());
        setRoleId(user.getRole().getId());
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
