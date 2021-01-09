package be.henallux.ig3.smartcity.elbatapp.ui.bookings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import be.henallux.ig3.smartcity.elbatapp.R;
import be.henallux.ig3.smartcity.elbatapp.data.model.Reservation;

public class BookingsListFragment extends Fragment {

    private BookingsListViewModel bookingsListViewModel;
    private RecyclerView reservationsRecyclerView;
    private TextView error;
    private TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookings_list, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.menu_my_bookings));

        reservationsRecyclerView = root.findViewById(R.id.bookings_list);
        error = root.findViewById(R.id.error_list);
        bookingsListViewModel = new ViewModelProvider(requireActivity()).get(BookingsListViewModel.class);

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int position = sharedPreferences.getInt("tabPosition", 0);

        bookingsListViewModel.loadBookings();

        // pour init par défaut le premier tab item
        BookingAdapter adapter = new BookingAdapter();

        switch (position){
            case 1 :
                bookingsListViewModel.getCanceledBookings().observe(getViewLifecycleOwner(), adapter::setReservations);
                break;
            default :
                bookingsListViewModel.getBookings().observe(getViewLifecycleOwner(), adapter::setReservations);
                break;
        }

        reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        reservationsRecyclerView.setAdapter(adapter);

        tabLayout = root.findViewById(R.id.tab_layout);
        tabLayout.getTabAt(position).select();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int currentPosition = tab.getPosition();
                editor.putInt("tabPosition", currentPosition);
                editor.commit();

                switch (currentPosition){
                    case 1 :
                        bookingsListViewModel.getCanceledBookings().observe(getViewLifecycleOwner(), adapter::setReservations);
                        break;
                    default :
                        bookingsListViewModel.getBookings().observe(getViewLifecycleOwner(), adapter::setReservations);
                        break;
                }

                reservationsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                reservationsRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // ignore
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // ignore
            }
        });

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
        public TextView date, time, establishment, person;

        public BookingViewHolder(@NonNull View itemView, OnItemSelectedListener listener){
            super(itemView);
            date = itemView.findViewById(R.id.booking_date);
            time = itemView.findViewById(R.id.booking_time);
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
            holder.date.setText(
                    date.get(Calendar.DAY_OF_MONTH) + "/" +
                    (date.get(Calendar.MONTH) + 1) + "/" +
                    date.get(Calendar.YEAR)
            );
            holder.time.setText(
                    date.get(Calendar.HOUR_OF_DAY) + "h" +
                    date.get(Calendar.MINUTE)
            );
            holder.establishment.setText(reservation.getEstablishmentName());
            holder.person.setText(reservation.getNbCustomers().toString());
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