package pt.ipleiria.estg.dei.ei.dae.pmei.dtos;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class PasswordChangeDTO implements Serializable {
    private String username;

    @NotBlank
    private String password;

    private String oldPassword;

    @NotBlank
    private String confirmPassword;

    public PasswordChangeDTO() {
    }

    public PasswordChangeDTO(String username, String password, String oldPassword, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
