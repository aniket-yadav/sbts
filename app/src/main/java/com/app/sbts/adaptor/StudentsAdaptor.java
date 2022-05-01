package com.app.sbts.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.StudentRowItemBinding;
import com.app.sbts.models.Student;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StudentsAdaptor extends RecyclerView.Adapter<StudentsAdaptor.MyViewHolder> {

    private final Context sContext;
    private final List<Student> sData;

    public StudentsAdaptor(Context sContext, List<Student> sData) {

        this.sContext = sContext;
        this.sData = sData;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        return new MyViewHolder(StudentRowItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {


        myViewHolder.binding.studentName.setText(sData.get(i).getName());
        myViewHolder.binding.studentDivision.setText(sData.get(i).getDivision());
        myViewHolder.binding.studentRollNo.setText(sData.get(i).getRoll_no());
        myViewHolder.binding.studentClass.setText(sData.get(i).getS_class());
        myViewHolder.binding.delete.setVisibility(View.VISIBLE);
        myViewHolder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url  =  "https://sbts2022.000webhostapp.com/api/delete_student.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST,  url,
                        response -> {
                            Toast.makeText(sContext, response, Toast.LENGTH_LONG).show();
                        }, error -> Toast.makeText(sContext, error.toString(), Toast.LENGTH_LONG).show()) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", sData.get(myViewHolder.getAdapterPosition()).getEmail());
                        return params;
                    }
                };
                SingletonClass.getInstance(sContext).addToRequestQueue(stringRequest);
            }
        });
        Glide
                .with(sContext)
                .load(sData.get(i).getPhoto())
                .centerCrop()
                .into(myViewHolder.binding.thumbnail);


    }

    @Override
    public int getItemCount() {
        return sData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final StudentRowItemBinding binding;

        public MyViewHolder(StudentRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
