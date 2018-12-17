package pt.lisomatrix.Sockets.websocket.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ThemeDAO {

    @JsonProperty
    private boolean dark;

    private String primaryColor;

    private String secondaryColor;

    private String textPrimaryColor;

    private String textSecondaryColor;

    private String headerColor;

    private String backgroundColor;

    private String cardBackground;

    private String iconColor;

    private String buttonColor;

    public String toJSON() {

        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(this);

        } catch (JsonProcessingException e) {
            return e.getMessage();
        }

    }

    public void populate(String json) {

        ObjectMapper mapper = new ObjectMapper();

        try {
            ThemeDAO obj = mapper.readValue(json, ThemeDAO.class);

            primaryColor = obj.primaryColor;
            secondaryColor = obj.secondaryColor;
            textPrimaryColor = obj.textPrimaryColor;
            textSecondaryColor = obj.textSecondaryColor;
            headerColor = obj.headerColor;
            backgroundColor = obj.backgroundColor;
            cardBackground = obj.cardBackground;
            iconColor = obj.iconColor;
            buttonColor = obj.buttonColor;
            dark = obj.dark;

        } catch (JsonParseException e) {
            e.printStackTrace();

        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }

    public String getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(String buttonColor) {
        this.buttonColor = buttonColor;
    }

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public boolean isDark() {
        return dark;
    }

    public void setDark(boolean dark) {
        this.dark = dark;
    }

    public String getCardBackground() {
        return cardBackground;
    }

    public void setCardBackground(String cardBackground) {
        this.cardBackground = cardBackground;
    }

    public Boolean getDark() {
        return dark;
    }

    public void setDark(Boolean dark) {
        this.dark = dark;
    }

    public String getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public String getTextPrimaryColor() {
        return textPrimaryColor;
    }

    public void setTextPrimaryColor(String textPrimaryColor) {
        this.textPrimaryColor = textPrimaryColor;
    }

    public String getTextSecondaryColor() {
        return textSecondaryColor;
    }

    public void setTextSecondaryColor(String textSecondaryColor) {
        this.textSecondaryColor = textSecondaryColor;
    }

    public String getHeaderColor() {
        return headerColor;
    }

    public void setHeaderColor(String headerColor) {
        this.headerColor = headerColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
