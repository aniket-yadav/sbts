package com.app.sbts.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.sbts.databinding.BusItemBinding;
import com.app.sbts.models.Bus;
import java.util.List;



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
