package be.henallux.ig3.smartcity.elbatapp.repositories.web.dto;

public class TableDto {
    private Integer id;
    private Integer establishmentId;
    private Integer nbSeats;
    private Boolean isOutside;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEstablishmentId() {
        return establishmentId;
    }

    public void setEstablishmentId(Integer establishmentId) {
        this.establishmentId = establishmentId;
    }

    public Integer getNbSeats() {
        return nbSeats;
    }

    public void setNbSeats(Integer nbSeats) {
        this.nbSeats = nbSeats;
    }

    public Boolean getOutside() {
        return isOutside;
    }

    public void setOutside(Boolean outside) {
        isOutside = outside;
    }
}
