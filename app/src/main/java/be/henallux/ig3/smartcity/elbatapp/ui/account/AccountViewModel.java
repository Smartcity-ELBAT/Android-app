package be.henallux.ig3.smartcity.elbatapp.ui.account;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import java.util.HashMap;
import java.util.Objects;

import be.henallux.ig3.smartcity.elbatapp.R;
import be.henallux.ig3.smartcity.elbatapp.data.model.Address;
import be.henallux.ig3.smartcity.elbatapp.data.model.NetworkError;
import be.henallux.ig3.smartcity.elbatapp.data.model.Password;
import be.henallux.ig3.smartcity.elbatapp.data.model.User;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.ELBATWebService;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.RetrofitConfigurationService;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.UserDto;
import be.henallux.ig3.smartcity.elbatapp.service.mappers.PasswordMapper;
import be.henallux.ig3.smartcity.elbatapp.service.mappers.UserMapper;
import be.henallux.ig3.smartcity.elbatapp.utils.InputCheck;
import be.henallux.ig3.smartcity.elbatapp.utils.errors.NoConnectivityException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountViewModel extends AndroidViewModel {

    private MutableLiveData<User> _user = new MutableLiveData<>();
    private LiveData<User> user = _user;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private MutableLiveData<HashMap<String , String>> _inputErrors = new MutableLiveData<>();
    private LiveData<HashMap<String, String>> inputErrors = _inputErrors;

    private MutableLiveData<Integer> _statutCode = new MutableLiveData<>();
    private LiveData<Integer> statutCode = _statutCode;

    private ELBATWebService webService;
    private UserMapper userMapper;
    private PasswordMapper passwordMapper;
    private String token;
    private User userFromToken;

    public AccountViewModel(Application application) {
        super(application);
        this.webService = RetrofitConfigurationService.getInstance(getApplication()).getELBATWebService();
        this.userMapper = UserMapper.getInstance();
        this.passwordMapper = PasswordMapper.getInstance();

        token = getApplication()
                .getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE)
                .getString("JSONWEBTOKEN", "");

        JWT jwt = new JWT(token);
        Claim userData = jwt.getClaim("userData");
        userFromToken = Objects.requireNonNull(userData.asObject(User.class));
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }

    public LiveData<HashMap<String, String>> getInputErrors() {
        return inputErrors;
    }

    public LiveData<Integer> getStatutCode() {
        return statutCode;
    }

    public void loadUser(){
        Integer userId = userFromToken.getId();

        webService.getUserById("Bearer " + token, userId).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                if (response.isSuccessful())
                    _user.setValue(userMapper.mapToUser(response.body()));
                else
                    _statutCode.setValue(response.code());
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void updatePassword(String currentPassword, String newPassword){
        String username = userFromToken.getUsername();
        Password password = new Password(currentPassword, newPassword, username);

        webService.updatePassword("Bearer " + token, passwordMapper.mapToPasswordDto(password)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                _statutCode.setValue(response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void updateUser(String lastName, String firstName, String birthDate, String gender, String phoneNumber,
                           String street, String number, String postalCode, String locality, String country){

        Address address = new Address(street, number, postalCode, locality, country);
        char sexe = gender.equals(getApplication().getResources().getString(R.string.woman)) ? 'F' : gender.equals(getApplication().getResources().getString(R.string.man)) ? 'M' : 'A';
        User user = new User(lastName, firstName, birthDate, phoneNumber, sexe, address);

        webService.updateUser("Bearer " + token, userMapper.mapToUserDto(user)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                _statutCode.setValue(response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void passwordDataChanged(String currentPassword, String newPassword, String confirmNewPassword){
        HashMap<String, String> errors = new HashMap<>();

        if(!InputCheck.isPasswordValid(currentPassword))
            errors.put("currentPassword", getApplication().getResources().getString(R.string.invalid_password));

        if(!InputCheck.isPasswordValid(newPassword))
            errors.put("newPassword", getApplication().getResources().getString(R.string.invalid_password));

        if(!InputCheck.isPasswordValid(confirmNewPassword) || !InputCheck.isPasswordConfirm(newPassword, confirmNewPassword))
            errors.put("passwordConfirm", getApplication().getResources().getString(R.string.invalid_password_confirm));

        _inputErrors.setValue(errors);
    }

    public void userDataChanged(String lastName, String firstName, String birthDate, String phoneNumber,
                                    String street, String streetNumber, String city, String postalCode, String country){

        HashMap<String, String> errors = new HashMap<>();

        if(!InputCheck.isWordValid(lastName))
            errors.put("lastName", getApplication().getResources().getString(R.string.invalid_lastName));

        if(!InputCheck.isWordValid(firstName))
            errors.put("firstName", getApplication().getResources().getString(R.string.invalid_firstName));

        if(!InputCheck.isDateValid(birthDate))
            errors.put("birthDate", getApplication().getResources().getString(R.string.invalid_birth_date));
        else if(!InputCheck.isAgeValid(birthDate))
            errors.put("birthDateAge", getApplication().getResources().getString(R.string.birthDate_age));

        if(!InputCheck.isPhoneValid(phoneNumber))
            errors.put("phone", getApplication().getResources().getString(R.string.invalid_phoneNumber));

        if(!InputCheck.isWordValid(street))
            errors.put("street", getApplication().getResources().getString(R.string.invalid_field));

        if(!InputCheck.isWordValid(streetNumber))
            errors.put("streetNumber", getApplication().getResources().getString(R.string.invalid_field));

        if(!InputCheck.isWordValid(city))
            errors.put("city", getApplication().getResources().getString(R.string.invalid_field));

        if(!InputCheck.isWordValid(postalCode))
            errors.put("postalCode", getApplication().getResources().getString(R.string.invalid_field));

        if(!InputCheck.isWordValid(country))
            errors.put("country", getApplication().getResources().getString(R.string.invalid_field));

        _inputErrors.setValue(errors);
    }
}