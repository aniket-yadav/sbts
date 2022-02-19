package com.app.sbts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.sbts.R;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.ActivityPasswordBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PasswordActivity extends AppCompatActivity {

    private  ActivityPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.passwordReset.setOnClickListener(submitClickListener);
    }


    View.OnClickListener submitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Objects.requireNonNull(binding.passwordResetEmail.getText()).toString().isEmpty()) {
                Toast.makeText(getApplicationContext(),getString( R.string.enter_email), Toast.LENGTH_LONG).show();
            } else {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.Reset_Password_URL), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }, error -> {

                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("email",binding.passwordResetEmail.getText().toString());
                        return params;
                    }
                };
                SingletonClass.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
            }
        }
    };
}