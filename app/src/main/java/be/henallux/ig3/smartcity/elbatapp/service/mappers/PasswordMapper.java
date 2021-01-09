package be.henallux.ig3.smartcity.elbatapp.service.mappers;

import be.henallux.ig3.smartcity.elbatapp.data.model.Password;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.PasswordDto;

public class PasswordMapper {
    private static PasswordMapper instance;

    static {
        instance = null;
    }

    public static PasswordMapper getInstance() {
        if (instance == null)
            instance = new PasswordMapper();

        return instance;
    }

    public PasswordDto mapToPasswordDto(Password password) {
        return new PasswordDto(
                password.getCurrentPassword(),
                password.getNewPassword(),
                password.getUsername()
        );
    }
}
