package be.henallux.ig3.smartcity.elbatapp.service.mappers;

import be.henallux.ig3.smartcity.elbatapp.data.model.Address;

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

    public Address mapToAddress(String street,
            String number,
            String country,
            String city,
            String postalCode) {
        return new Address(street, number, country, city, postalCode);
    }
}
