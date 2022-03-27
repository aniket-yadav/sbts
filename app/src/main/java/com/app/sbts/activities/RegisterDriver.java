package com.app.sbts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.sbts.R;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.ActivityRegisterDriverBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterDriver extends AppCompatActivity {

    ActivityRegisterDriverBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterDriverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerSubmit.setOnClickListener(v -> upload());
    }

    private void upload() {

        String registerDriver = getString(R.string.Driver_Registration_URL);

        StringRequest request = new StringRequest(Request.Method.POST, registerDriver, response -> Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show(), error -> {
            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("Full_Name", Objects.requireNonNull(binding.registerFullName.getText()).toString().trim());
                params.put("Age", Objects.requireNonNull(binding.age.getText()).toString().trim() );
                params.put("Email", Objects.requireNonNull(binding.email.getText()).toString().trim());
                params.put("Mobile_No1", Objects.requireNonNull(binding.registerMobile1.getText()).toString().trim());
                params.put("Mobile_No2", Objects.requireNonNull(binding.registerMobile2.getText()).toString().trim());
                params.put("Address", Objects.requireNonNull(binding.registerAddress.getText()).toString().trim());
                params.put("City", Objects.requireNonNull(binding.registerCity.getText()).toString().trim());
                params.put("Pincode", Objects.requireNonNull(binding.registerPinCode.getText()).toString().trim());
                params.put("Aadhar_No", Objects.requireNonNull(binding.aadhaar.getText()).toString().trim());

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