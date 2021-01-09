package be.henallux.ig3.smartcity.elbatapp.ui.tableBooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.widget.TextView;

import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import be.henallux.ig3.smartcity.elbatapp.R;
import be.henallux.ig3.smartcity.elbatapp.utils.RangeTimePickerDialog;

public class ReservationFragment extends Fragment {

    private ReservationViewModel mViewModel;

    private Integer day;
    private Integer month;
    private Integer year;
    private Integer hour;
    private Integer minutes;

    private String dateAsString;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reservation, container, false);

        final TextView chooseDateTextView = root.findViewById(R.id.choose_date);
        final TextView chooseTimeTextView = root.findViewById(R.id.choose_hour);

        hour = 0;
        minutes = 0;

        chooseDateTextView.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            final int calendarYear = year != null ? year : calendar.get(Calendar.YEAR);
            final int calendarMonth = month != null ? month : calendar.get(Calendar.MONTH);
            final int calendarDay = day != null ? day : calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireActivity(),
                    R.style.DateTimePickerTheme,
                    (view, year, month, dayOfMonth) -> {

                        day = dayOfMonth;
                        this.month = month;
                        this.year = year;

                        calendar.set(year, month, dayOfMonth, 0, 0);
                        chooseTimeTextView.setEnabled(true);

                        dateAsString = (String) DateFormat.format("dd/MM/yyyy", calendar);

                        chooseDateTextView.setText(getString(R.string.choose_date, "\n" + dateAsString));
                    }, calendarYear, calendarMonth, calendarDay
            );

            datePickerDialog.getDatePicker().setMinDate(Date.from(Instant.now()).getTime());

            datePickerDialog.updateDate(calendarYear, calendarMonth, calendarDay);
            datePickerDialog.show();
        });

        chooseTimeTextView.setOnClickListener(v -> {
            RangeTimePickerDialog timePickerDialog = new RangeTimePickerDialog(
                    requireActivity(),
                    R.style.DateTimePickerTheme,
                    (view, hourOfDay, minute) -> {
                        Calendar calendar = Calendar.getInstance();

                        minutes = minute;
                        hour = hourOfDay;

                        calendar.set(0, 0, 0, hourOfDay, minute);

                        chooseTimeTextView.setText(getString(R.string.choose_arrival_time, "\n" + DateFormat.format("HH:mm", calendar)));
                    },
                    Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                    Calendar.getInstance().get(Calendar.MINUTE),
                    true
            );

            if (DateFormat.format("dd/MM/yyyy", Date.from(Instant.now())).equals(dateAsString)) {
                timePickerDialog.setMin(
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        Calendar.getInstance().get(Calendar.MINUTE)
                );
            } else {
                timePickerDialog.setMin(-1, -1);
            }

            timePickerDialog.setMax(23, 0);

            timePickerDialog.updateTime(hour, minutes);
            timePickerDialog.show();
        });

        chooseDateTextView.setText(getString(R.string.choose_date, ""));
        chooseTimeTextView.setText(getString(R.string.choose_arrival_time, ""));

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(ReservationViewModel.class);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setTitle(mViewModel.getSelectedEstablishment().getName());


    }
}