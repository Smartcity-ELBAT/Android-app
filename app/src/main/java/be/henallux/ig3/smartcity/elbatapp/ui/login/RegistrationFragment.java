package be.henallux.ig3.smartcity.elbatapp.ui.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import be.henallux.ig3.smartcity.elbatapp.R;

public class RegistrationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_registration, container, false);

        final TextView usernameField;
        final Button cancelButton = root.findViewById(R.id.cancel_button);

        cancelButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.loginFragment));
        return root;
    }
}