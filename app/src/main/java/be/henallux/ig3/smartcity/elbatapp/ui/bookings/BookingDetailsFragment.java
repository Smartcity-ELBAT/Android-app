package be.henallux.ig3.smartcity.elbatapp.ui.bookings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.Calendar;
import java.util.GregorianCalendar;

import be.henallux.ig3.smartcity.elbatapp.R;

public class BookingDetailsFragment extends Fragment {
    private BookingsListViewModel bookingsListViewModel;
    private TextView error, name;
    private EditText date, table, nbCustomers, info;
    private Button cancelBooking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_booking_details, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.menu_my_bookings));

        name = root.findViewById(R.id.establishment_name_details);
        date = root.findViewById(R.id.date_details_value);
        table = root.findViewById(R.id.table_details_value);
        nbCustomers = root.findViewById(R.id.nb_person_details_value);
        info = root.findViewById(R.id.additional_info_details_value);
        error = root.findViewById(R.id.error_booking_details);
        error.setVisibility(View.INVISIBLE);
        error.setText(null);
        cancelBooking = root.findViewById(R.id.cancel_booking);

        cancelBooking.setOnClickListener(v -> bookingsListViewModel.cancelBooking());

        return root;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bookingsListViewModel = new ViewModelProvider(requireActivity()).get(BookingsListViewModel.class);

        bookingsListViewModel.getBookingChosen().observe(getViewLifecycleOwner(), reservation -> {
            name.setText(reservation.getEstablishmentName());
            name.setEnabled(false);

            GregorianCalendar reservationDate = reservation.getDateTimeReserved();
            date.setText(reservationDate.get(Calendar.DAY_OF_MONTH) + "/" +
                    (reservationDate.get(Calendar.MONTH) + 1) + "/" +
                    reservationDate.get(Calendar.YEAR) + " " +
                    reservationDate.get(Calendar.HOUR_OF_DAY) + "h" +
                    reservationDate.get(Calendar.MINUTE));
            date.setEnabled(false);

            table.setText(reservation.getOutside() ? getResources().getString(R.string.yes) : getResources().getString(R.string.no));
            table.setEnabled(false);

            nbCustomers.setText(reservation.getNbCustomers().toString());
            nbCustomers.setEnabled(false);

            info.setText(reservation.getAdditionalInfo());
            info.setEnabled(false);

            GregorianCalendar now = new GregorianCalendar();
            cancelBooking.setEnabled(!reservation.getCancelled() && now.compareTo(reservation.getDateTimeReserved()) < 0);
        });

        bookingsListViewModel.getError().observe(getViewLifecycleOwner(), networkError -> {
            if(networkError != null){
                error.setVisibility(View.VISIBLE);
                error.setText(networkError.getErrorMessage());
            }
        });

        bookingsListViewModel.getStatutCode().observe(getViewLifecycleOwner(), integer -> {
            if(integer != 204)
                error.setVisibility(View.VISIBLE);

            if(integer == 400)
                error.setText(R.string.error_400_cancel_booking);
            else if(integer == 401)
                error.setText(R.string.error_401_unauthorized);
            else if(integer == 404)
                error.setText(R.string.error_404_cancel_booking);
            else if(integer == 500)
                error.setText(R.string.error_500);
            else if(integer == 204){
                Toast.makeText(getActivity(), getResources().getString(R.string.booking_cancelled), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigate(R.id.action_bookingDetailsFragment3_to_nav_bookings);
            }
        });
    }
}