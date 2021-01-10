package be.henallux.ig3.smartcity.elbatapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;

import java.util.Date;

import be.henallux.ig3.smartcity.elbatapp.R;
import be.henallux.ig3.smartcity.elbatapp.data.model.User;
import be.henallux.ig3.smartcity.elbatapp.ui.MainActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String maybeJWTString = this.getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE).getString("JSONWEBTOKEN", "");

        if (!maybeJWTString.isEmpty()) {
            try {
                JWT jwt = new JWT(maybeJWTString);

                if (jwt.getExpiresAt().after(new Date())) {
                    Toast.makeText(
                            this,
                            getString(R.string.welcome) + jwt.getClaim("userData").asObject(User.class).getFirstName(),
                            Toast.LENGTH_LONG
                    ).show();

                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            } catch (DecodeException ignored) {}
        }

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
    }
}