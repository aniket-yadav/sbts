package com.app.sbts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.sbts.R;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.ActivityRegisterUserBinding;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterUser extends AppCompatActivity {

    private  ActivityRegisterUserBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.registerSubmit.setOnClickListener(
                v -> upload()
        );
    }

    private void upload() {

        String registerParent = getString(R.string.Parent_Registration_URL);

        StringRequest image_request = new StringRequest(Request.Method.POST, registerParent, response -> Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show(), error -> {
            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("Full_Name", Objects.requireNonNull(binding.registerFullName.getText()).toString().trim());
                params.put("DOB", Objects.requireNonNull(binding.registerDob.getText()).toString().trim() );
                params.put("Email", Objects.requireNonNull(binding.registerEmail.getText()).toString().trim());
                params.put("Mobile_No1", Objects.requireNonNull(binding.registerMobile1.getText()).toString().trim());
                params.put("Mobile_No2", Objects.requireNonNull(binding.registerMobile2.getText()).toString().trim());
                params.put("Address", Objects.requireNonNull(binding.registerAddress.getText()).toString().trim());
                params.put("City", Objects.requireNonNull(binding.registerCity.getText()).toString().trim());
                params.put("Pincode", Objects.requireNonNull(binding.registerPinCode.getText()).toString().trim());
                params.put("Student_Id", Objects.requireNonNull(binding.registerStudentId.getText()).toString().trim());
                params.put("Student_Name", Objects.requireNonNull(binding.registerStudentName.getText()).toString().trim());
                params.put("Role","Parent");

                return params;
            }
        };

        SingletonClass.getInstance(getApplicationContext()).addToRequestQueue(image_request);
    }
}