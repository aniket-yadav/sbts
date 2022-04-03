package com.app.sbts.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.app.sbts.R;
import com.app.sbts.activities.RegisterDriver;
import com.app.sbts.adaptor.BusAdaptor;
import com.app.sbts.adaptor.DriverAdaptor;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.FragmentDriverListBinding;
import com.app.sbts.models.Bus;
import com.app.sbts.models.Driver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DriverList extends Fragment {

    FragmentDriverListBinding binding;
    private JsonArrayRequest request;
    private List<Driver> driverList;
    DriverAdaptor driverAdaptor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDriverListBinding.inflate(inflater, container, false);
        driverList = new ArrayList<>();

        String url = getString(R.string.driver_list_url);
        loadDriverList(url);

        binding.addDriver.setOnClickListener(v -> startActivity(new Intent(requireActivity(), RegisterDriver.class)));
        return  binding.getRoot();


    }

    void  loadDriverList(String url){
        binding.loadingDriverList.setVisibility(View.VISIBLE);
        request = new JsonArrayRequest(url, response -> {
            driverList.clear();
            JSONObject jsonObject;

            for (int i = response.length() - 1; i >= 0; i--) {
                try {
                    jsonObject = response.getJSONObject(i);
                    Driver driver = new Driver();
                    driver.setFull_Name(jsonObject.getString("Full_Name"));
                    driver.setMobile_No1(jsonObject.getString("Email"));
                    driver.setEmail(jsonObject.getString("Mobile_No1"));
                    driver.setBus_No(jsonObject.getString("Bus_No"));
                    driverList.add(driver);
                } catch ( JSONException e) {
                    e.printStackTrace();
                }
            }

            binding.loadingDriverList.setVisibility(View.GONE);

            driverAdaptor = new DriverAdaptor(requireActivity().getApplicationContext(), driverList);
            binding.driverRecycler.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
            binding.driverRecycler.setAdapter(driverAdaptor);

        }, error -> {
            driverList.clear();
            Toast.makeText(requireActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            binding.loadingDriverList.setVisibility(View.GONE);
        });

        SingletonClass.getInstance(getContext()).addToRequestQueue(request);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}