package com.app.sbts.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sbts.R;
import com.app.sbts.databinding.FragmentAttendeeHomeBinding;

public class AttendeeHome extends Fragment {


    FragmentAttendeeHomeBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAttendeeHomeBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

}