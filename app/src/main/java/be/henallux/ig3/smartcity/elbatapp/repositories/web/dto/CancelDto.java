package be.henallux.ig3.smartcity.elbatapp.repositories.web.dto;

public class CancelDto {
    private Integer idPerson;
    private String dateTimeReserved;

    public CancelDto(Integer idPerson, String dateTimeReserved) {
        this.idPerson = idPerson;
        this.dateTimeReserved = dateTimeReserved;
    }
}
