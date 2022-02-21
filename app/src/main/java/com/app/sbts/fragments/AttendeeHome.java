package com.app.sbts.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sbts.R;
import com.app.sbts.databinding.FragmentAttendeeHomeBinding;
import com.bumptech.glide.Glide;

public class AttendeeHome extends Fragment {

    FragmentAttendeeHomeBinding binding;
    SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAttendeeHomeBinding.inflate(inflater, container, false);
        sharedPreferences = requireActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

//        Log.i("names",sharedPreferences.getString("Photo", null));
        String Photo = sharedPreferences.getString("Photo", null);
        Glide
                .with(requireActivity())
                .load(Photo)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(binding.attendeeProfileImage);
//        byte[] image_bit = Base64.decode(Photo, Base64.DEFAULT);
//        binding.attendeeProfileImage.setImasetgeBitmap(BitmapFactory.decodeByteArray(image_bit, 0, image_bit.length));

        binding.attendeeProfileName.setText(sharedPreferences.getString("Full_Name", null));
        binding.attendeeProfileAddress.setText(sharedPreferences.getString("Address", null));
        binding.attendeeProfileBus.setText(sharedPreferences.getString("Bus_No", null));
        binding.attendeeProfileDob.setText(sharedPreferences.getString("DOB", null));
        binding.attendeeProfileEmail.setText(sharedPreferences.getString("Email", null));
        binding.attendeeProfileMobile1.setText(sharedPreferences.getString("Mobile_No1", null));

        return  binding.getRoot();
    }

}