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
import com.app.sbts.adaptor.ParentAdaptor;
import com.app.sbts.adaptor.RequestsAdaptor;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.FragmentRegistrationRequestsBinding;
import com.app.sbts.models.Parent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class RegistrationRequests extends Fragment {

    FragmentRegistrationRequestsBinding binding;
    private JsonArrayRequest request;
    private List<Parent> requestsList;
    RequestsAdaptor requestsAdaptor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentRegistrationRequestsBinding.inflate(inflater, container, false);
        requestsList = new ArrayList<>();

        String url = getString(R.string.request_list_url);
        loadRequestsList(url);
        return  binding.getRoot();
    }

    void  loadRequestsList(String url){
        binding.loadingRequestsList.setVisibility(View.VISIBLE);
        request = new JsonArrayRequest(url, response -> {
            requestsList.clear();
            JSONObject jsonObject;

            for (int i = response.length() - 1; i >= 0; i--) {
                try {
                    jsonObject = response.getJSONObject(i);
                    Parent parent = new Parent();
                    parent.setFull_Name(jsonObject.getString("Full_Name"));
                    parent.setMobile_No1(jsonObject.getString("Mobile_No1"));
                    parent.setEmail(jsonObject.getString("Email"));
                    parent.setStudent_Name(jsonObject.getString("Student_Name"));
                    requestsList.add(parent);
                } catch ( JSONException e) {
                    e.printStackTrace();
                }
            }

            binding.loadingRequestsList.setVisibility(View.GONE);

            requestsAdaptor = new RequestsAdaptor(requireActivity().getApplicationContext(), requestsList);
            binding.requestsRecycler.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
            binding.requestsRecycler.setAdapter(requestsAdaptor);

        }, error -> {
            requestsList.clear();
            Toast.makeText(requireActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            binding.loadingRequestsList.setVisibility(View.GONE);
        });

        SingletonClass.getInstance(getContext()).addToRequestQueue(request);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}