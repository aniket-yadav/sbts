package com.app.sbts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.sbts.R;
import com.app.sbts.classes.SessionManager;
import com.app.sbts.databinding.ActivityMainBinding;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        sessionManager.checkLogIn();

        HashMap<String, String> user = sessionManager.getUserDetails();
        String role = user.get(SessionManager.ROLE);
        Log.i("login response", role);

        if (role != null) {
            switch (role) {
                case "Attendee":
                    Intent attendee = new Intent(MainActivity.this, AttendeeActivity.class);
                    startActivity(attendee);
                    finish();
                    break;
                case "Parent":
                    Intent parent = new Intent(MainActivity.this, ParentActivity.class);
                    startActivity(parent);
                    finish();
                    break;
                case "Driver":
                    Intent driver = new Intent(MainActivity.this, DriverActivity.class);
                    startActivity(driver);
                    finish();
                    break;
                case "Admin":
                    Intent admin = new Intent(MainActivity.this, AdminActivity.class);
                    startActivity(admin);
                    finish();
                    break;
            }
        }
    }
}