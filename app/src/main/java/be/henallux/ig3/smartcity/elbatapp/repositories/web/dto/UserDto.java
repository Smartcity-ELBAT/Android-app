package be.henallux.ig3.smartcity.elbatapp.repositories.web.dto;

public class UserDto {
    private Integer id;
    private String username;
    private String password;
    private String lastName;
    private String firstName;
    private String birthDate;
    private Character gender;
    private String phoneNumber;
    private String email;
    private Boolean isPositiveToCovid19;
    private AddressDto address;

    public UserDto(String username, String password, String lastName, String firstName, String birthDate,
                   Character gender, String phoneNumber, String email, Boolean isPositiveToCovid19, AddressDto address) {
        this.username = username;
        this.password = password;
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String  birthDate) {
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

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }
}
