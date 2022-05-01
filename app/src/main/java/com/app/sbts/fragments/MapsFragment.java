package com.app.sbts.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.app.sbts.R;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.FragmentMapsBinding;
import com.app.sbts.models.DirectionsJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class MapsFragment extends Fragment implements OnMapReadyCallback {


    GoogleMap gMap;
    MarkerOptions att;
    Boolean once = true;
    String Bus_No;
    String req_url;
    Boolean parent = false;
    String responseString;

    private final Handler handler = new Handler();
    String[] str;
    SharedPreferences sharedPreferences;
    Marker marker;

    LatLng newPos, oldPos;

    FragmentMapsBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences = requireActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        oldPos = new LatLng(0, 0);

    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        requireActivity().getMenuInflater().inflate(R.menu.maptype_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
            case R.id.hybrid: {
                gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            }
            case R.id.normal: {
                gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            }
            case R.id.terrain: {
                gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            }
            case R.id.satellite: {
                gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            }
            case R.id.bus: {
                if(marker != null) {
                    LatLng pos = marker.getPosition();
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
                }
                return true;
            }
        }

        if (sharedPreferences.getString("ROLE", "default").equals("Parent")){
            NavController navController = Navigation.findNavController(requireActivity(), R.id.parent_frame);
            return NavigationUI.onNavDestinationSelected(item, navController)
                    || super.onOptionsItemSelected(item);
        }else if (sharedPreferences.getString("ROLE", "default").equals("Attendee")){
            NavController navController = Navigation.findNavController(requireActivity(), R.id.attendee_frame);
            return NavigationUI.onNavDestinationSelected(item, navController)
                    || super.onOptionsItemSelected(item);
        }else if (sharedPreferences.getString("ROLE", "default").equals("Driver")){
            NavController navController = Navigation.findNavController(requireActivity(), R.id.driver_frame);
            return NavigationUI.onNavDestinationSelected(item, navController)
                    || super.onOptionsItemSelected(item);
        }else {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.parent_frame);
            return NavigationUI.onNavDestinationSelected(item, navController)
                    || super.onOptionsItemSelected(item);
        }

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        attLoc.run();
    }

    private final Runnable attLoc = new Runnable() {
        @Override
        public void run() {

            Bus_No = sharedPreferences.getString("Bus_No", null);
            if(Bus_No == null) {
             return;
            }
            if (Bus_No.isEmpty()) {
                return;
            }
            String url = getString(R.string.Location_In_URL);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(gMap == null){
                                return;
                            }
                            gMap.clear();
                            str = Pattern.compile(",").split(response);
                            if (str.length < 2){
                                return;
                            }
                            newPos = new LatLng(Double.parseDouble(str[0]), Double.parseDouble(str[1]));

                            if (once) {
                                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(str[0]), Double.parseDouble(str[1])), 17));
                                once = false;
                                if (sharedPreferences.getString("ROLE", "Attendee").equals("Parent")) {
                                    gMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(sharedPreferences.getString("Latitude", null)), Double.valueOf(sharedPreferences.getString("Longitude", null)))).title("Pick-up spot"));
                                    req_url = getRequestURL(new LatLng(Double.parseDouble(str[0]), Double.parseDouble(str[1]))
                                            , new LatLng(Double.parseDouble(sharedPreferences.getString("Latitude", null)), Double.parseDouble(sharedPreferences.getString("Longitude", null))));
                                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                                    taskRequestDirections.execute(req_url);
                                }
                            }
                            if (!oldPos.equals(newPos)) {
                                att = new MarkerOptions().position(oldPos).title( sharedPreferences.getString("Bus_No","Bus"));
                                att.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_station_location__48));
                                marker = gMap.addMarker(att);
                                animateMarker(newPos, oldPos, marker, gMap);
                                oldPos = newPos;
                            }else{
                                att = new MarkerOptions().position(newPos).title( sharedPreferences.getString("Bus_No","Bus"));
                                att.icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_station_location__48));
                                marker = gMap.addMarker(att);
                            }
//                            if(sharedPreferences.getString("ROLE","Attendee").equals("Parent")){
//                                if(responseString != null){
//                                    TaskParser taskParser = new TaskParser();
//                                    taskParser.execute(responseString);
//                                }
//                            }

                        }
                    }, ignored -> {}) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("Bus_no", Bus_No);
                    return params;
                }
            };
            SingletonClass.getInstance(requireActivity().getApplicationContext()).addToRequestQueue(stringRequest);

            handler.postDelayed(this, 1000);

        }
    };


    private interface LatLngInterpolator {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolator {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }

    public static void animateMarker(final LatLng destination, final LatLng source, final Marker m, final GoogleMap map) {

        final LatLngInterpolator latLngInterpolator = new LatLngInterpolator.LinearFixed();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(1000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            try {
                float v = animation.getAnimatedFraction();
                LatLng newPosition = latLngInterpolator.interpolate(v, source, destination);
                m.setPosition(newPosition);
            } catch (Exception ignored) {}
        });

        valueAnimator.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(attLoc);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setTrafficEnabled(false);
        gMap.setBuildingsEnabled(true);
        gMap.setIndoorEnabled(true);

        if (sharedPreferences.getString("ROLE", "Attendee").equals("Parent")) {
            if (ActivityCompat.checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            gMap.setMyLocationEnabled(true);
        }
        gMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            responseString = null;
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException ignored) {
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }

    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsJSONParser directionsJSONParser = new DirectionsJSONParser();
                routes = directionsJSONParser.parse(jsonObject);
            } catch (JSONException  ignored) {
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            ArrayList points = null;
            PolylineOptions polylineOptions = null;
            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(Objects.requireNonNull(point.get("lat")));
                    double lng = Double.parseDouble(Objects.requireNonNull(point.get("lng")));

                    points.add(new LatLng(lat, lng));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions != null) {
                gMap.addPolyline(polylineOptions);
            }
        }
    }

    private String getRequestURL(LatLng origin, LatLng destination) {
        String str_org = "origin=" + origin.latitude + "," + origin.longitude;
        String str_des = "destination=" + destination.latitude + "," + destination.longitude;
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String param = str_org + "&" + str_des + "&" + sensor + "&" + mode;
        String out = "json";
        String key = "AIzaSyAE7O6iGNhwSYT3KeeBRsjC6OyEERBq460";
        String url = "https://maps.googleapis.com/maps/api/directions/" + out + "?" + param + "&key=" + key;
        return url;
    }

    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuffer = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStream.close();
        } catch (IOException ignored) {
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            assert httpURLConnection != null;
            httpURLConnection.disconnect();
        }
        return responseString;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }










}