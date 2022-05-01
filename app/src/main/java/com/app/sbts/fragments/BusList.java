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
import com.app.sbts.activities.AddBus;
import com.app.sbts.adaptor.BusAdaptor;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.FragmentBusListBinding;
import com.app.sbts.models.Bus;

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


public class BusList extends Fragment {

    FragmentBusListBinding binding;
    private JsonArrayRequest request;
    private List<Bus> busList;
    BusAdaptor busAdaptor;

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

                if(busList.isEmpty()){
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
        File filePath = new File(Environment.getExternalStorageDirectory() + "/Download/BusList.xls");
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        HSSFSheet hssfSheet = hssfWorkbook.createSheet("Bus List");
        HSSFRow headerRow = hssfSheet.createRow(0);
        HSSFCell nameCell = headerRow.createCell(0);
        nameCell.setCellValue("Vehicle Name");
        HSSFCell modelCell = headerRow.createCell(1);
        modelCell.setCellValue("Vehicle Model");
        HSSFCell capacityCell = headerRow.createCell(2);
        capacityCell.setCellValue("Vehicle Capacity");
        HSSFCell colorCell = headerRow.createCell(3);
        colorCell.setCellValue("Vehicle Color");
        HSSFCell noCell = headerRow.createCell(4);
        noCell.setCellValue("Vehicle No");
        HSSFCell driverCell = headerRow.createCell(5);
        driverCell.setCellValue("Vehicle Driver");

        int i = 1;
        for (Bus attendee : busList){
            HSSFRow arr =  hssfSheet.createRow(i);
            arr.createCell(0).setCellValue(attendee.getVehicleName());
            arr.createCell(1).setCellValue(attendee.getVehicleModel());
            arr.createCell(2).setCellValue(attendee.getVehicleCapacity());
            arr.createCell(3).setCellValue(attendee.getVehicleColor());
            arr.createCell(4).setCellValue(attendee.getVehicleNo());
            arr.createCell(5).setCellValue(attendee.getVehicleDriver());

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

        binding =  FragmentBusListBinding.inflate(inflater, container, false);
        busList = new ArrayList<>();

        String url = getString(R.string.bus_list_url);
        loadBusList(url);
        binding.addBus.setOnClickListener(v -> startActivity(new Intent(requireActivity(), AddBus.class)));
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