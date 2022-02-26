package com.app.sbts.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentList extends Fragment {

    FragmentStudentListBinding binding;
    private JsonArrayRequest request;
    private List<Student> studentList;
    RecyclerViewAdapter sAdapter;
    AlertDialog.Builder builder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        builder = new AlertDialog.Builder(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        requireActivity().getMenuInflater().inflate(R.menu.studentlist_filter_menu,menu);
    }






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStudentListBinding.inflate(inflater, container, false);
        studentList = new ArrayList<>();

        String url = getString(R.string.Student_List_URL);
        loadStudentList(url);

        return  binding.getRoot();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
            case R.id.all: {
                String url = getString(R.string.Student_List_URL);
                loadStudentList(url);
                return true;
            }
            case R.id.present: {
                String url = getString(R.string.Present_Students_URL);
                loadStudentList(url);
                return true;
            }
            case R.id.absent: {
                String url = getString(R.string.Absent_Students_URL);
                loadStudentList(url);
                return true;
            }
        }

        NavController navController = Navigation.findNavController(requireActivity(), R.id.attendee_frame);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
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

            sAdapter = new RecyclerViewAdapter(requireActivity().getApplicationContext(), studentList, builder);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
            binding.recyclerView.setAdapter(sAdapter);

        }, error -> {
            studentList.clear();
            Toast.makeText(requireActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            binding.loadingStudentList.setVisibility(View.GONE);
        });

        SingletonClass.getInstance(getContext()).addToRequestQueue(request);


    }
}