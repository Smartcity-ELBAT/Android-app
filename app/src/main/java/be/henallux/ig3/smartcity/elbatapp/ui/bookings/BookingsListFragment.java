package be.henallux.ig3.smartcity.elbatapp.ui.bookings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.GregorianCalendar;
import java.util.List;

import be.henallux.ig3.smartcity.elbatapp.R;
import be.henallux.ig3.smartcity.elbatapp.data.model.NetworkError;
import be.henallux.ig3.smartcity.elbatapp.data.model.Reservation;

public class BookingsListFragment extends Fragment {

    private BookingsListViewModel bookingsListViewModel;
    private RecyclerView reservationsRecyclerView;
    private LinearLayout errorLayout;
    private TextView noBookingsView;
    private TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookings_list, container, false);

        reservationsRecyclerView = root.findViewById(R.id.bookings_list);
        errorLayout = root.findViewById(R.id.loading_error_view);
        noBookingsView = root.findViewById(R.id.no_bookings_view);
        bookingsListViewModel = new ViewModelProvider(requireActivity()).get(BookingsListViewModel.class);

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int position = sharedPreferences.getInt("tabPosition", 0);

        bookingsListViewModel.loadBookings();

        // pour init par défaut le premier tab item
        BookingAdapter adapter = new BookingAdapter();

        if (position == 1) {
            bookingsListViewModel.getCanceledBookings().observe(getViewLifecycleOwner(), adapter::setReservations);
        } else {
            bookingsListViewModel.getBookings().observe(getViewLifecycleOwner(), adapter::setReservations);
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
                editor.apply();

                if (currentPosition == 1) {
                    bookingsListViewModel.getCanceledBookings().observe(getViewLifecycleOwner(), adapter::setReservations);
                } else {
                    bookingsListViewModel.getBookings().observe(getViewLifecycleOwner(), adapter::setReservations);
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
            if (networkError != null) {
                final TextView errorTextView = root.findViewById(R.id.loading_error_text);
                final ImageView errorDrawable = root.findViewById(R.id.loading_error_image);

                errorDrawable.setImageResource(networkError == NetworkError.NO_CONNECTION ? R.drawable.ic_no_connectivity : R.drawable.ic_error);
                errorTextView.setText(networkError.getErrorMessage());

                errorLayout.setVisibility(View.VISIBLE);
            }
        });

        bookingsListViewModel.getStatutCode().observe(getViewLifecycleOwner(), statusCode -> {
            final TextView errorTextView = root.findViewById(R.id.loading_error_text);
            final ImageView errorDrawable = root.findViewById(R.id.loading_error_image);

            errorDrawable.setImageResource(R.drawable.ic_error);

            if(statusCode == 400)
                errorTextView.setText(R.string.error_400_load_bookings);
            else if(statusCode == 401)
                errorTextView.setText(R.string.error_401_unauthorized);
            else if(statusCode == 404)
                errorTextView.setText(R.string.error_404_load_bookings);
            else if(statusCode == 500)
                errorTextView.setText(R.string.error_500);
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

        @Override
        public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {

            Reservation reservation = reservations.get(position);
            GregorianCalendar date = reservation.getDateTimeReserved();

            holder.date.setText(DateFormat.format("dd/MM/yyyy", date));
            holder.time.setText(DateFormat.format("HH:mm", date));
            holder.establishment.setText(reservation.getEstablishmentName());
            holder.person.setText(getString(R.string.places, reservation.getNbCustomers()));
        }

        @Override
        public int getItemCount() {
            return reservations == null ? 0 : reservations.size();
        }

        public void setReservations(List<Reservation> reservations){
            noBookingsView.setVisibility(reservations.isEmpty() ? View.VISIBLE : View.GONE);

            this.reservations = reservations;
            notifyDataSetChanged();
        }
    }
}