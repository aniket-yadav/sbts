package com.app.sbts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.app.sbts.R;
import com.app.sbts.fragments.MapsFragment;

public class ParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new MapsFragment()).commit();
        }
    }
}