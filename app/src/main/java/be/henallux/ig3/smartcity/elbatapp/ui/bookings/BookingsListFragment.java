package be.henallux.ig3.smartcity.elbatapp.ui.bookings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import be.henallux.ig3.smartcity.elbatapp.R;
import be.henallux.ig3.smartcity.elbatapp.data.model.Reservation;

public class BookingsListFragment extends Fragment {

    private BookingsListViewModel bookingsListViewModel;
    private RecyclerView reservationsRecyclerView;
    private TextView error;

    // TODO je ne sais pas mettre les viewModel dans onActivityCreated sinon ça fait une erreur avec le RecyclerView

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookings_list, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.menu_my_bookings));

        reservationsRecyclerView = root.findViewById(R.id.bookings_list);
        error = root.findViewById(R.id.error_list);
        bookingsListViewModel = new ViewModelProvider(requireActivity()).get(BookingsListViewModel.class);

        bookingsListViewModel.loadBookings();

        BookingAdapter adapter = new BookingAdapter();
        bookingsListViewModel.getBookings().observe(getViewLifecycleOwner(), adapter::setReservations);

        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        reservationsRecyclerView.setAdapter(adapter);

        bookingsListViewModel.getError().observe(getViewLifecycleOwner(), networkError -> {
            error.setText(networkError.getErrorMessage());
        });

        bookingsListViewModel.getStatutCode().observe(getViewLifecycleOwner(), integer -> {
            if(integer == 400)
                error.setText(R.string.error_400_load_bookings);
            else if(integer == 401)
                error.setText(R.string.error_401_unauthorized);
            else if(integer == 404)
                error.setText(R.string.error_404_load_bookings);
            else if(integer == 500)
                error.setText(R.string.error_500);
        });

        return root;
    }

    // ViewHolder that describes a single element
    private class BookingViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTime, establishment, person;

        public BookingViewHolder(@NonNull View itemView, OnItemSelectedListener listener){
            super(itemView);
            dateTime = itemView.findViewById(R.id.booking_date_time);
            establishment = itemView.findViewById(R.id.booking_establishment);
            person = itemView.findViewById(R.id.booking_nb_person);

            itemView.setOnClickListener(e -> {
                int currentPosition = getAdapterPosition();
                listener.onItemSelected(currentPosition);
            });
        }
    }

    // Adapter will handle changes and layouts for each item
    private class BookingAdapter extends RecyclerView.Adapter<BookingViewHolder>{
        private List<Reservation> reservations;

        @NonNull
        @Override
        public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout linearLayout = (LinearLayout)LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_booking_item, parent, false);

            BookingViewHolder viewHolder = new BookingViewHolder(linearLayout, position -> {
                bookingsListViewModel.setBookingChosen(reservations.get(position));
                Navigation.findNavController(requireView()).navigate(R.id.action_nav_bookings_to_bookingDetailsFragment3);
            });

            return viewHolder;
        }

        @SuppressLint({"SetTextI18n", "ResourceAsColor"})
        @Override
        public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
            Reservation reservation = reservations.get(position);
            GregorianCalendar date = reservation.getDateTimeReserved();
            holder.dateTime.setText(
                            date.get(Calendar.DAY_OF_MONTH) + "/" +
                            (date.get(Calendar.MONTH) + 1) + "/" +
                            date.get(Calendar.YEAR) + " " +
                            date.get(Calendar.HOUR_OF_DAY) + "h" +
                            date.get(Calendar.MINUTE)
            );
            holder.establishment.setText(reservation.getEstablishmentName());
            holder.person.setText(reservation.getNbCustomers().toString());

            if(reservation.getCancelled() != null && reservation.getCancelled())
                holder.itemView.setBackgroundColor(R.color.cancel);
        }

        @Override
        public int getItemCount() {
            return reservations == null ? 0 : reservations.size();
        }

        public void setReservations(List<Reservation> reservations){
            this.reservations = reservations;
            notifyDataSetChanged();
        }
    }
}