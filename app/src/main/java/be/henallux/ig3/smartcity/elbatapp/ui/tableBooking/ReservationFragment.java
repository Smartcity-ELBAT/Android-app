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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Objects;

import be.henallux.ig3.smartcity.elbatapp.R;

public class ReservationFragment extends Fragment {

    private ReservationViewModel mViewModel;

    private int day;
    private int month;
    private int year;
    private int hour;
    private int minutes;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reservation, container, false);

        final TextView chooseDateTextView = root.findViewById(R.id.choose_date);
        final TextView chooseTimeTextView = root.findViewById(R.id.choose_hour);

        chooseDateTextView.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();

            final int calendarYear = calendar.get(Calendar.YEAR);
            final int calendarMonth = calendar.get(Calendar.MONTH);
            final int calendarDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireActivity(),
                    (view, year, month, dayOfMonth) -> {

                        day = dayOfMonth;
                        this.month = month;
                        this.year = year;

                        calendar.set(year, month, dayOfMonth, 0, 0);

                        chooseDateTextView.setText(getString(R.string.choose_date, "\n" + DateFormat.format("dd/MM/yyyy", calendar)));
                    }, calendarYear, calendarMonth, calendarDay
            );

            datePickerDialog.updateDate(calendarYear, calendarMonth, calendarDay);
            datePickerDialog.show();
        });

        chooseTimeTextView.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    requireActivity(),
                    (view, hourOfDay, minute) -> {
                        Calendar calendar = Calendar.getInstance();

                        minutes = minute;
                        hour = hourOfDay;

                        calendar.set(0, 0, 0, hourOfDay, minute);

                        chooseTimeTextView.setText(getString(R.string.choose_arrival_time, "\n" + DateFormat.format("HH:mm", calendar)));
                    }, 12, 0, true
            );

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