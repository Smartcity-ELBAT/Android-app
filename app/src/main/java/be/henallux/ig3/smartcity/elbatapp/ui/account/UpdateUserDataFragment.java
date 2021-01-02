package be.henallux.ig3.smartcity.elbatapp.ui.account;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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
    private RadioButton woman, man, other;
    private Button cancel, confirm;
    private TextView error;

    @SuppressLint("SetTextI18n")
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
        woman = root.findViewById(R.id.woman_button_update);
        man = root.findViewById(R.id.man_button_update);
        other = root.findViewById(R.id.other_button_update);

        genderSelected = gender.findViewById(gender.getCheckedRadioButtonId());
        gender.setOnCheckedChangeListener((group, checkedId) -> genderSelected = group.findViewById(checkedId));

        birthDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            birthDatePicker = new DatePickerDialog(
                    getContext(),
                    (view, year1, month1, dayOfMonth) -> birthDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1), year, month, day);

            String [] date = birthDate.getText().toString().split("/");
            birthDatePicker.updateDate(Integer.parseInt(date[2]), Integer.parseInt(date[1]) - 1, Integer.parseInt(date[0]));
            birthDatePicker.show();
        });

        if(savedInstanceState != null) {
            lastName.setText(savedInstanceState.getString("lastName"));
            firstName.setText(savedInstanceState.getString("firstName"));
            gender.check(savedInstanceState.getInt("gender"));
            birthDate.setText(savedInstanceState.getString("birthDate"));
            phone.setText(savedInstanceState.getString("phone"));
            street.setText(savedInstanceState.getString("street"));
            streetNumber.setText(savedInstanceState.getString("streetNumber"));
            locality.setText(savedInstanceState.getString("locality"));
            postalCode.setText(savedInstanceState.getString("postalCode"));
            country.setText(savedInstanceState.getString("country"));
        }

        confirm.setOnClickListener(v -> {
              checkData();

            if(confirm.isEnabled()){
                accountViewModel.updateUser(
                        lastName.getText().toString(),
                        firstName.getText().toString(),
                        birthDate.getText().toString(),
                        genderSelected.getText().toString(),
                        phone.getText().toString(),
                        street.getText().toString(),
                        streetNumber.getText().toString(),
                        locality.getText().toString(),
                        postalCode.getText().toString(),
                        country.getText().toString());
            }
        });

        cancel.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_updateUserDataFragment_to_nav_account2);
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!confirm.isEnabled())
                    checkData();
            }
        };

        lastName.addTextChangedListener(afterTextChangedListener);
        firstName.addTextChangedListener(afterTextChangedListener);
        birthDate.addTextChangedListener(afterTextChangedListener);
        phone.addTextChangedListener(afterTextChangedListener);
        street.addTextChangedListener(afterTextChangedListener);
        streetNumber.addTextChangedListener(afterTextChangedListener);
        locality.addTextChangedListener(afterTextChangedListener);
        postalCode.addTextChangedListener(afterTextChangedListener);
        country.addTextChangedListener(afterTextChangedListener);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);

        accountViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            lastName.setText(user.getLastName());
            firstName.setText(user.getFirstName());
            phone.setText(user.getPhoneNumber());
            street.setText(user.getAddress().getStreet());
            streetNumber.setText(user.getAddress().getNumber());
            postalCode.setText(user.getAddress().getPostalCode());
            locality.setText(user.getAddress().getCity());
            country.setText(user.getAddress().getCountry());
            birthDate.setText(user.getBirthDate());
            gender.check(user.getGender() == 'F' ? woman.getId() : user.getGender() == 'M' ? man.getId() : other.getId());
        });

        accountViewModel.getInputErrors().observe(getViewLifecycleOwner(), inputErrors -> {
            if(!inputErrors.isEmpty()) {
                lastName.setError(inputErrors.containsKey("lastName") ? inputErrors.get("lastName") : null);
                firstName.setError(inputErrors.containsKey("firstName") ? inputErrors.get("firstName") : null);
                birthDate.setError(inputErrors.containsKey("birthDate") ? inputErrors.get("birthDate") : null);
                birthDate.setError(inputErrors.containsKey("birthDateAge") ? inputErrors.get("birthDateAge") : null);
                phone.setError(inputErrors.containsKey("phone") ? inputErrors.get("phone") : null);
                street.setError(inputErrors.containsKey("street") ? inputErrors.get("street") : null);
                streetNumber.setError(inputErrors.containsKey("streetNumber") ? inputErrors.get("streetNumber") : null);
                locality.setError(inputErrors.containsKey("city") ? inputErrors.get("city") : null);
                postalCode.setError(inputErrors.containsKey("postalCode") ? inputErrors.get("postalCode") : null);
                country.setError(inputErrors.containsKey("country") ? inputErrors.get("country") : null);
            }
            confirm.setEnabled(inputErrors.isEmpty());
        });

        accountViewModel.getError().observe(getViewLifecycleOwner(), networkError -> {
            error.setText(networkError.getErrorMessage());
        });

        accountViewModel.getStatutCode().observe(getViewLifecycleOwner(), integer -> {
            if(integer == 400)
                error.setText(R.string.error_400_update_user_data);
            else if(integer == 401)
                error.setText(R.string.error_401_unauthorized);
            else if(integer == 403)
                error.setText(R.string.error_403_forbidden);
            else if(integer == 404)
                error.setText(R.string.error_404_update_user_data);
            else if(integer == 500)
                error.setText(R.string.error_500);
            else if (integer == 204){
                Toast.makeText(getActivity(), getResources().getString(R.string.data_updated), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigate(R.id.action_updateUserDataFragment_to_loginFragment2);
            }
        });
    }

    private void checkData(){
        accountViewModel.userDataChanged(
                lastName.getText().toString(),
                firstName.getText().toString(),
                birthDate.getText().toString(),
                phone.getText().toString(),
                street.getText().toString(),
                streetNumber.getText().toString(),
                locality.getText().toString(),
                postalCode.getText().toString(),
                country.getText().toString()
        );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("lastName", lastName.getText().toString());
        outState.putString("firstName", firstName.getText().toString());
        outState.putInt("gender", gender.getCheckedRadioButtonId());
        outState.putString("birthDate", birthDate.getText().toString());
        outState.putString("phone", phone.getText().toString());
        outState.putString("street", street.getText().toString());
        outState.putString("streetNumber", streetNumber.getText().toString());
        outState.putString("locality", locality.getText().toString());
        outState.putString("postalCode", postalCode.getText().toString());
        outState.putString("country", country.getText().toString());

        super.onSaveInstanceState(outState);
    }
}