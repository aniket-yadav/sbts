package com.app.sbts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.sbts.R;
import com.app.sbts.databinding.ActivityRegisterUserBinding;

public class RegisterUser extends AppCompatActivity {

    private  ActivityRegisterUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}