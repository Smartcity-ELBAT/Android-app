package be.henallux.ig3.smartcity.elbatapp.ui.tableBooking;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import be.henallux.ig3.smartcity.elbatapp.data.model.Establishment;
import be.henallux.ig3.smartcity.elbatapp.data.model.NetworkError;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.ELBATWebService;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.RetrofitConfigurationService;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.EstablishmentDto;
import be.henallux.ig3.smartcity.elbatapp.service.mappers.EstablishmentMapper;
import be.henallux.ig3.smartcity.elbatapp.utils.errors.NoConnectivityException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EstablishmentsMapViewModel extends AndroidViewModel {
    private MutableLiveData<List<Establishment>> _establishments = new MutableLiveData<>();
    private LiveData<List<Establishment>> establishments = _establishments;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private ELBATWebService webService;
    private EstablishmentMapper establishmentMapper;

    public EstablishmentsMapViewModel(@NotNull Application application) {
        super(application);

        this.webService = RetrofitConfigurationService
                .getInstance(getApplication())
                .getELBATWebService();

        this.establishmentMapper = EstablishmentMapper.getInstance();
    }

    public void requestEstablishments() {
        String token = "Bearer " + getApplication()
                .getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE)
                .getString("JSONWEBTOKEN", "");

        webService.getAllEstablishments(token).enqueue(new Callback<List<EstablishmentDto>>() {
            @Override
            public void onResponse(@NotNull Call<List<EstablishmentDto>> call, @NotNull Response<List<EstablishmentDto>> response) {
                if (response.isSuccessful()) {
                    _establishments.setValue(establishmentMapper.mapToEstablishments(response.body()));
                    _error.setValue(null);
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<EstablishmentDto>> call, @NotNull Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public LiveData<List<Establishment>> getEstablishments() {
        return establishments;
    }
}