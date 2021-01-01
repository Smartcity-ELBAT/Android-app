package be.henallux.ig3.smartcity.elbatapp.service.mappers;

import be.henallux.ig3.smartcity.elbatapp.data.model.Address;
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
                new AddressDto(
                        user.getAddress().getStreet(),
                        user.getAddress().getNumber(),
                        user.getAddress().getCountry(),
                        user.getAddress().getCity(),
                        user.getAddress().getPostalCode())
        );
    }

    public User mapToUser(UserDto userDto){
        return new User(
                userDto.getId(),
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getLastName(),
                userDto.getFirstName(),
                userDto.getBirthDate(),
                userDto.getEmail(),
                userDto.getPhoneNumber(),
                userDto.getGender(),
                new Address(
                        userDto.getAddress().getId(),
                        userDto.getAddress().getStreet(),
                        userDto.getAddress().getNumber(),
                        userDto.getAddress().getPostalCode(),
                        userDto.getAddress().getCity(),
                        userDto.getAddress().getCountry()
                )
        );
    }
}
