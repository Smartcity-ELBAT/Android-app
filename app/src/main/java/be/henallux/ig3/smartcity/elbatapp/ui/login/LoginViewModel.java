package be.henallux.ig3.smartcity.elbatapp.ui.login;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import org.jetbrains.annotations.NotNull;

import be.henallux.ig3.smartcity.elbatapp.R;
import be.henallux.ig3.smartcity.elbatapp.data.model.NetworkError;
import be.henallux.ig3.smartcity.elbatapp.data.model.PositiveToCovid;
import be.henallux.ig3.smartcity.elbatapp.data.model.User;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.ELBATWebService;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.RetrofitConfigurationService;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.LoginCredentialsDto;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.PositiveToCovidDto;
import be.henallux.ig3.smartcity.elbatapp.service.mappers.PositiveToCovidMapper;
import be.henallux.ig3.smartcity.elbatapp.utils.InputCheck;
import be.henallux.ig3.smartcity.elbatapp.utils.errors.NoConnectivityException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();

    private MutableLiveData<User> _user = new MutableLiveData<>();
    private LiveData<User> user = _user;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private MutableLiveData<Integer> _statutCode = new MutableLiveData<>();
    private LiveData<Integer> statutCode = _statutCode;

    private MutableLiveData<PositiveToCovid> _contactWithPersonAtRisk = new MutableLiveData<>();
    private LiveData<PositiveToCovid> contactWithPersonAtRisk = _contactWithPersonAtRisk;

    private ELBATWebService webService;

    private String token;
    private PositiveToCovidMapper positiveToCovidMapper;

    public LoginViewModel(@NotNull Application application) {
        super(application);

        this.webService = RetrofitConfigurationService.getInstance(application).getELBATWebService();
        this.positiveToCovidMapper = PositiveToCovidMapper.getInstance();
        _error.setValue(null);
    }

    public LiveData<User> getLoginResult() {
        return user;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }

    public LiveData<Integer> getStatutCode() {
        return statutCode;
    }

    public LiveData<PositiveToCovid> getContactWithPersonAtRisk() {
        return contactWithPersonAtRisk;
    }

    public String getToken() {
        return token;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public void login(String username, String password) {
        webService.login(new LoginCredentialsDto(username, password)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.isSuccessful()) {
                    JWT jwt = new JWT(response.body());
                    Claim userData = jwt.getClaim("userData");

                    token = response.body();
                    _user.setValue(userData.asObject(User.class));
                    _error.setValue(null);
                }
                _statutCode.setValue(response.code());
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void checkReservationsContactCovid(){
        webService.checkReservationContactCovid("Bearer " + token, _user.getValue().getId()).enqueue(new Callback<PositiveToCovidDto>() {
            @Override
            public void onResponse(@NotNull Call<PositiveToCovidDto> call, @NotNull Response<PositiveToCovidDto> response) {
                if(response.isSuccessful())
                    _contactWithPersonAtRisk.setValue(positiveToCovidMapper.mapToPositiveToCovid(response.body()));

                _statutCode.setValue(response.code());
                _error.setValue(null);
            }

            @Override
            public void onFailure(@NotNull Call<PositiveToCovidDto> call, @NotNull Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });

    }

    public void loginDataChanged(String username, String password) {
        if (!InputCheck.isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!InputCheck.isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }
}