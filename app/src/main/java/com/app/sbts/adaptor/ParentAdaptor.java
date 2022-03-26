package com.app.sbts.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.sbts.databinding.BusItemBinding;
import com.app.sbts.databinding.DriverItemBinding;
import com.app.sbts.databinding.ParentItemBinding;
import com.app.sbts.models.Bus;
import com.app.sbts.models.Driver;
import com.app.sbts.models.Parent;

import java.util.List;



public class ParentAdaptor extends RecyclerView.Adapter<ParentAdaptor.MyViewHolder> {

    private final Context sContext;
    private final List<Parent> sData;

    public ParentAdaptor(Context sContext, List<Parent> sData) {

        this.sContext = sContext;
        this.sData = sData;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyViewHolder(ParentItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        myViewHolder.binding.parentName.setText(sData.get(i).getFull_Name());
        myViewHolder.binding.parentEmail.setText(sData.get(i).getEmail());
        myViewHolder.binding.parentMobile.setText(sData.get(i).getMobile_No1());
        myViewHolder.binding.studentName.setText(sData.get(i).getStudent_Name());

    }

    @Override
    public int getItemCount() {
        return sData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final ParentItemBinding binding;

        public MyViewHolder(ParentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
