package be.henallux.ig3.smartcity.elbatapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.auth0.android.jwt.JWT;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import be.henallux.ig3.smartcity.elbatapp.R;
import be.henallux.ig3.smartcity.elbatapp.data.model.User;
import be.henallux.ig3.smartcity.elbatapp.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_booking, R.id.nav_account, R.id.nav_bookings, R.id.nav_corona)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = navigationView.getHeaderView(0);
        TextView greetingsView = headerView.findViewById(R.id.greetings_view);
        JWT token = new JWT(getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE).getString("JSONWEBTOKEN", ""));

        greetingsView.setText(getString(R.string.nav_greeting, Objects.requireNonNull(token.getClaim("userData").asObject(User.class)).getFirstName()));

        navigationView.setNavigationItemSelectedListener(item -> {
            if (String.valueOf(item.getTitle()).equals(getString(R.string.menu_map))
                    || String.valueOf(item.getTitle()).equals(getString(R.string.menu_my_account))
                    || String.valueOf(item.getTitle()).equals(getString(R.string.menu_my_bookings))
                    || String.valueOf(item.getTitle()).equals(getString(R.string.menu_covid_positive))) {
                NavigationUI.onNavDestinationSelected(item, navController);
                drawer.close();
            } else if (String.valueOf(item.getTitle()).equals(getString(R.string.menu_exit))) {
                SharedPreferences.Editor sharedPref = getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE).edit();

                sharedPref.remove("JSONWEBTOKEN");
                sharedPref.apply();

                startActivity(new Intent(this, LoginActivity.class));
            }

            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}