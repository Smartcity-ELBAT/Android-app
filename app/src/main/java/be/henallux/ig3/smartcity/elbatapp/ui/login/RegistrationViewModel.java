package be.henallux.ig3.smartcity.elbatapp.ui.login;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import be.henallux.ig3.smartcity.elbatapp.R;
import be.henallux.ig3.smartcity.elbatapp.data.model.Address;
import be.henallux.ig3.smartcity.elbatapp.data.model.NetworkError;
import be.henallux.ig3.smartcity.elbatapp.data.model.User;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.ELBATWebService;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.RetrofitConfigurationService;
import be.henallux.ig3.smartcity.elbatapp.service.mappers.UserMapper;
import be.henallux.ig3.smartcity.elbatapp.utils.InputCheck;
import be.henallux.ig3.smartcity.elbatapp.utils.errors.NoConnectivityException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationViewModel extends AndroidViewModel {

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private MutableLiveData<Integer> _statutCode = new MutableLiveData<>();
    private LiveData<Integer> statutCode = _statutCode;

    private MutableLiveData<HashMap<String , String>> _inputErrors = new MutableLiveData<>();
    private LiveData<HashMap<String, String>> inputErrors = _inputErrors;

    private ELBATWebService webService;
    private UserMapper userMapper;

    public RegistrationViewModel(@NotNull Application application) {
        super(application);
        this.webService = RetrofitConfigurationService.getInstance(getApplication()).getELBATWebService();
        this.userMapper = UserMapper.getInstance();
    }

    public LiveData<HashMap<String, String>> getInputErrors() {
        return inputErrors;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }

    public LiveData<Integer> getStatutCode() {
        return statutCode;
    }

    @SuppressLint("ResourceType")
    public void registerDataChanged(String username, String password, String passwordConfirm, String lastName, String firstName,
                                    String birthDate, String email, String phoneNumber,
                                    String street, String streetNumber, String city, String postalCode, String country){

        HashMap<String, String> errors = new HashMap<>();

        if(!InputCheck.isWordValid(username))
            errors.put("username", getApplication().getResources().getString(R.string.usernameError));

        if(!InputCheck.isPasswordValid(password))
            errors.put("password", getApplication().getResources().getString(R.string.invalid_password));

        if(!InputCheck.isPasswordValid(passwordConfirm) || !InputCheck.isPasswordConfirm(password, passwordConfirm))
            errors.put("passwordConfirm", getApplication().getResources().getString(R.string.invalid_password_confirm));

        if(!InputCheck.isWordValid(lastName))
            errors.put("lastName", getApplication().getResources().getString(R.string.invalid_lastName));

        if(!InputCheck.isWordValid(firstName))
            errors.put("firstName", getApplication().getResources().getString(R.string.invalid_firstName));

        if(!InputCheck.isDateValid(birthDate))
            errors.put("birthDate", getApplication().getResources().getString(R.string.invalid_birth_date));
        else if(!InputCheck.isAgeValid(birthDate))
            errors.put("birthDateAge", getApplication().getResources().getString(R.string.birthDate_age));

        if(!InputCheck.isEmailValid(email))
            errors.put("email", getApplication().getResources().getString(R.string.invalid_email));

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

    public void addUser(String username, String password, String lastName, String firstName,
                        String birthDate, String gender, String email, String phoneNumber,
                        String street, String streetNumber, String city, String postalCode, String country){

        Address address = new Address(street, streetNumber, postalCode, city, country);
        char sexe = gender.equals(getApplication().getResources().getString(R.string.woman)) ? 'F' : gender.equals(getApplication().getResources().getString(R.string.man)) ? 'M' : 'A';
        User user = new User(username, password, lastName, firstName, birthDate, sexe, email, phoneNumber, address);

        webService.addUser(userMapper.mapToUserDto(user)).enqueue(new Callback<Void>() {
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
}
