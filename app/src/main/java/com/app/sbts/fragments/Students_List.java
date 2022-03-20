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
import com.app.sbts.adaptor.RecyclerViewAdapter;
import com.app.sbts.adaptor.StudentsAdaptor;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.FragmentStudentsListBinding;
import com.app.sbts.models.Student;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Students_List extends Fragment {

    FragmentStudentsListBinding binding;
    private JsonArrayRequest request;
    private List<Student> studentList;
    StudentsAdaptor sAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding =  FragmentStudentsListBinding.inflate(inflater, container, false);
        studentList = new ArrayList<>();

        String url = getString(R.string.Student_List_URL);
        loadStudentList(url);

        return  binding.getRoot();
    }

    void  loadStudentList(String url){
        binding.loadingStudentList.setVisibility(View.VISIBLE);
        request = new JsonArrayRequest(url, response -> {
            studentList.clear();
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

            sAdapter = new StudentsAdaptor(requireActivity().getApplicationContext(), studentList);
            binding.studentRecycler.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
            binding.studentRecycler.setAdapter(sAdapter);

        }, error -> {
            studentList.clear();
            Toast.makeText(requireActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            binding.loadingStudentList.setVisibility(View.GONE);
        });

        SingletonClass.getInstance(getContext()).addToRequestQueue(request);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}