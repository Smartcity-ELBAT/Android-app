package be.henallux.ig3.smartcity.elbatapp.ui.tableBooking;

import android.app.Application;
import android.content.Context;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.GregorianCalendar;
import java.util.List;

import be.henallux.ig3.smartcity.elbatapp.data.model.NetworkError;
import be.henallux.ig3.smartcity.elbatapp.data.model.Establishment;
import be.henallux.ig3.smartcity.elbatapp.data.model.Reservation;
import be.henallux.ig3.smartcity.elbatapp.data.model.Table;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.ELBATWebService;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.RetrofitConfigurationService;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.TableDto;
import be.henallux.ig3.smartcity.elbatapp.service.mappers.ReservationMapper;
import be.henallux.ig3.smartcity.elbatapp.service.mappers.TableMapper;
import be.henallux.ig3.smartcity.elbatapp.utils.errors.NoConnectivityException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationViewModel extends AndroidViewModel {
    private Establishment selectedEstablishment;

    private MutableLiveData<List<Table>> _tablesGotten = new MutableLiveData<>();
    private LiveData<List<Table>> availableTables = _tablesGotten;
    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;
    private MutableLiveData<Integer> _statusCode = new MutableLiveData<>();
    private LiveData<Integer> status = _statusCode;

    private ELBATWebService webService;

    public ReservationViewModel(@NonNull Application application) {
        super(application);

        this.webService = RetrofitConfigurationService.getInstance(application).getELBATWebService();
    }

    public void setEstablishment(Establishment establishment) {
        selectedEstablishment = establishment;
    }

    public Establishment getSelectedEstablishment() {
        return selectedEstablishment;
    }

    public LiveData<List<Table>> getAvailableTables() {
        return availableTables;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }

    public LiveData<Integer> getStatus() {
        return status;
    }

    private String getToken() {
        return "Bearer " + getApplication()
                .getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE)
                .getString("JSONWEBTOKEN", "");
    }

    public void requestTables(GregorianCalendar calendar) {
        webService.getAvailableTablesForDate(
                getToken(),
                selectedEstablishment.getId(),
                (String) DateFormat.format("yyyy-MM-dd", calendar)
        ).enqueue(new Callback<List<TableDto>>() {
            @Override
            public void onResponse(@NotNull Call<List<TableDto>> call, @NotNull Response<List<TableDto>> response) {
                if (response.isSuccessful())
                    _tablesGotten.setValue(TableMapper.getInstance().mapTableDtosToTables(response.body()));
                else {
                    _statusCode.setValue(response.code());
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<TableDto>> call, @NotNull Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void makeReservation(Reservation reservation) {
        webService.makeReservation(getToken(), ReservationMapper.getInstance().mapToReservationDto(reservation)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                _statusCode.setValue(response.code());
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }
}