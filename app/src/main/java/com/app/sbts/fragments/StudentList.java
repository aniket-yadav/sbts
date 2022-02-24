package com.app.sbts.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.sbts.R;
import com.app.sbts.adaptor.RecyclerViewAdapter;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.FragmentStudentListBinding;
import com.app.sbts.models.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentList extends Fragment {

    FragmentStudentListBinding binding;
    private JsonArrayRequest request;
    private List<Student> studentList;
//    private RecyclerView recyclerView;
//    private ProgressBar loading;
    RecyclerViewAdapter sAdapter;
    AlertDialog.Builder builder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        builder = new AlertDialog.Builder(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentListBinding.inflate(inflater, container, false);
        studentList = new ArrayList<>();


        request = new JsonArrayRequest(getString(R.string.Student_List_URL), new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject;

                for (int i = response.length() - 1; i >= 0; i--) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Student s = new Student();
                        s.setName(jsonObject.getString("Full_Name"));
                        s.setDivision(jsonObject.getString("Division"));
                        s.setRoll_no(jsonObject.getString("Roll_No"));
                        s.setS_class(jsonObject.getString("Class"));
                        s.setPhoto(jsonObject.getString("Photo"));
                        studentList.add(s);
                    } catch ( JSONException e) {
                        e.printStackTrace();
                    }
                }

              binding.loadingStudentList.setVisibility(View.GONE);

                sAdapter = new RecyclerViewAdapter(getActivity().getApplicationContext(), studentList, builder);
              binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
               binding.recyclerView.setAdapter(sAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        SingletonClass.getInstance(getContext()).addToRequestQueue(request);


        return  binding.getRoot();
    }
}