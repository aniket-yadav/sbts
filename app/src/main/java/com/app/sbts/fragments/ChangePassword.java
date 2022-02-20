package com.app.sbts.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.sbts.R;
import com.app.sbts.classes.SessionManager;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.FragmentChangePasswordBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ChangePassword extends Fragment {

    private FragmentChangePasswordBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentChangePasswordBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SessionManager sessionManager = new SessionManager(requireActivity().getApplicationContext());
        final HashMap<String, String> user = sessionManager.getUserDetails();
       binding.passwordChange.setOnClickListener(v -> {
           if (Objects.requireNonNull(binding.oldPassword.getText()).toString().isEmpty()) {
               Toast.makeText(requireActivity().getApplicationContext(), getString(R.string.old_pass_error), Toast.LENGTH_LONG).show();

           } else if (Objects.requireNonNull(binding.newPassword.getText()).toString().isEmpty()) {
               Toast.makeText(requireActivity().getApplicationContext(),getString( R.string.new_password_error), Toast.LENGTH_LONG).show();

           } else if (binding.oldPassword.getText().toString().equalsIgnoreCase(binding.newPassword.getText().toString())) {

               Toast.makeText(requireActivity().getApplicationContext(), getString(R.string.same_pass_error), Toast.LENGTH_LONG).show();

           } else {

               StringRequest stringRequest = new StringRequest(Request.Method.POST, getString(R.string.Change_Password_URL), response -> Toast.makeText(requireActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show(), error -> Toast.makeText(requireActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show()) {
                   @Override
                   protected Map<String, String> getParams() {
                       Map<String, String> params = new HashMap<>();
                       params.put("username", user.get(SessionManager.USERNAME));
                       params.put("role",user.get(SessionManager.ROLE));
                       params.put("old_password", binding.oldPassword.getText().toString());
                       params.put("new_password", binding.newPassword.getText().toString());
                       return params;
                   }
               };
               SingletonClass.getInstance(requireActivity().getApplicationContext()).addToRequestQueue(stringRequest);

           }
       });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}