package com.app.sbts.adaptor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.app.sbts.R;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.CustomDialogBinding;
import com.app.sbts.databinding.DriverItemBinding;
import com.app.sbts.models.Driver;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;


public class DriverAdaptor extends RecyclerView.Adapter<DriverAdaptor.MyViewHolder> {

    private final Context sContext;
    private final List<Driver> sData;
    private JsonArrayRequest request;
    private List<String> busNoList = new ArrayList<>();
    private  final String role;

    public DriverAdaptor(Context sContext, List<Driver> sData,String role) {
        this.role = role;
        this.sContext = sContext;
        this.sData = sData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyViewHolder(DriverItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        myViewHolder.binding.driverName.setText(sData.get(i).getFull_Name());
        myViewHolder.binding.driverEmail.setText(sData.get(i).getEmail());
        myViewHolder.binding.driverMobile.setText(sData.get(i).getMobile_No1());
        myViewHolder.binding.driverBus.setText(sData.get(i).getBus_No());
        myViewHolder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url  = role.equalsIgnoreCase("Attendee") ? "https://sbts2022.000webhostapp.com/api/delete_attendee.php" : "https://sbts2022.000webhostapp.com/api/delete_driver.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,  url,
                        response -> {
                            Toast.makeText(sContext, response, Toast.LENGTH_LONG).show();
                        }, error -> Toast.makeText(sContext, error.toString(), Toast.LENGTH_LONG).show()) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", myViewHolder.binding.driverEmail.getText().toString().trim());
                        return params;
                    }
                };
                SingletonClass.getInstance(sContext).addToRequestQueue(stringRequest);
            }
        });
        myViewHolder.binding.container.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(myViewHolder.binding.getRoot().getContext());
            ViewGroup viewGroup = (ViewGroup) myViewHolder.binding.getRoot().getParent();
            View dialogView =  LayoutInflater.from(v.getContext()).inflate(R.layout.custom_dialog, viewGroup, false);
            CustomDialogBinding customDialogBinding = CustomDialogBinding.bind(dialogView);
            request = new JsonArrayRequest(sContext.getString(R.string.bus_no_url), response -> {
                busNoList.clear();
                for(int j = response.length() - 1 ; j >= 0; j--){
                    try {
                        busNoList.add(response.getString(j));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                ArrayAdapter busesAdaptor = new ArrayAdapter(sContext, android.R.layout.simple_spinner_dropdown_item,busNoList);
                busesAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                customDialogBinding.customDialog.setAdapter(busesAdaptor);

            }, error -> {
                busNoList.clear();
                Toast.makeText(sContext, error.toString(), Toast.LENGTH_LONG).show();

            });

            SingletonClass.getInstance(sContext).addToRequestQueue(request);

            customDialogBinding.applyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   StringRequest stringRequest = new StringRequest(Request.Method.POST,  sContext.getString(R.string.update_bus_no),
                            response -> {
                                Toast.makeText(sContext, response, Toast.LENGTH_LONG).show();
                            }, error -> Toast.makeText(sContext, error.toString(), Toast.LENGTH_LONG).show()) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("email", myViewHolder.binding.driverEmail.getText().toString().trim());
                            params.put("bus",customDialogBinding.customDialog.getSelectedItem().toString() );
                            params.put("role",role );
                            return params;
                        }
                    };
                    SingletonClass.getInstance(sContext).addToRequestQueue(stringRequest);
                }
            });
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        });
    }

    @Override
    public int getItemCount() {
        return sData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final DriverItemBinding binding;

        public MyViewHolder(DriverItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
