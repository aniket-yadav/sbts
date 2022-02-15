package com.app.sbts.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Toast;

import com.app.sbts.R;
import com.app.sbts.classes.SessionManager;
import com.app.sbts.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private  int LOCATION_PERMISSION_CODE = 1;
    private String role, user;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sessionManager = new SessionManager(this);

        final Intent register = new Intent(this, RegisterUser.class);
        final Intent reset = new Intent(this, PasswordActivity.class);
        SpannableString rss = new SpannableString(getString(R.string.register_text));

        ClickableSpan rcs = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(register);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.RED);
            }
        };

        rss.setSpan(rcs,30,40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.userRegistration.setText(rss);
        binding.userRegistration.setMovementMethod(LinkMovementMethod.getInstance());
        SpannableString pss = new SpannableString(getString(R.string.reset_password));
        ClickableSpan pcs = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(reset);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.RED);
            }
        };

        pss.setSpan(pcs,0,16,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.resetPassword.setText(pss);
        binding.resetPassword.setMovementMethod(LinkMovementMethod.getInstance());

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        }

        binding.loading.setVisibility(View.GONE);
        binding.loginButton.setVisibility(View.VISIBLE);
        binding.loginButton.setOnClickListener(loginClickListener);
    }

   View.OnClickListener loginClickListener = new View.OnClickListener() {
       @Override
       public void onClick(View v) {

       }
   };

    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            new AlertDialog.Builder(this).setTitle(getString(R.string.permission_needed)).setMessage(getString(R.string.permission_required_message)).setPositiveButton(
                   getString( R.string.allow), (dialog, which) -> ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE)
            ).setNegativeButton(getString(R.string.deny), (dialog, which) -> dialog.dismiss()).create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,getString( R.string.permission_granted), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }
}