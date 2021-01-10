package be.henallux.ig3.smartcity.elbatapp.service.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import be.henallux.ig3.smartcity.elbatapp.data.model.Table;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.TableDto;

public class TableMapper {
    private static TableMapper instance;

    public static TableMapper getInstance() {
        if (instance == null)
            instance = new TableMapper();

        return instance;
    }

    public ArrayList<Table> mapTableDtosToTables(List<TableDto> tableDtos) {
        return tableDtos == null ? null :
                tableDtos
                        .stream()
                        .map(tableDto -> new Table(tableDto.getId(), tableDto.getEstablishmentId(), tableDto.getNbSeats(), tableDto.getOutside()))
                        .collect(Collectors.toCollection(ArrayList::new));
    }
}
