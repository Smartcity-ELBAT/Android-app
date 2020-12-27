package be.henallux.ig3.smartcity.elbatapp.service.mappers;

import be.henallux.ig3.smartcity.elbatapp.data.model.Address;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.AddressDto;

public class AddressMapper {
    private static AddressMapper instance;

    static {
        instance = null;
    }

    public static AddressMapper getInstance() {
        if (instance == null)
            instance = new AddressMapper();

        return instance;
    }

    public Address mapToAddress(AddressDto dto) {
        return dto == null ? null : new Address(dto.getStreet(), dto.getNumber(), dto.getCity(), dto.getPostalCode(), dto.getCountry());
    }
}
