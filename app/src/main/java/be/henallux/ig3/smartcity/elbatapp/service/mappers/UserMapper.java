package be.henallux.ig3.smartcity.elbatapp.service.mappers;

import be.henallux.ig3.smartcity.elbatapp.data.model.User;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.AddressDto;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.UserDto;

public class UserMapper {
    private static UserMapper instance = null;

    public static UserMapper getInstance() {
        if(instance == null)
            instance = new UserMapper();

        return instance;
    }

    public UserDto mapToUserDto(User user){
        return new UserDto(
                user.getUsername(),
                user.getPassword(),
                user.getLastName(),
                user.getFirstName(),
                user.getBirthDate(),
                user.getGender(),
                user.getPhoneNumber(),
                user.getEmail(),
                user.getPositiveToCovid19(),
                new AddressDto(
                        user.getAddress().getStreet(),
                        user.getAddress().getNumber(),
                        user.getAddress().getCountry(),
                        user.getAddress().getCity(),
                        user.getAddress().getPostalCode())
        );
    }
}
