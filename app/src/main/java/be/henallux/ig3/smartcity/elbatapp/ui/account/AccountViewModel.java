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

    public AccountViewModel(Application application) {
        super(application);
        this.webService = RetrofitConfigurationService.getInstance(getApplication()).getELBATWebService();
        this.userMapper = UserMapper.getInstance();
        this.passwordMapper = PasswordMapper.getInstance();
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

        String token = getApplication()
                .getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE)
                .getString("JSONWEBTOKEN", "");

        JWT jwt = new JWT(token);
        Claim userData = jwt.getClaim("userData");
        Integer userId = Objects.requireNonNull(userData.asObject(User.class)).getId();

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
        String token = getApplication()
                .getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE)
                .getString("JSONWEBTOKEN", "");

        JWT jwt = new JWT(token);
        Claim userData = jwt.getClaim("userData");
        String username = Objects.requireNonNull(userData.asObject(User.class)).getUsername();
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
}