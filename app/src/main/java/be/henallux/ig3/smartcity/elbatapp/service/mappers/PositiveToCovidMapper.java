package be.henallux.ig3.smartcity.elbatapp.service.mappers;

import be.henallux.ig3.smartcity.elbatapp.data.model.PositiveToCovid;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.PositiveToCovidDto;

public class PositiveToCovidMapper {
    private static PositiveToCovidMapper instance;

    static {
        instance = null;
    }

    public static PositiveToCovidMapper getInstance() {
        if (instance == null)
            instance = new PositiveToCovidMapper();

        return instance;
    }

    public PositiveToCovid mapToPositiveToCovid(PositiveToCovidDto isPositiveToCovidDto) {
        return new PositiveToCovid(isPositiveToCovidDto.getPositifToCovid());
    }
}
