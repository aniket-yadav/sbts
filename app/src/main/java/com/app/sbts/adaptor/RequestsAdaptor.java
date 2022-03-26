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
import com.app.sbts.R;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.BusItemBinding;
import com.app.sbts.databinding.DriverItemBinding;
import com.app.sbts.databinding.ParentItemBinding;
import com.app.sbts.databinding.RequestsItemBinding;
import com.app.sbts.models.Bus;
import com.app.sbts.models.Driver;
import com.app.sbts.models.Parent;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;


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

        String email = sData.get(i).getEmail();
        myViewHolder.binding.approve.setOnClickListener(v -> {
          StringRequest  stringRequest = new StringRequest(Request.Method.POST,  sContext.getString(R.string.approve_url),
                    response -> {
                        Toast.makeText(sContext, response, Toast.LENGTH_LONG).show();
                        if(response.equals("Approved and approval email sent")){
                            myViewHolder.binding.approve.setText(R.string.approved);
                        }else if(response.equals("Approved and failed to send email")){
                            myViewHolder.binding.approve.setText(R.string.approved);
                        }

                    }, error -> Toast.makeText(sContext, error.toString(), Toast.LENGTH_LONG).show()) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("email",email);
                    return params;
                }
            };
            SingletonClass.getInstance(sContext).addToRequestQueue(stringRequest);
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
