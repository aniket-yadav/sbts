package com.app.sbts.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.sbts.R;
import com.app.sbts.classes.SessionManager;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.ActivityAttendeeBinding;
import com.app.sbts.databinding.NavHeaderMainBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class AttendeeActivity extends AppCompatActivity {

    private ActivityAttendeeBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    SessionManager sessionManager;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private  int LOCATION_PERMISSION_CODE = 1;
    private String User;
    StringRequest stringRequest;
    String[] str;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    private  NavHeaderMainBinding headerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMainAttendee.toolbar);

        sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        User = user.get(SessionManager.USERNAME);

        View headerView = binding.navView.getHeaderView(0);
        headerBinding = NavHeaderMainBinding.bind(headerView);

        headerBinding.userName.setText(sharedPreferences.getString("Full_Name", null));
//        byte[] image_bit =  Base64.decode(sharedPreferences.getString("Photo", "null"),Base64.DEFAULT);
//        headerBinding.imageView.setImageBitmap(BitmapFactory.decodeByteArray(image_bit, 0, image_bit.length));
        Glide
                .with(this)
                .load(sharedPreferences.getString("Photo", "null"))
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(headerBinding.imageView);
        headerBinding.userEmail.setText(sharedPreferences.getString("Email", null));
        headerBinding.imageView.setOnClickListener(imageOnclickListener);


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_profile, R.id.nav_map, R.id.nav_password_change,R.id.nav_student_list)
                .setOpenableLayout(drawer)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.attendee_frame);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            sessionManager.logout();
            return true;
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create()
                .setInterval(4000)
                .setFastestInterval(3000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(3000);
        buildLocationCallBack();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        getData();

    }

    View.OnClickListener imageOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }


    private void getData() {


        stringRequest = new StringRequest(Request.Method.POST, getString(R.string.Attendee_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        editor = sharedPreferences.edit();
                        str = Pattern.compile(",").split(response);
                        editor.putString("Full_Name", str[0]);
                        editor.putString("Photo", str[1]);
                        editor.putString("Email", str[2]);
                        editor.putString("Mobile_No1", str[3]);
                        editor.putString("Bus_No", str[4]);
                        editor.putString("DOB", str[5]);
                        editor.putString("Address", str[6]);
                        editor.apply();

//                        String Photo = sharedPreferences.getString("Photo", null);
//                        byte[] imagebit=  Base64.decode(Photo,Base64.DEFAULT);
//                      headerBinding.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imagebit, 0,imagebit.length));
                        Glide
                                .with(getApplicationContext())
                                .load(sharedPreferences.getString("Photo", "null"))
                                .centerCrop()
                                .placeholder(R.drawable.placeholder)
                                .into(headerBinding.imageView);
                     headerBinding.userName.setText(sharedPreferences.getString("Full_Name", null));
                        headerBinding.userEmail.setText(sharedPreferences.getString("Email", null));

                    }
                }, error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", User);
                return params;
            }
        };
        SingletonClass.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void buildLocationCallBack() {
        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(final Location location:locationResult.getLocations()){
                    String url = getString(R.string.Location_Out_URL);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (!response.trim().contains("success")) {
                                        Toast.makeText(getApplicationContext(), "Failed to capture location.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("username", User);
                            params.put("latitude", String.valueOf(location.getLatitude()));
                            params.put("longitude", String.valueOf(location.getLongitude()));
                            return params;
                        }
                    };
                    SingletonClass.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }
            }
        };
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.attendee_frame);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this).setTitle(getString(R.string.permission_needed)).setMessage(getString(R.string.permission_required_message)).setPositiveButton(
                    getString( R.string.allow), (dialog, which) -> ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE)
            ).setNegativeButton(getString(R.string.deny), (dialog, which) -> dialog.dismiss()).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,getString( R.string.permission_granted), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }
}