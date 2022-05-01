package com.app.sbts.fragments;

import android.Manifest;
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
import com.app.sbts.adaptor.ParentAdaptor;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.FragmentParentListBinding;
import com.app.sbts.models.Driver;
import com.app.sbts.models.Parent;
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


public class ParentList extends Fragment {


    FragmentParentListBinding binding;
    private JsonArrayRequest request;
    private List<Parent> parentList;
    ParentAdaptor parentAdaptor;



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

                if(parentList.isEmpty()){
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
        File filePath = new File(Environment.getExternalStorageDirectory() + "/Download/ParentList.xls");
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Parent List");
        HSSFRow headerRow = hssfSheet.createRow(0);
        HSSFCell nameCell = headerRow.createCell(0);
        nameCell.setCellValue("Full Name");
        HSSFCell emailCell = headerRow.createCell(1);
        emailCell.setCellValue("Email");
        HSSFCell mobileCell = headerRow.createCell(2);
        mobileCell.setCellValue("Mobile No");
        HSSFCell busCell = headerRow.createCell(3);
        busCell.setCellValue("Student Name");

        int i = 1;
        for (Parent parent : parentList){
            HSSFRow arr =  hssfSheet.createRow(i);
            arr.createCell(0).setCellValue(parent.getFull_Name());
            arr.createCell(1).setCellValue(parent.getEmail());
            arr.createCell(2).setCellValue(parent.getMobile_No1());
            arr.createCell(3).setCellValue(parent.getStudent_Name());
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
                    parent.setEmail(jsonObject.getString("Email"));
                    parent.setMobile_No1(jsonObject.getString("Mobile_No1"));
                    parent.setStudent_Name(jsonObject.getString("Student_Name"));
                    parent.setHasPaid(jsonObject.getString("hasPaid"));
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