package be.henallux.ig3.smartcity.elbatapp.repositories.web.dto;


public class AddressDto {
    private Integer id;
    private String street;
    private String number;
    private String country;
    private String city;
    private String postalCode;

    public AddressDto(String street, String number, String country, String city, String postalCode) {
        this.street = street;
        this.number = number;
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
    }

    public AddressDto(Integer id, String street, String number, String country, String city, String postalCode) {
        this(street, number, postalCode, city, country);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
