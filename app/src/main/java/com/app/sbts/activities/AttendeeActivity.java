package com.app.sbts.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.app.sbts.R;
import com.app.sbts.databinding.ActivityAttendeeBinding;

public class AttendeeActivity extends AppCompatActivity {

    private ActivityAttendeeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAttendeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}