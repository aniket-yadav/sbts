package com.app.sbts.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.app.sbts.R;
import com.app.sbts.classes.SessionManager;
import com.app.sbts.databinding.ActivityAdminBinding;
import com.app.sbts.databinding.NavHeaderMainBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {

    ActivityAdminBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    SessionManager sessionManager;
    private String User;
    SharedPreferences sharedPreferences;
    private  NavHeaderMainBinding headerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMainAdmin.toolbar);

        sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        User = user.get(SessionManager.USERNAME);

        View headerView = binding.navView.getHeaderView(0);
        headerBinding = NavHeaderMainBinding.bind(headerView);


        headerBinding.userName.setText(sessionManager.getUserDetails().get(SessionManager.USERNAME));

        headerBinding.userEmail.setVisibility(View.GONE);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_busList, R.id.nav_driver_list, R.id.nav_parent_list,R.id.nav_students_list,R.id.nav_bus_locations,R.id.nav_requests_list)
                .setOpenableLayout(drawer)
                .build();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.admin_frame);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            sessionManager.logout();
            return true;
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.admin_frame);
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}