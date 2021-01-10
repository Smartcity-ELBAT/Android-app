package be.henallux.ig3.smartcity.elbatapp.ui.login;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import be.henallux.ig3.smartcity.elbatapp.R;
import be.henallux.ig3.smartcity.elbatapp.data.model.User;
import be.henallux.ig3.smartcity.elbatapp.ui.MainActivity;

import static androidx.core.content.ContextCompat.getSystemService;

public class LoginFragment extends Fragment {

    private static final String CHANNEL_ID = "channel_id_notification_covid_19";
    private final Integer notificationId = 1;
    private NotificationManagerCompat notificationManager;
    private TextView error;

    private LoginViewModel loginViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        final EditText usernameEditText = view.findViewById(R.id.username);
        final EditText passwordEditText = view.findViewById(R.id.password);
        final Button loginButton = view.findViewById(R.id.login_button);
        final ProgressBar loadingProgressBar = view.findViewById(R.id.loading);
//        final LinearLayout errorLayout = view.findViewById(R.id.error_layout);
        final Button registerButton = view.findViewById(R.id.register_button);
        error = view.findViewById(R.id.error_login);
        error.setVisibility(View.INVISIBLE);
        error.setText(null);

        createNotificationChannel();

        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), loginFormState -> {
            if (loginFormState == null) {
                return;
            }
            loginButton.setEnabled(loginFormState.isDataValid());
            if (loginFormState.getUsernameError() != null) {
                usernameEditText.setError(getString(loginFormState.getUsernameError()));
            }
            if (loginFormState.getPasswordError() != null) {
                passwordEditText.setError(getString(loginFormState.getPasswordError()));
            }
        });

        loginViewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                loadingProgressBar.setVisibility(View.GONE);
//                errorLayout.setVisibility(View.VISIBLE);

//                final ImageView errorDrawable = view.findViewById(R.id.error_image_view);
//                final TextView errorTextView = view.findViewById(R.id.error_label);
//
//                errorDrawable.setImageResource(error.getErrorDrawable());
//                errorTextView.setText(error.getErrorMessage());
            }
        });

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), loginResult -> {
            loadingProgressBar.setVisibility(View.GONE);
            requireActivity().setResult(Activity.RESULT_OK);

            loginViewModel.checkReservationsContactCovid();

            //Complete and destroy login activity once successful
            updateUiWithUser(loginResult);
            requireActivity().finish();
        });

        loginViewModel.getContactWithPersonAtRisk().observe(getViewLifecycleOwner(), positiveToCovid -> {
            if(positiveToCovid.getPositifToCovid()){
                notificationManager = NotificationManagerCompat.from(getContext());

                Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_menu_virus)
                        .setContentTitle(getResources().getString(R.string.notification_title))
                        .setContentText(getResources().getString(R.string.notification_text))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setCategory(NotificationCompat.CATEGORY_ALARM)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(getResources().getString(R.string.notification_text)))
                        .build();

                notificationManager.notify(notificationId, notification);
            }
        });

        loginViewModel.getError().observe(getViewLifecycleOwner(), networkError -> {
            if(networkError != null){
                error.setVisibility(View.VISIBLE);
                error.setText(networkError.getErrorMessage());
            }
        });

        loginViewModel.getStatutCode().observe(getViewLifecycleOwner(), integer -> {
            error.setVisibility(View.VISIBLE);

            if(integer == 400)
                error.setText(R.string.error_400_check_covid);
            else if(integer == 401)
                error.setText(R.string.error_401_unauthorized);
            else if(integer == 404)
                error.setText(R.string.error_404_check_covid);
            else if(integer == 500)
                error.setText(R.string.error_500);
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* ignore */ }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { /* ignore */ }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            loadingProgressBar.setVisibility(View.VISIBLE);
//            errorLayout.setVisibility(View.GONE);
            loginViewModel.login(usernameEditText.getText().toString(),
                    passwordEditText.getText().toString());
        });

        registerButton.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.registrationFragment));
    }

    private void updateUiWithUser(User user) {
        Toast.makeText(requireContext(), getString(R.string.welcome) + user.getFirstName(), Toast.LENGTH_LONG).show();

        SharedPreferences.Editor sharedPref = requireActivity().getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE).edit();

        sharedPref.putString("JSONWEBTOKEN", loginViewModel.getToken());
        sharedPref.apply();

        startActivity(new Intent(requireActivity(), MainActivity.class));
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(getContext(), NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}