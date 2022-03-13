package com.app.sbts.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import com.app.sbts.databinding.ActivityDriverBinding;
import com.app.sbts.databinding.NavHeaderMainBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class DriverActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    SharedPreferences sharedPreferences;
    SessionManager sessionManager;
    SharedPreferences.Editor editor;
    StringRequest stringRequest;
    String[] str;
    final int PICK_CODE = 1;
    Bitmap bitmap;
    private  int READ_PERMISSION_CODE = 1;
    private NavHeaderMainBinding headerBinding;
    ActivityDriverBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMainDriver.toolbar);

        sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        sessionManager = new SessionManager(this);


        View headerView = binding.navView.getHeaderView(0);
        headerBinding = NavHeaderMainBinding.bind(headerView);
        headerBinding.userName.setText(sharedPreferences.getString("Full_Name", null));
        Glide
                .with(this)
                .load(sharedPreferences.getString("Photo", "null"))
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(headerBinding.imageView);

        headerBinding.userEmail.setText(sharedPreferences.getString("Email", null));

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_driver_home, R.id.nav_map, R.id.nav_password_change)
                .setOpenableLayout(drawer)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.driver_frame);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.getMenu().findItem(R.id.nav_logout_parent).setOnMenuItemClickListener(menuItem -> {
            sessionManager.logout();
            return true;
        });

        getData();

        headerBinding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(DriverActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestReadPermission();
                    return;
                }
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_CODE);

            }
        });
    }

    private void requestReadPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this).setTitle(getString(R.string.permission_needed)).setMessage("Permission to read file").setPositiveButton(
                    getString( R.string.allow), (dialog, which) -> ActivityCompat.requestPermissions(DriverActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,}, READ_PERMISSION_CODE)
            ).setNegativeButton(getString(R.string.deny), (dialog, which) -> dialog.dismiss()).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CODE && resultCode == RESULT_OK && data != null) {

            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                upload();
            } catch ( IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String imagetoString(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    private void upload() {

        String imageURL = getString(R.string.Upload_Profile_URL);

        StringRequest image_request = new StringRequest(Request.Method.POST, imageURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    getData();
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("name", sessionManager.getUserDetails().get(SessionManager.USERNAME));
                params.put("image", imagetoString(bitmap));
                params.put("role",sharedPreferences.getString("Role","Driver"));

                return params;
            }
        };

        SingletonClass.getInstance(getApplicationContext()).addToRequestQueue(image_request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.driver_frame);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void getData() {

        stringRequest = new StringRequest(Request.Method.POST,  getString(R.string.Driver_URL),
                response -> {

                    editor = sharedPreferences.edit();
                    str = Pattern.compile(",").split(response);
                    editor.putString("Full_Name", str[0]);
                    editor.putString("Age", str[1]);
                    editor.putString("DOB", str[2]);
                    editor.putString("Gender", str[3]);
                    editor.putString("Email", str[4]);
                    editor.putString("Mobile_No1", str[5]);
                    editor.putString("Mobile_No2", str[6]);
                    editor.putString("Address",str[7]);
                    editor.putString("City",str[8]);
                    editor.putString("Pincode",str[9]);
                    editor.putString("Aadhar_No",str[10]);
                    editor.putString("Bus_No",str[11]);
                    editor.putString("Latitude",str[12]);
                    editor.putString("Longitude",str[13]);
                    editor.putString("Role",str[14]);
                    editor.putString("Photo",str[15]);
                    editor.apply();

                    Glide
                            .with(this)
                            .load(sharedPreferences.getString("Photo", "null"))
                            .centerCrop()
                            .placeholder(R.drawable.placeholder)
                            .into(headerBinding.imageView);
                    headerBinding.userName.setText(sharedPreferences.getString("Full_Name", null));
                    headerBinding.userEmail.setText(sharedPreferences.getString("Email", null));

                }, error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", Objects.requireNonNull(sharedPreferences.getString("USERNAME", "NULL")));
                return params;
            }
        };
        SingletonClass.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}