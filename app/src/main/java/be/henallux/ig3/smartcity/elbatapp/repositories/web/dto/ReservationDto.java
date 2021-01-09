package be.henallux.ig3.smartcity.elbatapp.repositories.web.dto;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Objects;

public class ReservationDto {

    private Integer personId;
    private String dateTimeReserved;
    private Integer nbCustomers;
    private String additionalInfo;
    private Boolean isCancelled;
    private Boolean isOutside;
    private String establishmentName;

    public Integer getPersonId() {
        return personId;
    }

    public GregorianCalendar getDateTimeReserved() throws ParseException {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

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
}
