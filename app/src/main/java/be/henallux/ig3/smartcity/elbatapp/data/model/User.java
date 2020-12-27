package be.henallux.ig3.smartcity.elbatapp.data.model;

import java.util.Date;

import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.AddressDto;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String lastName;
    private String firstName;
    private Date birthDate;
    private Character gender;
    private Boolean isPositiveToCovid19;
    private Address address;

    public User(Integer id, String username, String password, String lastName, String firstName, Date birthDate, Character gender, Boolean isPositiveToCovid19, Address address) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.isPositiveToCovid19 = isPositiveToCovid19;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public Boolean getPositiveToCovid19() {
        return isPositiveToCovid19;
    }

    public void setPositiveToCovid19(Boolean positiveToCovid19) {
        isPositiveToCovid19 = positiveToCovid19;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}