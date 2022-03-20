package com.app.sbts.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.sbts.R;
import com.app.sbts.databinding.FragmentParentListBinding;


public class ParentList extends Fragment {


    FragmentParentListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         binding = FragmentParentListBinding.inflate(inflater, container, false);
         return  binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}