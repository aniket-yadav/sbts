package com.app.sbts.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.sbts.R;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.FragmentBusLocationsBinding;
import com.app.sbts.models.BusLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Bus_Locations extends Fragment implements OnMapReadyCallback {

    GoogleMap gMap;
    FragmentBusLocationsBinding binding;
    private JsonArrayRequest request;

    private List<BusLocation> busLocationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding =  FragmentBusLocationsBinding.inflate(inflater, container, false);
        busLocationList = new ArrayList<>();

        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.bus_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        loadLocations();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

   void loadLocations() {


            String url = getString(R.string.bus_locations_url);
            request = new JsonArrayRequest(url, response -> {
                busLocationList.clear();
                JSONObject jsonObject;

                for (int i = response.length() - 1; i >= 0; i--) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        BusLocation busLocation = new BusLocation();
                        busLocation.setBus_No(jsonObject.getString("Bus_No"));
                        busLocation.setLatitude(jsonObject.getDouble("Latitude"));
                        busLocation.setLongitude(jsonObject.getDouble("Longitude"));

                        busLocationList.add(busLocation);
                    } catch ( JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(busLocationList.size() > 0) {
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.2183, 72.9781), 0));
                    for(int i =0; i < busLocationList.size(); i++){

                        gMap.addMarker(new MarkerOptions().position(new LatLng(busLocationList.get(i).getLatitude(), busLocationList.get(i).getLatitude())).title(busLocationList.get(i).getBus_No()));

                    }

                }

            }, error -> {
                busLocationList.clear();
                Toast.makeText(requireActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            });

            SingletonClass.getInstance(getContext()).addToRequestQueue(request);
        }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setTrafficEnabled(false);
        gMap.setBuildingsEnabled(true);
        gMap.setIndoorEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(true);
    }
}