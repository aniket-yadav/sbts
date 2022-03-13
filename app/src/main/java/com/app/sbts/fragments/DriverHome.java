package com.app.sbts.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sbts.R;
import com.app.sbts.databinding.FragmentDriverHomeBinding;
import com.bumptech.glide.Glide;

public class DriverHome extends Fragment {


    private FragmentDriverHomeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDriverHomeBinding.inflate(inflater,container,false);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        binding.driverProfileName.setText(sharedPreferences.getString("Full_Name", null));
        String Photo = sharedPreferences.getString("Photo", null);
        Glide
                .with(requireActivity())
                .load(Photo)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(binding.driverProfileImage);

        binding.driverProfileEmail.setText(sharedPreferences.getString("Email", null));
        binding.driverProfileMobile1.setText(sharedPreferences.getString("Mobile_No1", null));
        binding.driverProfileBus.setText(sharedPreferences.getString("Bus_No", null));
        binding.driverProfileDOB.setText(sharedPreferences.getString("DOB", null));
        binding.driverProfileAddress.setText(sharedPreferences.getString("Address", null));


        return  binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}