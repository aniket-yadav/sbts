package com.app.sbts.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.app.sbts.R;
import com.app.sbts.adaptor.BusAdaptor;
import com.app.sbts.adaptor.StudentsAdaptor;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.FragmentBusListBinding;
import com.app.sbts.models.Bus;
import com.app.sbts.models.Student;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BusList extends Fragment {

    FragmentBusListBinding binding;
    private JsonArrayRequest request;
    private List<Bus> busList;
    BusAdaptor busAdaptor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding =  FragmentBusListBinding.inflate(inflater, container, false);
        busList = new ArrayList<>();

        String url = getString(R.string.bus_list_url);
        loadBusList(url);
        return  binding.getRoot();


    }

    void  loadBusList(String url){
        binding.loadingBusList.setVisibility(View.VISIBLE);
        request = new JsonArrayRequest(url, response -> {
            busList.clear();
            JSONObject jsonObject;

            for (int i = response.length() - 1; i >= 0; i--) {
                try {
                    jsonObject = response.getJSONObject(i);
                    Bus s = new Bus();
                    s.setVehicleName(jsonObject.getString("Vehicle_Name"));
                    s.setVehicleColor(jsonObject.getString("Color"));
                    s.setVehicleCapacity(jsonObject.getString("Capacity"));
                    s.setVehicleDriver(jsonObject.getString("Driver_Name"));
                    s.setVehicleModel(jsonObject.getString("Vehicle_Model"));
                    s.setVehicleNo(jsonObject.getString("Bus_No"));
                    busList.add(s);
                } catch ( JSONException e) {
                    e.printStackTrace();
                }
            }

            binding.loadingBusList.setVisibility(View.GONE);

            busAdaptor = new BusAdaptor(requireActivity().getApplicationContext(), busList);
            binding.busRecycler.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
            binding.busRecycler.setAdapter(busAdaptor);

        }, error -> {
            busList.clear();
            Toast.makeText(requireActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            binding.loadingBusList.setVisibility(View.GONE);
        });

        SingletonClass.getInstance(getContext()).addToRequestQueue(request);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}