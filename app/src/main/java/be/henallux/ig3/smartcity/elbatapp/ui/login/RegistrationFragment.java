package be.henallux.ig3.smartcity.elbatapp.ui.login;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.Calendar;

import be.henallux.ig3.smartcity.elbatapp.R;


public class RegistrationFragment extends Fragment {
    private EditText email, username, password, confirmPassword, lastName, firstName, phone;
    private EditText street, streetNumber, postalCode, locality, country;
    private Button birthDate;
    private DatePickerDialog birthDatePicker;
    private RadioGroup gender;
    private RadioButton genderSelected;
    private Button cancelButton, confirmButton;
    private RegistrationViewModel registrationViewModel;
    private TextView error;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registration, container, false);
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);

        email = root.findViewById(R.id.email_address_prompt);
        username = root.findViewById(R.id.username_prompt);
        password = root.findViewById(R.id.password_prompt);
        confirmPassword = root.findViewById(R.id.confirm_password_prompt);
        lastName = root.findViewById(R.id.name_prompt);
        firstName = root.findViewById(R.id.first_name_prompt);
        birthDate = root.findViewById(R.id.birth_date_prompt);
        phone = root.findViewById(R.id.phone_number_prompt);
        street = root.findViewById(R.id.street_prompt);
        streetNumber = root.findViewById(R.id.street_number_prompt);
        postalCode = root.findViewById(R.id.postal_code_prompt);
        locality = root.findViewById(R.id.locality_prompt);
        country = root.findViewById(R.id.country_prompt);
        cancelButton = root.findViewById(R.id.cancel_button);
        confirmButton = root.findViewById(R.id.confirm_button);
        error = root.findViewById(R.id.error);

        gender = root.findViewById(R.id.gender_group);
        genderSelected = gender.findViewById(gender.getCheckedRadioButtonId());
        gender.setOnCheckedChangeListener((group, checkedId) -> genderSelected = group.findViewById(checkedId));

        birthDate.setInputType(InputType.TYPE_NULL);
        birthDate.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);

            birthDatePicker = new DatePickerDialog(getContext(),
                    (view, year1, month1, dayOfMonth) -> birthDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1), year, month, day);

            birthDatePicker.updateDate(year - 18, month, day);
            birthDatePicker.show();
        });

        if(savedInstanceState != null) {
            username.setText(savedInstanceState.getString("username"));
            password.setText(savedInstanceState.getString("password"));
            confirmPassword.setText(savedInstanceState.getString("passwordConfirm"));
            lastName.setText(savedInstanceState.getString("lastName"));
            firstName.setText(savedInstanceState.getString("firstName"));
            gender.check(savedInstanceState.getInt("gender"));
            birthDate.setText(savedInstanceState.getString("birthDate"));
            email.setText(savedInstanceState.getString("email"));
            phone.setText(savedInstanceState.getString("phone"));
            street.setText(savedInstanceState.getString("street"));
            streetNumber.setText(savedInstanceState.getString("streetNumber"));
            locality.setText(savedInstanceState.getString("locality"));
            postalCode.setText(savedInstanceState.getString("postalCode"));
            country.setText(savedInstanceState.getString("country"));
        }

        confirmButton.setOnClickListener(v -> {
            checkData();

            if(confirmButton.isEnabled()){
                registrationViewModel.addUser(
                        username.getText().toString(),
                        password.getText().toString(),
                        lastName.getText().toString(),
                        firstName.getText().toString(),
                        birthDate.getText().toString(),
                        genderSelected.getText().toString(),
                        email.getText().toString(),
                        phone.getText().toString(),
                        street.getText().toString(),
                        streetNumber.getText().toString(),
                        locality.getText().toString(),
                        postalCode.getText().toString(),
                        country.getText().toString());
            }
        });

        cancelButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.loginFragment));

        registrationViewModel.getInputErrors().observe(getViewLifecycleOwner(), inputErrors -> {
            if(!inputErrors.isEmpty()) {
                username.setError(inputErrors.containsKey("username") ? inputErrors.get("username") : null);
                password.setError(inputErrors.containsKey("password") ? inputErrors.get("password") : null);
                confirmPassword.setError(inputErrors.containsKey("passwordConfirm") ? inputErrors.get("passwordConfirm") : null);
                lastName.setError(inputErrors.containsKey("lastName") ? inputErrors.get("lastName") : null);
                firstName.setError(inputErrors.containsKey("firstName") ? inputErrors.get("firstName") : null);
                birthDate.setError(inputErrors.containsKey("birthDate") ? inputErrors.get("birthDate") : null);
                email.setError(inputErrors.containsKey("email") ? inputErrors.get("email") : null);
                phone.setError(inputErrors.containsKey("phone") ? inputErrors.get("phone") : null);
                street.setError(inputErrors.containsKey("street") ? inputErrors.get("street") : null);
                streetNumber.setError(inputErrors.containsKey("streetNumber") ? inputErrors.get("streetNumber") : null);
                locality.setError(inputErrors.containsKey("city") ? inputErrors.get("city") : null);
                postalCode.setError(inputErrors.containsKey("postalCode") ? inputErrors.get("postalCode") : null);
                country.setError(inputErrors.containsKey("country") ? inputErrors.get("country") : null);
            }
            confirmButton.setEnabled(inputErrors.isEmpty());
        });

        registrationViewModel.getError().observe(getViewLifecycleOwner(), networkError -> {
                error.setText(networkError.getErrorMessage());
        });

        registrationViewModel.getStatutCode().observe(getViewLifecycleOwner(), integer -> {

            if(integer == 400)
                error.setText(R.string.error_400_add_customer);
            else if(integer == 500)
                error.setText(R.string.error_500);
            else if (integer == 201){
                Toast.makeText(getActivity(), getResources().getString(R.string.user_created), Toast.LENGTH_SHORT).show();
                Navigation.findNavController(requireView()).navigate(R.id.loginFragment);
            }
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
                if(!confirmButton.isEnabled())
                    checkData();
            }
        };

        username.addTextChangedListener(afterTextChangedListener);
        password.addTextChangedListener(afterTextChangedListener);
        confirmPassword.addTextChangedListener(afterTextChangedListener);
        lastName.addTextChangedListener(afterTextChangedListener);
        firstName.addTextChangedListener(afterTextChangedListener);
        birthDate.addTextChangedListener(afterTextChangedListener);
        email.addTextChangedListener(afterTextChangedListener);
        phone.addTextChangedListener(afterTextChangedListener);
        street.addTextChangedListener(afterTextChangedListener);
        streetNumber.addTextChangedListener(afterTextChangedListener);
        locality.addTextChangedListener(afterTextChangedListener);
        postalCode.addTextChangedListener(afterTextChangedListener);
        country.addTextChangedListener(afterTextChangedListener);

        return root;
    }

    private void checkData(){
        registrationViewModel.registerDataChanged(
                username.getText().toString(),
                password.getText().toString(),
                confirmPassword.getText().toString(),
                lastName.getText().toString(),
                firstName.getText().toString(),
                birthDate.getText().toString(),
                email.getText().toString(),
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
        outState.putString("username", username.getText().toString());
        outState.putString("password", password.getText().toString());
        outState.putString("passwordConfirm", confirmPassword.getText().toString());
        outState.putString("lastName", lastName.getText().toString());
        outState.putString("firstName", firstName.getText().toString());
        outState.putInt("gender", gender.getCheckedRadioButtonId());
        outState.putString("birthDate", birthDate.getText().toString());
        outState.putString("email", email.getText().toString());
        outState.putString("phone", phone.getText().toString());
        outState.putString("street", street.getText().toString());
        outState.putString("streetNumber", streetNumber.getText().toString());
        outState.putString("locality", locality.getText().toString());
        outState.putString("postalCode", postalCode.getText().toString());
        outState.putString("country", country.getText().toString());

        super.onSaveInstanceState(outState);
    }
}