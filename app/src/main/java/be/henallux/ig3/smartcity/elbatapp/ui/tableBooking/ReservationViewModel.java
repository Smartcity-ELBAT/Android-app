package be.henallux.ig3.smartcity.elbatapp.ui.tableBooking;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import be.henallux.ig3.smartcity.elbatapp.data.model.Establishment;

public class ReservationViewModel extends AndroidViewModel {
    private Establishment selectedEstablishment;

    public ReservationViewModel(@NonNull Application application) {
        super(application);
    }

    public void setEstablishment(Establishment establishment) {
        selectedEstablishment = establishment;
    }

    public Establishment getSelectedEstablishment() {
        return selectedEstablishment;
    }
}