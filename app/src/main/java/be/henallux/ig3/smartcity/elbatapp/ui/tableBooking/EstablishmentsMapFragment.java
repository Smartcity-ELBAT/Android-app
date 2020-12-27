package be.henallux.ig3.smartcity.elbatapp.ui.tableBooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import be.henallux.ig3.smartcity.elbatapp.R;
import be.henallux.ig3.smartcity.elbatapp.data.model.Establishment;

public class EstablishmentsMapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {
    private HashMap<MarkerOptions, Establishment> establishmentsMarkers;

    private EstablishmentsMapViewModel establishmentsMapViewModel;
    private GoogleMap googleMap;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            EstablishmentsMapFragment.this.googleMap = googleMap;

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.4669, 4.86746), 14));
            googleMap.setMinZoomPreference(14);
            googleMap.setOnInfoWindowClickListener(EstablishmentsMapFragment.this);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_establishments_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        establishmentsMapViewModel = new ViewModelProvider(this).get(EstablishmentsMapViewModel.class);
        establishmentsMapViewModel.requestEstablishments();

        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        establishmentsMapViewModel.getEstablishments().observe(requireActivity(), establishments -> {
            Geocoder geocoder = new Geocoder(requireActivity(), Locale.FRANCE);

            establishmentsMarkers = new HashMap<>();

            for (Establishment establishment : establishments) {
                try {
                    Address address = geocoder.getFromLocationName(establishment.getAddress().fullAddress(),1).get(0);
                    LatLng addressLatLng = new LatLng(address.getLatitude(), address.getLongitude());
                    MarkerOptions options = new MarkerOptions().position(addressLatLng).title(establishment.getName());

                    establishmentsMarkers.put(options, establishment);
                    googleMap.addMarker(options);
                } catch (IOException ignored) {}
            }

        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        // Accéder à la page de réservation en fonction du Marker
    }
}