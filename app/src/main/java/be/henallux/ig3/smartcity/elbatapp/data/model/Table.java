package be.henallux.ig3.smartcity.elbatapp.data.model;

public class Table {
    private Integer id;
    private Integer establishmentId;
    private Integer nbSeats;
    private Boolean isOutside;

    public Table(Integer id, Integer establishmentId, Integer nbSeats, Boolean isOutside) {
        this.id = id;
        this.establishmentId = establishmentId;
        this.nbSeats = nbSeats;
        this.isOutside = isOutside;
    }

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
