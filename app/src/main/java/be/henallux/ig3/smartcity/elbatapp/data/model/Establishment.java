package be.henallux.ig3.smartcity.elbatapp.data.model;

public class Establishment {
    private Integer id;
    private String name;
    private String VATNumber;
    private String email;
    private String category;
    private Address address;

    public Establishment(Integer id, String name, String VATNumber, String email, String category, Address address) {
        this.id = id;
        this.name = name;
        this.VATNumber = VATNumber;
        this.email = email;
        this.category = category;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVATNumber() {
        return VATNumber;
    }

    public void setVATNumber(String VATNumber) {
        this.VATNumber = VATNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
