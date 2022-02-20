package com.app.sbts.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.app.sbts.R;
import com.app.sbts.classes.SessionManager;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.ActivityParentBinding;
import com.app.sbts.databinding.NavHeaderMainBinding;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public class ParentActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityParentBinding binding;
    SharedPreferences sharedPreferences;
    SessionManager sessionManager;
    SharedPreferences.Editor editor;
    StringRequest stringRequest;
    String[] str;
    private  NavHeaderMainBinding headerBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();

        View headerView = binding.navView.getHeaderView(0);
        headerBinding = NavHeaderMainBinding.bind(headerView);

        headerBinding.userName.setText(sharedPreferences.getString("Full_Name", null));
        byte[] image_bit =  Base64.decode(sharedPreferences.getString("Photo", null),Base64.DEFAULT);
        headerBinding.imageView.setImageBitmap(BitmapFactory.decodeByteArray(image_bit, 0, image_bit.length));
        headerBinding.userEmail.setText(sharedPreferences.getString("Email", null));


        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_parent_home, R.id.nav_map, R.id.nav_password_change)
                .setOpenableLayout(drawer)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.parent_frame);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.getMenu().findItem(R.id.nav_logout_parent).setOnMenuItemClickListener(menuItem -> {
            sessionManager.logout();
            return true;
        });

        getData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.parent_frame);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void getData() {

        stringRequest = new StringRequest(Request.Method.POST,  getString(R.string.Parent_URL),
                response -> {
                    editor = sharedPreferences.edit();
                    str = Pattern.compile(",").split(response);
                    editor.putString("Full_Name", str[0]);
                    editor.putString("Photo", str[1]);
                    editor.putString("Email", str[2]);
                    editor.putString("Mobile_No1", str[3]);
                    editor.putString("Bus_No", str[4]);
                    editor.putString("DOB", str[5]);
                    editor.putString("Student_Name", str[6]);
                    editor.putString("Address", str[7]);
                    editor.putString("Latitude",str[8]);
                    editor.putString("Longitude",str[9]);
                    editor.apply();

                    String Photo = sharedPreferences.getString("Photo", null);
                    if(!Photo.isEmpty()) {
                        byte[] imagebit = Base64.decode(Photo, Base64.DEFAULT);
                        headerBinding.imageView.setImageBitmap(BitmapFactory.decodeByteArray(imagebit, 0, imagebit.length));
                    }
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