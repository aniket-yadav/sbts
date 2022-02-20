package com.app.sbts.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.app.sbts.databinding.FragmentParentHomeBinding;


public class ParentHome extends Fragment {

    private FragmentParentHomeBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentParentHomeBinding.inflate(inflater, container, false);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        binding.parentProfileName.setText(sharedPreferences.getString("Full_Name", null));
        String Photo = sharedPreferences.getString("Photo", null);
        if(!Photo.isEmpty()) {
            byte[] image_bit = Base64.decode(Photo, Base64.DEFAULT);
            binding.parentProfileImage.setImageBitmap(BitmapFactory.decodeByteArray(image_bit, 0, image_bit.length));
        }
        binding.parentProfileEmail.setText(sharedPreferences.getString("Email", null));
        binding.parentProfileMobile1.setText(sharedPreferences.getString("Mobile_No1", null));
        binding.parentProfileBus.setText(sharedPreferences.getString("Bus_No", null));
        binding.parentProfileStudentName.setText(sharedPreferences.getString("Student_Name", null));
        binding.parentProfileDOB.setText(sharedPreferences.getString("DOB", null));
        binding.parentProfileAddress.setText(sharedPreferences.getString("Address", null));

        return  binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}