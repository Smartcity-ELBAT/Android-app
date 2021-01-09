package be.henallux.ig3.smartcity.elbatapp.ui.tableBooking;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.GregorianCalendar;

import be.henallux.ig3.smartcity.elbatapp.data.model.NetworkError;
import be.henallux.ig3.smartcity.elbatapp.data.model.Establishment;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.ELBATWebService;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.RetrofitConfigurationService;

public class ReservationViewModel extends AndroidViewModel {
    private Establishment selectedEstablishment;
    private GregorianCalendar chosenDateAndTime;

    private MutableLiveData<Integer> _serverResponse = new MutableLiveData<>();
    private LiveData<Integer> serverResponse = _serverResponse;
    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

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

    public LiveData<Integer> getServerResponse() {
        return serverResponse;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }

    public void makeReservation() {

    }
}