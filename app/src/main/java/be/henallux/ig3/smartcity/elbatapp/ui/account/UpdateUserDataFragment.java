package be.henallux.ig3.smartcity.elbatapp.ui.account;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.Calendar;

import be.henallux.ig3.smartcity.elbatapp.R;

public class UpdateUserDataFragment extends Fragment {
    private AccountViewModel accountViewModel;
    private EditText lastName, firstName, phone;
    private EditText street, streetNumber, postalCode, locality, country;
    private Button birthDate;
    private DatePickerDialog birthDatePicker;
    private RadioGroup gender;
    private RadioButton genderSelected;
    private Button cancel, confirm;
    private TextView error;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_update_user_data, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.update_user_data));

        lastName = root.findViewById(R.id.name_update);
        firstName = root.findViewById(R.id.first_name_update);
        phone = root.findViewById(R.id.phone_number_update);
        birthDate = root.findViewById(R.id.birth_date_update);
        street = root.findViewById(R.id.street_update);
        streetNumber = root.findViewById(R.id.street_number_update);
        postalCode = root.findViewById(R.id.postal_code_update);
        locality = root.findViewById(R.id.locality_update);
        country = root.findViewById(R.id.country_update);
        cancel = root.findViewById(R.id.cancel_update_button);
        confirm = root.findViewById(R.id.confirm_update_button);
        error = root.findViewById(R.id.error_update);

        gender = root.findViewById(R.id.gender_group_update);
        genderSelected = gender.findViewById(gender.getCheckedRadioButtonId());
        gender.setOnCheckedChangeListener((group, checkedId) -> genderSelected = group.findViewById(checkedId));

        birthDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            birthDatePicker = new DatePickerDialog(getContext(),
                    (view, year1, month1, dayOfMonth) -> birthDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1), year, month, day);

            // TODO mettre à jour la date avec celle du user
            birthDatePicker.show();
        });


        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);

        accountViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            lastName.setText(user.getLastName());
            firstName.setText(user.getFirstName());


            // TODO setText avec les infos de user
            //  pas sûre que ça va fonctionner ....
        });


    }
}