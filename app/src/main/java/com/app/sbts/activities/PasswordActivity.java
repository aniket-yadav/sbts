package com.app.sbts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.sbts.databinding.ActivityPasswordBinding;

public class PasswordActivity extends AppCompatActivity {

    private  ActivityPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}