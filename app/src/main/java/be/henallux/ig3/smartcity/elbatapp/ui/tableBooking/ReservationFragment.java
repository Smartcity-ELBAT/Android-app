package be.henallux.ig3.smartcity.elbatapp.ui.tableBooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import be.henallux.ig3.smartcity.elbatapp.R;
import be.henallux.ig3.smartcity.elbatapp.data.model.Reservation;
import be.henallux.ig3.smartcity.elbatapp.data.model.Table;
import be.henallux.ig3.smartcity.elbatapp.data.model.User;
import be.henallux.ig3.smartcity.elbatapp.utils.RangeTimePickerDialog;

public class ReservationFragment extends Fragment {

    private ReservationViewModel reservationViewModel;

    private Reservation reservation;

    private TextView chooseDateTextView;
    private TextView chooseTimeTextView;
    private SwitchMaterial tableOutsideSwitch;
    private AppCompatEditText guestsNbr;
    private AppCompatEditText extraInformation;
    private ProgressBar tablesLoadingBar;
    private TextView errorView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reservation, container, false);

        reservation = new Reservation();

        reservationViewModel = new ViewModelProvider(requireActivity()).get(ReservationViewModel.class);

        chooseDateTextView = root.findViewById(R.id.choose_date);
        chooseTimeTextView = root.findViewById(R.id.choose_hour);
        tableOutsideSwitch = root.findViewById(R.id.table_outside);
        guestsNbr = root.findViewById(R.id.guests_number);
        extraInformation = root.findViewById(R.id.requests_textview);
        tablesLoadingBar = root.findViewById(R.id.tables_loading_bar);
        errorView = root.findViewById(R.id.reservation_error_textview);

        final Button cancelButton = root.findViewById(R.id.booking_cancel_button);
        final Button bookButton = root.findViewById(R.id.booking_button);

        chooseDateTextView.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            final int calendarYear = reservation.getDateTimeReserved() != null ? reservation.getDateTimeReserved().get(Calendar.YEAR) : calendar.get(Calendar.YEAR);
            final int calendarMonth = reservation.getDateTimeReserved() != null ? reservation.getDateTimeReserved().get(Calendar.MONTH) : calendar.get(Calendar.MONTH);
            final int calendarDay = reservation.getDateTimeReserved() != null ? reservation.getDateTimeReserved().get(Calendar.DAY_OF_MONTH) : calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireActivity(),
                    R.style.DateTimePickerTheme,
                    (view, year, month, dayOfMonth) -> {
                        reservation.getDateTimeReserved().set(year, month, dayOfMonth);

                        chooseDateTextView.setText(getString(R.string.choose_date, "\n" + DateFormat.format("dd/MM/yyyy", reservation.getDateTimeReserved())));

                        loadAvailableTables();
                        errorView.setVisibility(View.GONE);
                    }, calendarYear, calendarMonth, calendarDay
            );

            datePickerDialog.getDatePicker().setMinDate(Date.from(Instant.now()).getTime());

            datePickerDialog.updateDate(calendarYear, calendarMonth, calendarDay);
            datePickerDialog.show();

            chooseTimeTextView.setText(getString(R.string.choose_arrival_time, ""));

            if (reservation.getDateTimeReserved() == null)
                reservation.setDateTimeReserved(new GregorianCalendar());

            reservation.getDateTimeReserved().set(
                    reservation.getDateTimeReserved().get(Calendar.YEAR),
                    reservation.getDateTimeReserved().get(Calendar.MONTH),
                    reservation.getDateTimeReserved().get(Calendar.DAY_OF_MONTH),
                    0, 0
            );

        });

        chooseTimeTextView.setOnClickListener(v -> {
            RangeTimePickerDialog timePickerDialog = new RangeTimePickerDialog(
                    requireActivity(),
                    R.style.DateTimePickerTheme,
                    (view, hourOfDay, minute) -> {
                        reservation.getDateTimeReserved().add(Calendar.HOUR_OF_DAY, hourOfDay);
                        reservation.getDateTimeReserved().add(Calendar.MINUTE, minute);

                        chooseTimeTextView.setText(getString(R.string.choose_arrival_time, "\n" + DateFormat.format("HH:mm", reservation.getDateTimeReserved())));
                    },
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    Calendar.getInstance().get(Calendar.MINUTE),
                    true
            );

            if (DateFormat.format("dd/MM/yyyy", Date.from(Instant.now())).equals(DateFormat.format("dd/MM/yyyy", reservation.getDateTimeReserved()))
                    && Calendar.getInstance().after(
                            new GregorianCalendar(
                                    Calendar.getInstance().get(Calendar.YEAR),
                                    Calendar.getInstance().get(Calendar.MONTH),
                                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                                    10, 30
                            )
            )) {
                timePickerDialog.setMin(
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        Calendar.getInstance().get(Calendar.MINUTE)
                );
            } else {
                timePickerDialog.setMin(10, 30);
            }

            timePickerDialog.setMax(23, 0);

            timePickerDialog.updateTime(
                    reservation.getDateTimeReserved().get(Calendar.HOUR_OF_DAY),
                    reservation.getDateTimeReserved().get(Calendar.MINUTE)
            );
            timePickerDialog.show();
        });

        chooseDateTextView.setText(getString(R.string.choose_date, ""));
        chooseTimeTextView.setText(getString(R.string.choose_arrival_time, ""));

        tableOutsideSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> reservation.setOutside(isChecked));

        guestsNbr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty())
                    reservation.setNbCustomers(Integer.parseInt(s.toString()));
                else {
                    reservation.setNbCustomers(1);
                }
            }
        });

        extraInformation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                reservation.setAdditionalInfo(s.toString());
            }
        });

        reservationViewModel.getAvailableTables().observe(requireActivity(), tables -> {
            if (tables != null) {
                tablesLoadingBar.setVisibility(View.GONE);

                chooseTimeTextView.setEnabled(true);
                changeActivationState(true);

                if (tables.isEmpty()) {
                    errorView.setVisibility(View.VISIBLE);
                    bookButton.setEnabled(false);
                } else {
                    guestsNbr.setFilters(new InputFilter[]{
                            new InputFilterMinMax(
                                    1,
                                    tables.stream()
                                            .map(Table::getNbSeats)
                                            .reduce(Integer::max)
                                            .get()
                            )
                    });
                }
            }
        });

        reservationViewModel.getError().observe(requireActivity(), error -> errorView.setText(error.getErrorMessage()));

        reservationViewModel.getStatus().observe(requireActivity(), statusCode -> {
            if (statusCode != null) {
                if (statusCode == 201) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.booking_created), Toast.LENGTH_SHORT).show();
                    requireActivity().onBackPressed();
                } else {
                    errorView.setVisibility(View.VISIBLE);

                    errorView.setText(statusCode == 400 ? R.string.must_fill_all : R.string.error_500);
                }
            } else {
                errorView.setVisibility(View.GONE);
            }
        });

        cancelButton.setOnClickListener(v -> requireActivity().onBackPressed());
        bookButton.setOnClickListener(v -> {
            errorView.setVisibility(View.GONE);

            if (allRequiredInputsFilled()) {
                if (reservationViewModel
                        .getAvailableTables()
                        .getValue()
                        .stream()
                        .anyMatch(table -> table.getNbSeats() >= reservation.getNbCustomers() && table.getOutside() == reservation.getOutside())
                ) {
                    User user = new JWT(
                            requireActivity()
                                    .getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE)
                                    .getString("JSONWEBTOKEN", "")
                    ).getClaim("userData").asObject(User.class);

                    reservation.setPersonId(user.getId());
                    reservation.setEstablishmentId(reservationViewModel.getSelectedEstablishment().getId());
                    reservation.setTableId(
                            reservationViewModel
                                    .getAvailableTables()
                                    .getValue()
                                    .stream()
                                    .filter(table -> table.getNbSeats() >= reservation.getNbCustomers() && table.getOutside() == reservation.getOutside())
                                    .findFirst()
                                    .get().getId()
                    );

                    reservationViewModel.makeReservation(reservation);
                } else {
                    errorView.setText(R.string.no_table_available);
                    errorView.setVisibility(View.VISIBLE);
                }
            } else {
                errorView.setText(R.string.must_fill_all);
                errorView.setVisibility(View.VISIBLE);
            }
        });

        return root;
    }

    private void loadAvailableTables() {
        changeActivationState(false);

        reservationViewModel.requestTables(reservation.getDateTimeReserved());
        tablesLoadingBar.setVisibility(View.VISIBLE);
    }

    private void changeActivationState(boolean state) {
        chooseDateTextView.setActivated(state);
        chooseTimeTextView.setActivated(state);
        tableOutsideSwitch.setActivated(state);
        guestsNbr.setActivated(state);
        extraInformation.setActivated(state);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(reservationViewModel.getSelectedEstablishment().getName());
    }

    private boolean allRequiredInputsFilled() {
        return reservation.getDateTimeReserved() != null && reservation.getDateTimeReserved().after(new GregorianCalendar()) && !guestsNbr.getText().toString().isEmpty();
    }

    private static class InputFilterMinMax implements InputFilter {
        private int min;
        private int max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException ignored) { }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}