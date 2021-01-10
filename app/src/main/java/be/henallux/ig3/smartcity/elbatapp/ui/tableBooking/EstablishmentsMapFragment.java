package be.henallux.ig3.smartcity.elbatapp.ui.tableBooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;

import be.henallux.ig3.smartcity.elbatapp.R;
import be.henallux.ig3.smartcity.elbatapp.data.model.Establishment;
import be.henallux.ig3.smartcity.elbatapp.data.model.NetworkError;

public class EstablishmentsMapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {
    private ArrayList<Establishment> establishments;

    private EstablishmentsMapViewModel establishmentsMapViewModel;
    private ReservationViewModel reservationViewModel;
    private GoogleMap googleMap;

    private ConstraintLayout mapLayout;
    private TextView errorView;
    private ProgressBar loadingBar;

    private OnMapReadyCallback callback = googleMap -> {
        EstablishmentsMapFragment.this.googleMap = googleMap;

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.4669, 4.86746), 12));
        googleMap.setMinZoomPreference(12);
        googleMap.setOnInfoWindowClickListener(EstablishmentsMapFragment.this);
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_establishments_map, container, false);

        reservationViewModel = new ViewModelProvider(requireActivity()).get(ReservationViewModel.class);
        establishmentsMapViewModel = new ViewModelProvider(this).get(EstablishmentsMapViewModel.class);

        loadingBar = root.findViewById(R.id.establishments_loading_bar);

        mapLayout = root.findViewById(R.id.map_layout);
        errorView = root.findViewById(R.id.loading_error_view);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        establishmentsMapViewModel.requestEstablishments();

        establishmentsMapViewModel.getEstablishments().observe(requireActivity(), establishments -> {
            // besoin d'une AsyncTask parce que le geocoding ne se fait pas dans un processus séparé
            new AsyncEstablishmentsLocationGetter(requireActivity()).execute(establishments.toArray(new Establishment[]{}));

            this.establishments = (ArrayList<Establishment>) establishments;
        });

        establishmentsMapViewModel.getError().observe(getViewLifecycleOwner(), this::displayErrorScreen);
    }

    private void displayErrorScreen(NetworkError error) {
        if (error == null) {
            errorView.setVisibility(View.GONE);
        } else {
            mapLayout.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
            loadingBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Establishment clickedEstablishment = establishments
                .stream()
                .filter(establishment -> marker
                        .getTitle()
                        .equals(establishment.getName() + " - " + establishment.getCategory()))
                .collect(Collectors.toCollection(ArrayList::new)).get(0);

        reservationViewModel.setEstablishment(clickedEstablishment);

        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
                .navigate(R.id.action_nav_booking_to_reservationFragment);
    }

    private class AsyncEstablishmentsLocationGetter extends AsyncTask<Establishment, Void, ArrayList<LatLng>> {
        private Geocoder geocoder;

        public AsyncEstablishmentsLocationGetter(Context context) {
            super();

            this.geocoder = new Geocoder(context, Locale.FRANCE);
        }

        @Override
        protected ArrayList<LatLng> doInBackground(Establishment... establishments) {
            ArrayList<LatLng> locations = new ArrayList<>();

            for (Establishment establishment : establishments) {
                try {
                    Address address = geocoder.getFromLocationName(establishment.getAddress().fullAddress(),1).get(0);
                    locations.add(new LatLng(address.getLatitude(), address.getLongitude()));
                } catch (IOException ignored) {}
            }

            return locations;
        }

        @Override
        protected void onPostExecute(ArrayList<LatLng> locations) {
            for (int i = 0; i < locations.size(); i++) {
                Establishment establishment = establishments.get(i);

                googleMap.addMarker(new MarkerOptions()
                        .position(locations.get(i))
                        .title(establishment.getName() + " - " + establishment.getCategory())
                        .snippet(establishment.getAddress().fullAddress()));
            }

            loadingBar.setVisibility(View.GONE);
            mapLayout.setVisibility(View.VISIBLE);
        }
    }
}