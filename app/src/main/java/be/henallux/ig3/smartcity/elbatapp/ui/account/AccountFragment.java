package be.henallux.ig3.smartcity.elbatapp.ui.account;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import be.henallux.ig3.smartcity.elbatapp.R;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private TextView name, error;
    private EditText email, username, birthDate, phone, address;
    private Button updatePasswordButton, updateUserDataButton;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.my_account));

        name = root.findViewById(R.id.name_account);
        email = root.findViewById(R.id.email_address_account);
        username = root.findViewById(R.id.username_account);
        birthDate = root.findViewById(R.id.birth_date_account);
        phone = root.findViewById(R.id.phone_number_account);
        address = root.findViewById(R.id.address_account);
        updatePasswordButton = root.findViewById(R.id.update_password_button);
        updateUserDataButton = root.findViewById(R.id.update_user_data_button);
        error = root.findViewById(R.id.error_account);
        error.setVisibility(View.INVISIBLE);
        error.setText(null);

        name.setEnabled(false);
        email.setEnabled(false);
        username.setEnabled(false);
        birthDate.setEnabled(false);
        phone.setEnabled(false);
        address.setEnabled(false);

        updatePasswordButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_account_to_updatePasswordFragment));

        updateUserDataButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_nav_account_to_updateUserDataFragment));

        return root;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);

        accountViewModel.loadUser();

        accountViewModel.getError().observe(getViewLifecycleOwner(), networkError -> {
            if(networkError != null){
                error.setVisibility(View.VISIBLE);
                error.setText(networkError.getErrorMessage());
            }
        });

        accountViewModel.getStatutCode().observe(getViewLifecycleOwner(), integer -> {
            error.setVisibility(View.VISIBLE);

            if(integer == 400)
                error.setText(R.string.error_400_load_user);
            else if (integer == 404)
                error.setText(R.string.error_404_load_user);
            else if(integer == 500)
                error.setText(R.string.error_500);
        });

        accountViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            name.setText(user.getFirstName() + " " + user.getLastName());
            email.setText(user.getEmail());
            username.setText(user.getUsername());
            birthDate.setText(user.getBirthDate());
            phone.setText(user.getPhoneNumber());
            address.setText(user.getAddress().fullAddress());
        });
    }
}