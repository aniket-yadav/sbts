package com.app.sbts.adaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.sbts.databinding.BusItemBinding;
import com.app.sbts.databinding.DriverItemBinding;
import com.app.sbts.databinding.ParentItemBinding;
import com.app.sbts.databinding.RequestsItemBinding;
import com.app.sbts.models.Bus;
import com.app.sbts.models.Driver;
import com.app.sbts.models.Parent;

import java.util.List;



public class RequestsAdaptor extends RecyclerView.Adapter<RequestsAdaptor.MyViewHolder> {

    private final Context sContext;
    private final List<Parent> sData;

    public RequestsAdaptor(Context sContext, List<Parent> sData) {

        this.sContext = sContext;
        this.sData = sData;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyViewHolder(RequestsItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        myViewHolder.binding.parentName.setText(sData.get(i).getFull_Name());
        myViewHolder.binding.parentEmail.setText(sData.get(i).getEmail());
        myViewHolder.binding.parentMobile.setText(sData.get(i).getMobile_No1());
        myViewHolder.binding.studentName.setText(sData.get(i).getStudent_Name());

        myViewHolder.binding.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return sData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final RequestsItemBinding binding;

        public MyViewHolder(RequestsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
