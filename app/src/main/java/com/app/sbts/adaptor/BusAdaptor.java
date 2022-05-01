package com.app.sbts.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.BusItemBinding;
import com.app.sbts.models.Bus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BusAdaptor extends RecyclerView.Adapter<BusAdaptor.MyViewHolder> {

    private final Context sContext;
    private final List<Bus> sData;

    public BusAdaptor(Context sContext, List<Bus> sData) {

        this.sContext = sContext;
        this.sData = sData;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyViewHolder(BusItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        myViewHolder.binding.vehicleModel.setText(sData.get(i).getVehicleModel());
        myViewHolder.binding.vehicleName.setText(sData.get(i).getVehicleName());
        myViewHolder.binding.vehicleCapacity.setText(sData.get(i).getVehicleCapacity());
        myViewHolder.binding.vehicleColor.setText(sData.get(i).getVehicleColor());
        myViewHolder.binding.vehicleDriver.setText(sData.get(i).getVehicleDriver());
        myViewHolder.binding.vehicleNo.setText(sData.get(i).getVehicleNo());
        myViewHolder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url  =  "https://sbts2022.000webhostapp.com/api/delete_bus.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,  url,
                        response -> {
                            Toast.makeText(sContext, response, Toast.LENGTH_LONG).show();
                        }, error -> Toast.makeText(sContext, error.toString(), Toast.LENGTH_LONG).show()) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("busNo", sData.get(myViewHolder.getAdapterPosition()).getVehicleNo());
                        return params;
                    }
                };
                SingletonClass.getInstance(sContext).addToRequestQueue(stringRequest);
            }
        });


    }

    @Override
    public int getItemCount() {
        return sData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final BusItemBinding binding;

        public MyViewHolder(BusItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
