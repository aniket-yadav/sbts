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
import com.app.sbts.adaptor.DriverAdaptor;
import com.app.sbts.adaptor.ParentAdaptor;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.FragmentParentListBinding;
import com.app.sbts.models.Driver;
import com.app.sbts.models.Parent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ParentList extends Fragment {


    FragmentParentListBinding binding;
    private JsonArrayRequest request;
    private List<Parent> parentList;
    ParentAdaptor parentAdaptor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         binding = FragmentParentListBinding.inflate(inflater, container, false);
        parentList = new ArrayList<>();

        String url = getString(R.string.parent_list_url);
        loadParentList(url);
         return  binding.getRoot();
    }

    void  loadParentList(String url){
        binding.loadingParentList.setVisibility(View.VISIBLE);
        request = new JsonArrayRequest(url, response -> {
            parentList.clear();
            JSONObject jsonObject;

            for (int i = response.length() - 1; i >= 0; i--) {
                try {
                    jsonObject = response.getJSONObject(i);
                    Parent parent = new Parent();
                    parent.setFull_Name(jsonObject.getString("Full_Name"));
                    parent.setMobile_No1(jsonObject.getString("Email"));
                    parent.setEmail(jsonObject.getString("Mobile_No1"));
                    parent.setStudent_Name(jsonObject.getString("Student_Name"));
                    parentList.add(parent);
                } catch ( JSONException e) {
                    e.printStackTrace();
                }
            }

            binding.loadingParentList.setVisibility(View.GONE);

            parentAdaptor = new ParentAdaptor(requireActivity().getApplicationContext(), parentList);
            binding.parentRecycler.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
            binding.parentRecycler.setAdapter(parentAdaptor);

        }, error -> {
            parentList.clear();
            Toast.makeText(requireActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            binding.loadingParentList.setVisibility(View.GONE);
        });

        SingletonClass.getInstance(getContext()).addToRequestQueue(request);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}