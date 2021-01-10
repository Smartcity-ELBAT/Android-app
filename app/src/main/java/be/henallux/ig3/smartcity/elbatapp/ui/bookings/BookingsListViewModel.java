package be.henallux.ig3.smartcity.elbatapp.ui.bookings;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import be.henallux.ig3.smartcity.elbatapp.data.model.NetworkError;
import be.henallux.ig3.smartcity.elbatapp.data.model.Reservation;
import be.henallux.ig3.smartcity.elbatapp.data.model.User;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.ELBATWebService;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.RetrofitConfigurationService;
import be.henallux.ig3.smartcity.elbatapp.repositories.web.dto.ReservationDto;
import be.henallux.ig3.smartcity.elbatapp.service.mappers.CancelMapper;
import be.henallux.ig3.smartcity.elbatapp.service.mappers.ReservationMapper;
import be.henallux.ig3.smartcity.elbatapp.utils.errors.NoConnectivityException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingsListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Reservation>> _bookings = new MutableLiveData<>();
    private LiveData<List<Reservation>> bookings = _bookings;

    private MutableLiveData<List<Reservation>> _canceledBookings = new MutableLiveData<>();
    private LiveData<List<Reservation>> canceledBookings = _canceledBookings;

    private MutableLiveData<Reservation> _bookingChosen = new MutableLiveData<>();
    private LiveData<Reservation> bookingChosen = _bookingChosen;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private MutableLiveData<Integer> _statutCode = new MutableLiveData<>();
    private LiveData<Integer> statutCode = _statutCode;

    private ELBATWebService webService;
    private ReservationMapper reservationMapper;
    private CancelMapper cancelMapper;
    private String token;
    private Integer userId;

    public BookingsListViewModel(Application application) {
        super(application);
        this.webService = RetrofitConfigurationService.getInstance(getApplication()).getELBATWebService();
        this.reservationMapper = ReservationMapper.getInstance();
        this.cancelMapper = CancelMapper.getInstance();

        token = getApplication()
                .getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE)
                .getString("JSONWEBTOKEN", "");

        JWT jwt = new JWT(token);
        Claim userData = jwt.getClaim("userData");
        userId = Objects.requireNonNull(userData.asObject(User.class)).getId();

        _error.setValue(null);
    }

    public LiveData<List<Reservation>> getBookings() {
        return bookings;
    }

    public LiveData<List<Reservation>> getCanceledBookings() {
        return canceledBookings;
    }

    public LiveData<Reservation> getBookingChosen() {
        return bookingChosen;
    }

    public void setBookingChosen(Reservation bookingChosen) {
        _bookingChosen.setValue(bookingChosen);
    }

    public LiveData<NetworkError> getError() {
        return error;
    }

    public LiveData<Integer> getStatutCode() {
        return statutCode;
    }

    public void loadBookings(){
        webService.getReservations("Bearer " + token, userId).enqueue(new Callback<List<ReservationDto>>() {
            @Override
            public void onResponse(@NotNull Call<List<ReservationDto>> call, @NotNull Response<List<ReservationDto>> response) {
                if(response.isSuccessful()){
                    List<Reservation> bookingsToCome = new ArrayList<>();
                    List<Reservation> canceledBookings = new ArrayList<>();
                    GregorianCalendar now = new GregorianCalendar();

                    for (Reservation booking : reservationMapper.mapToReservation(response.body())) {
                        if(now.compareTo(booking.getDateTimeReserved()) > 0 || booking.getCancelled())
                            canceledBookings.add(booking);
                        else
                            bookingsToCome.add(booking);
                    }
                    _bookings.setValue(bookingsToCome);
                    _canceledBookings.setValue(canceledBookings);
                }
                _statutCode.setValue(response.code());
                _error.setValue(null);
            }

            @Override
            public void onFailure(@NotNull Call<List<ReservationDto>> call, @NotNull Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });
    }

    public void cancelBooking(){
        webService.cancelReservations("Bearer " + token, cancelMapper.mapToCancelDto(userId, bookingChosen.getValue().getDateTimeReserved())).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                _statutCode.setValue(response.code());
                _error.setValue(null);
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                _error.setValue(t instanceof NoConnectivityException ? NetworkError.NO_CONNECTION : NetworkError.TECHNICAL_ERROR);
            }
        });

    }
}