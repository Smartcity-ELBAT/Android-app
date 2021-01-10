package be.henallux.ig3.smartcity.elbatapp.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import be.henallux.ig3.smartcity.elbatapp.R;
import be.henallux.ig3.smartcity.elbatapp.ui.login.LoginActivity;

public class UpdatePasswordFragment extends Fragment {
    private AccountViewModel accountViewModel;
    private EditText currentPassword, newPassword, confirmNewPassword;
    private Button cancel, confirm;
    private TextView error;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_update_password, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.update_password));

        currentPassword = root.findViewById(R.id.current_password);
        newPassword = root.findViewById(R.id.new_password);
        confirmNewPassword = root.findViewById(R.id.confirm_new_password);
        cancel = root.findViewById(R.id.cancel_update_password_button);
        confirm = root.findViewById(R.id.confirm_update_password_button);
        error = root.findViewById(R.id.error_update_password);
        error.setVisibility(View.INVISIBLE);
        error.setText(null);

        if(savedInstanceState != null){
            currentPassword.setText(savedInstanceState.getString("currentPassword"));
            newPassword.setText(savedInstanceState.getString("newPassword"));
            confirmNewPassword.setText(savedInstanceState.getString("confirmPassword"));
        }

        confirm.setOnClickListener(v -> {
            checkData();

            if(confirm.isEnabled()){
                accountViewModel.updatePassword(
                        currentPassword.getText().toString(),
                        newPassword.getText().toString()
                );
            }
        });

        cancel.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_updatePasswordFragment_to_nav_account));

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

        currentPassword.addTextChangedListener(afterTextChangedListener);
        newPassword.addTextChangedListener(afterTextChangedListener);
        confirmNewPassword.addTextChangedListener(afterTextChangedListener);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        accountViewModel = new ViewModelProvider(requireActivity()).get(AccountViewModel.class);

        accountViewModel.getInputErrors().observe(getViewLifecycleOwner(), inputErrors -> {
            if(!inputErrors.isEmpty()) {
                currentPassword.setError(inputErrors.containsKey("currentPassword") ? inputErrors.get("currentPassword") : null);
                newPassword.setError(inputErrors.containsKey("newPassword") ? inputErrors.get("newPassword") : null);
                confirmNewPassword.setError(inputErrors.containsKey("passwordConfirm") ? inputErrors.get("passwordConfirm") : null);
            }
            confirm.setEnabled(inputErrors.isEmpty());
        });

        accountViewModel.getError().observe(getViewLifecycleOwner(), networkError -> {
            if(networkError != null){
                error.setVisibility(View.VISIBLE);
                error.setText(networkError.getErrorMessage());
            }
        });

        accountViewModel.getStatutCode().observe(getViewLifecycleOwner(), integer -> {
            if(integer != 204)
                error.setVisibility(View.VISIBLE);

            if(integer == 400)
                error.setText(R.string.error_400_update_password);
            else if(integer == 401)
                error.setText(R.string.error_401_unauthorized);
            else if(integer == 403)
                error.setText(R.string.error_403_forbidden);
            else if(integer == 404)
                error.setText(R.string.error_404_update_password);
            else if(integer == 500)
                error.setText(R.string.error_500);
            else if (integer == 204){
                Toast.makeText(getActivity(), getResources().getString(R.string.password_updated), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(requireActivity(), LoginActivity.class));
                requireActivity().finish();
            }
        });
    }

    private void checkData(){
        accountViewModel.passwordDataChanged(
                currentPassword.getText().toString(),
                newPassword.getText().toString(),
                confirmNewPassword.getText().toString()
        );
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("currentPassword", currentPassword.getText().toString());
        outState.putString("newPassword", newPassword.getText().toString());
        outState.putString("confirmPassword", confirmNewPassword.getText().toString());
        super.onSaveInstanceState(outState);
    }
}