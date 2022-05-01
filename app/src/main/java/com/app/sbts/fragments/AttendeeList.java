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
import com.app.sbts.activities.RegisterAttendee;
import com.app.sbts.adaptor.DriverAdaptor;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.FragmentAttendeeListBinding;
import com.app.sbts.models.Driver;
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

public class AttendeeList extends Fragment {

    FragmentAttendeeListBinding binding;
    private JsonArrayRequest request;
    private List<Driver> driverList;
    DriverAdaptor driverAdaptor;



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

                if(driverList.isEmpty()){
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
        File filePath = new File(Environment.getExternalStorageDirectory() + "/Download/AttendeeList.xls");
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Attendee List");
        HSSFRow headerRow = hssfSheet.createRow(0);
        HSSFCell nameCell = headerRow.createCell(0);
        nameCell.setCellValue("Full Name");
        HSSFCell emailCell = headerRow.createCell(1);
        emailCell.setCellValue("Email");
        HSSFCell mobileCell = headerRow.createCell(2);
        mobileCell.setCellValue("Mobile No");
        HSSFCell busCell = headerRow.createCell(3);
        busCell.setCellValue("Bus No");

        int i = 1;
        for (Driver attendee : driverList){
            HSSFRow arr =  hssfSheet.createRow(i);
            arr.createCell(0).setCellValue(attendee.getFull_Name());
            arr.createCell(1).setCellValue(attendee.getEmail());
            arr.createCell(2).setCellValue(attendee.getMobile_No1());
            arr.createCell(3).setCellValue(attendee.getBus_No());
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

        binding = FragmentAttendeeListBinding.inflate(inflater, container, false);
        driverList = new ArrayList<>();

        String url = getString(R.string.attendee_list_url);
        loadDriverList(url);
        binding.addAttendee.setOnClickListener(v -> startActivity(new Intent(requireActivity(), RegisterAttendee.class)));
        return  binding.getRoot();


    }

    void  loadDriverList(String url){
        binding.loadingAttendeeList.setVisibility(View.VISIBLE);
        request = new JsonArrayRequest(url, response -> {
            driverList.clear();
            JSONObject jsonObject;

            for (int i = response.length() - 1; i >= 0; i--) {
                try {
                    jsonObject = response.getJSONObject(i);
                    Driver driver = new Driver();
                    driver.setFull_Name(jsonObject.getString("Full_Name"));
                    driver.setEmail(jsonObject.getString("Email"));
                    driver.setMobile_No1(jsonObject.getString("Mobile_No1"));
                    driver.setBus_No(jsonObject.getString("Bus_No"));
                    driverList.add(driver);
                } catch ( JSONException e) {
                    e.printStackTrace();
                }
            }

            binding.loadingAttendeeList.setVisibility(View.GONE);

            driverAdaptor = new DriverAdaptor(requireActivity().getApplicationContext(), driverList,"Attendee");
            binding.attendeeRecycler.setLayoutManager(new LinearLayoutManager(requireActivity().getApplicationContext()));
            binding.attendeeRecycler.setAdapter(driverAdaptor);

        }, error -> {
            driverList.clear();
            Toast.makeText(requireActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            binding.attendeeRecycler.setVisibility(View.GONE);
        });

        SingletonClass.getInstance(getContext()).addToRequestQueue(request);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}