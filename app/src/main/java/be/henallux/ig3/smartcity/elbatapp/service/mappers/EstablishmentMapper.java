package be.henallux.ig3.smartcity.elbatapp.service.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import be.henallux.ig3.smartcity.elbatapp.data.model.Establishment;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.EstablishmentDto;

public class EstablishmentMapper {
    private static EstablishmentMapper instance;

    static {
        instance = null;
    }

    public static EstablishmentMapper getInstance() {
        if (instance == null)
            instance = new EstablishmentMapper();
        return instance;
    }

    public List<Establishment> mapToEstablishments(List<EstablishmentDto> dto) {
        return dto == null ?
                null : dto
                .stream()
                .map(establishmentDto ->
                        new Establishment(
                                establishmentDto.getId(),
                                establishmentDto.getName(),
                                establishmentDto.getVATNumber(),
                                establishmentDto.getEmail(),
                                establishmentDto.getCategory(),
                                AddressMapper.getInstance().mapToAddress(establishmentDto.getStreet(), establishmentDto.getNumber(), establishmentDto.getCountry(), establishmentDto.getCity(), establishmentDto.getPostalCode())
                        )
                ).collect(Collectors.toCollection(ArrayList::new));
    }
}
