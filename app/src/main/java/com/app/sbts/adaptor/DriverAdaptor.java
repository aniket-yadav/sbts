package com.app.sbts.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.sbts.databinding.BusItemBinding;
import com.app.sbts.databinding.DriverItemBinding;
import com.app.sbts.models.Bus;
import com.app.sbts.models.Driver;

import java.util.List;



public class DriverAdaptor extends RecyclerView.Adapter<DriverAdaptor.MyViewHolder> {

    private final Context sContext;
    private final List<Driver> sData;

    public DriverAdaptor(Context sContext, List<Driver> sData) {

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
