package be.henallux.ig3.smartcity.elbatapp.data.model;

import java.util.GregorianCalendar;

public class Reservation {
    private Integer personId;
    private GregorianCalendar dateTimeReserved;
    private Integer nbCustomers;
    private String additionalInfo;
    private Boolean isCancelled;
    private Boolean isOutside;
    private Integer establishmentId;
    private String establishmentName;
    private Integer tableId;

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

    public Reservation() {}

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

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    public void setDateTimeReserved(GregorianCalendar dateTimeReserved) {
        this.dateTimeReserved = dateTimeReserved;
    }

    public void setNbCustomers(Integer nbCustomers) {
        this.nbCustomers = nbCustomers;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void setCancelled(Boolean cancelled) {
        isCancelled = cancelled;
    }

    public void setOutside(Boolean outside) {
        isOutside = outside;
    }

    public void setEstablishmentName(String establishmentName) {
        this.establishmentName = establishmentName;
    }

    public Integer getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(Integer establishmentId) {
        this.establishmentId = establishmentId;
    }

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }
}
