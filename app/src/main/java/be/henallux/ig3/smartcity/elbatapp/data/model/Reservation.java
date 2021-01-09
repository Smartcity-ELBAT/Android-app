package be.henallux.ig3.smartcity.elbatapp.data.model;

import java.util.GregorianCalendar;

public class Reservation {
    private Integer personId;
    private GregorianCalendar dateTimeReserved;
    private Integer nbCustomers;
    private String additionalInfo;
    private Boolean isCancelled;
    private Boolean isOutside;
    private String establishmentName;

    public Reservation(Integer personId, GregorianCalendar dateTimeReserved,
                       Integer nbCustomers, String additionalInfo, Boolean isCancelled,
                       Boolean isOutside, String establishmentName) {
        this.personId = personId;
        this.dateTimeReserved = dateTimeReserved;
        this.nbCustomers = nbCustomers;
        this.additionalInfo = additionalInfo;
        this.isCancelled = isCancelled;
        this.isOutside = isOutside;
        this.establishmentName = establishmentName;
    }

    public Integer getPersonId() {
        return personId;
    }

    public GregorianCalendar getDateTimeReserved() {
        return dateTimeReserved;
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
