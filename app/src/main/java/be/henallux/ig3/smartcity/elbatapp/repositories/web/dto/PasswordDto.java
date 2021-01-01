package be.henallux.ig3.smartcity.elbatapp.repositories.web.dto;

public class PasswordDto {
    private String currentPassword, newPassword, username;

    public PasswordDto(String currentPassword, String newPassword, String username) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.username = username;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
