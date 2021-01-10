package be.henallux.ig3.smartcity.elbatapp.repositories.web.dto;

import com.squareup.moshi.Json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class ReservationDto {

    @Json(name = "idPerson")
    private Integer personId;
    private String dateTimeReserved;
    private Integer nbCustomers;
    private String additionalInfo;
    private Boolean isCancelled;
    private Boolean isOutside;
    @Json(name = "idEstablishment")
    private Integer establishmentId;
    private String establishmentName;
    @Json(name = "idTable")
    private Integer tableId;

    public ReservationDto(Integer personId, String dateTimeReserved, Integer nbCustomers, String additionalInfo, Boolean isCancelled, Boolean isOutside, Integer establishmentId, String establishmentName, Integer tableId) {
        this.personId = personId;
        this.dateTimeReserved = dateTimeReserved;
        this.nbCustomers = nbCustomers;
        this.additionalInfo = additionalInfo;
        this.isCancelled = isCancelled;
        this.isOutside = isOutside;
        this.establishmentId = establishmentId;
        this.establishmentName = establishmentName;
        this.tableId = tableId;
    }

    public Integer getPersonId() {
        return personId;
    }

    public GregorianCalendar getDateTimeReserved() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.FRANCE);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(Objects.requireNonNull(format.parse(dateTimeReserved)));

        return calendar;
    }

    public Integer getNbCustomers() {
        return nbCustomers;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public Boolean getCancelled() {
        return isCancelled;
    }

    public Boolean getOutside() {
        return isOutside;
    }

    public String getEstablishmentName() {
        return establishmentName;
    }

    public Integer getEstablishmentId() {
        return establishmentId;
    }

    public Integer getTableId() {
        return tableId;
    }
}
