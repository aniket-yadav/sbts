package com.app.sbts.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.app.sbts.databinding.FragmentMyBusBinding;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;


public class MyBus extends Fragment {


    FragmentMyBusBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    StringRequest stringRequest;
    String[] str;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = requireActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);

        getData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyBusBinding.inflate(inflater, container, false);
        return  binding.getRoot();
    }

    private void getData() {

        stringRequest = new StringRequest(Request.Method.POST,  getString(R.string.Bus_URL),
                response -> {

                    editor = sharedPreferences.edit();
                    str = Pattern.compile(",").split(response);
                    editor.putString("Vehicle_Name", str[0]);
                    editor.putString("Vehicle_Model", str[1]);
                    editor.putString("Capacity", str[2]);
                    editor.putString("Color", str[3]);
                    editor.putString("Bus_No", str[4]);
                    editor.apply();
                    String vehicleName = "Vehicle Name:- "+sharedPreferences.getString("Vehicle_Name","N/A");
                    String vehicleModel = "Vehicle Model:- "+sharedPreferences.getString("Vehicle_Model","N/A");
                    String vehicleCapacity = "Vehicle Capacity:- "+sharedPreferences.getString("Capacity","N/A");
                    String vehicleColor = "Vehicle Color:- "+sharedPreferences.getString("Color","N/A");
                    String vehicleBusNo = "Vehicle Bus No:- "+sharedPreferences.getString("Bus_No","N/A");
                    binding.vehicleName.setText(vehicleName);
                    binding.vehicleModel.setText(vehicleModel);
                    binding.capacity.setText(vehicleCapacity);
                    binding.busColor.setText(vehicleColor);
                    binding.busNo.setText(vehicleBusNo);


                }, error -> Toast.makeText(requireContext(), error.toString(), Toast.LENGTH_LONG).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("driver", Objects.requireNonNull(sharedPreferences.getString("Full_Name", "default")));
                return params;
            }
        };
        SingletonClass.getInstance(requireContext()).addToRequestQueue(stringRequest);
    }

}