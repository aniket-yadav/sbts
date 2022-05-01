package com.app.sbts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.sbts.R;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.ActivityAddBusBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddBus extends AppCompatActivity {

    ActivityAddBusBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBusBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.registerSubmit.setOnClickListener(
                v -> upload()
        );
    }

    private void upload() {

        String busNo = binding.busNo.getText().toString().trim();
        if(busNo.isEmpty()){
            Toast.makeText(AddBus.this,"Fill Bus no",Toast.LENGTH_LONG).show();
            return;
        }
        String addBus = getString(R.string.add_bus_url);

        StringRequest request = new StringRequest(Request.Method.POST, addBus, response -> Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show(), error -> {
            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("Vehicle_Name", Objects.requireNonNull(binding.vehicleName.getText()).toString().trim());
                params.put("Vehicle_Model", Objects.requireNonNull(binding.vehicleModel.getText()).toString().trim() );
                params.put("Capacity", Objects.requireNonNull(binding.capacity.getText()).toString().trim());
                params.put("Color", Objects.requireNonNull(binding.color.getText()).toString().trim());
                params.put("Bus_No", Objects.requireNonNull(binding.busNo.getText()).toString().trim());

                return params;
            }
        };

        SingletonClass.getInstance(getApplicationContext()).addToRequestQueue(request);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}