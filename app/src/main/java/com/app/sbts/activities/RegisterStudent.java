package com.app.sbts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.sbts.R;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.ActivityRegisterStudentBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterStudent extends AppCompatActivity {

    ActivityRegisterStudentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.registerSubmit.setOnClickListener(v -> upload());

    }

    private void upload() {

        String registerStudent = getString(R.string.Student_Registration_URL);

        StringRequest request = new StringRequest(Request.Method.POST, registerStudent, response -> Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show(), error -> {
            Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("Full_Name", Objects.requireNonNull(binding.studentName.getText()).toString().trim());
                params.put("Father_Name", Objects.requireNonNull(binding.fatherName.getText()).toString().trim() );
                params.put("Mother_Name", Objects.requireNonNull(binding.motherName.getText()).toString().trim());
                params.put("DOB", Objects.requireNonNull(binding.dob.getText()).toString().trim());
                params.put("Gender", Objects.requireNonNull(binding.gender.getText()).toString().trim());
                params.put("Age", Objects.requireNonNull(binding.age.getText()).toString().trim());
                params.put("Email", Objects.requireNonNull(binding.email.getText()).toString().trim());
                params.put("Mobile_No1", Objects.requireNonNull(binding.mobile1.getText()).toString().trim());
                params.put("Mobile_No2", Objects.requireNonNull(binding.mobile2.getText()).toString().trim());
                params.put("Address", Objects.requireNonNull(binding.address.getText()).toString().trim());
                params.put("City", Objects.requireNonNull(binding.city.getText()).toString().trim());
                params.put("Pincode", Objects.requireNonNull(binding.pincode.getText()).toString().trim());
                params.put("School_Id", Objects.requireNonNull(binding.schoolId.getText()).toString().trim());
                params.put("Roll_No", Objects.requireNonNull(binding.rollNo.getText()).toString().trim());
                params.put("Class", Objects.requireNonNull(binding.className.getText()).toString().trim());
                params.put("Division", Objects.requireNonNull(binding.division.getText()).toString().trim());

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