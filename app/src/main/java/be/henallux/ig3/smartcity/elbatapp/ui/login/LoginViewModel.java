package be.henallux.ig3.smartcity.elbatapp.ui.login;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import android.app.Application;
import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import org.jetbrains.annotations.NotNull;

import be.henallux.ig3.smartcity.elbatapp.R;
import be.henallux.ig3.smartcity.elbatapp.data.model.NetworkError;
import be.henallux.ig3.smartcity.elbatapp.data.model.User;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.ELBATWebService;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.RetrofitConfigurationService;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.LoginCredentialsDto;
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

    private ELBATWebService webService;

    private String token;

    public LoginViewModel(@NotNull Application application) {
        super(application);

        this.webService = RetrofitConfigurationService.getInstance(application).getELBATWebService();
    }

    public LiveData<User> getLoginResult() {
        return user;
    }

    public LiveData<NetworkError> getError() {
        return error;
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
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        return username != null && !username.trim().isEmpty();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public String getToken() {
        return token;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }
}