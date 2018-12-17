package pt.lisomatrix.Sockets.websocket.models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class UserDAO {

    private String name;

    private long userId;

    private String roleEntityId;

    private String Id;

    private String userThemeSettings;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isClassDirector;

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

    public String getRoleEntityId() {
        return roleEntityId;
    }

    public void setRoleEntityId(String roleEntityId) {
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

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
