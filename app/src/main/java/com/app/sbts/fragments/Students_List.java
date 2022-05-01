package com.app.sbts.fragments;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.app.sbts.R;
import com.app.sbts.activities.RegisterStudent;
import com.app.sbts.adaptor.RecyclerViewAdapter;
import com.app.sbts.adaptor.StudentsAdaptor;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.FragmentStudentsListBinding;
import com.app.sbts.models.Student;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Students_List extends Fragment {

    FragmentStudentsListBinding binding;
    private JsonArrayRequest request;
    private List<Student> studentList;
    StudentsAdaptor sAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        requireActivity().getMenuInflater().inflate(R.menu.menu_print,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.print: {

                if(studentList.isEmpty()){
                    Toast.makeText(requireActivity().getApplicationContext(),"No data to print",Toast.LENGTH_LONG).show();
                    return true;
                }
                print();
                return true;
            }
        }

        NavController navController = Navigation.findNavController(requireActivity(), R.id.admin_frame);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }


    void  print(){

        Dexter.withContext(requireActivity().getApplicationContext()).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                createExcel();
            }
            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

            }
        }).check();

    }

    void createExcel(){
        File filePath = new File(Environment.getExternalStorageDirectory() + "/Download/Students.xls");
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Students List");

        HSSFRow headerRow = hssfSheet.createRow(0);
        HSSFCell nameCell = headerRow.createCell(0);
        nameCell.setCellValue("Name");
        HSSFCell rollCell = headerRow.createCell(1);
        rollCell.setCellValue("Roll No");
        HSSFCell divisionCell = headerRow.createCell(2);
        divisionCell.setCellValue("Division");
        HSSFCell classCell = headerRow.createCell(3);
        classCell.setCellValue("Class");

        int i = 1;
        for (Student student : studentList){
            HSSFRow arr =  hssfSheet.createRow(i);
            arr.createCell(0).setCellValue(student.getName());
            arr.createCell(1).setCellValue(student.getRoll_no());
            arr.createCell(2).setCellValue(student.getDivision());
            arr.createCell(3).setCellValue(student.getS_class());
            i++;
        }


        try {
            if (!filePath.exists()){
                filePath.createNewFile();
            }

            FileOutputStream fileOutputStream= new FileOutputStream(filePath);
            hssfWorkbook.write(fileOutputStream);

            if (fileOutputStream!=null){
                fileOutputStream.flush();
                fileOutputStream.close();
                Toast.makeText(requireActivity().getApplicationContext(),"File saved at "+filePath.toString(),Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding =  FragmentStudentsListBinding.inflate(inflater, container, false);
        studentList = new ArrayList<>();

        String url = getString(R.string.Student_List_URL);
        loadStudentList(url);

        binding.addStudent.setOnClickListener(v -> startActivity(new Intent(requireActivity(), RegisterStudent.class)));
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
                    s.setEmail(jsonObject.getString("Email"));
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