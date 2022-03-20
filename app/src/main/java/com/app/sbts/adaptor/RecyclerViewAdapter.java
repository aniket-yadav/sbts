package com.app.sbts.adaptor;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.app.sbts.R;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.StudentRowItemBinding;
import com.app.sbts.models.Student;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private final Context sContext;
    private final List<Student> sData;
    AlertDialog.Builder builder;

    public RecyclerViewAdapter(Context sContext, List<Student> sData, AlertDialog.Builder builder) {

        this.sContext = sContext;
        this.sData = sData;
        this.builder = builder;

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
        Glide
                .with(sContext)
                .load(sData.get(i).getPhoto())
                .centerCrop()
                .into(myViewHolder.binding.thumbnail);


        myViewHolder.binding.container.setOnClickListener(v -> {
            String name = ((TextView) v.findViewById(R.id.student_name)).getText().toString();
            final String rollno = ((TextView) v.findViewById(R.id.student_roll_no)).getText().toString();

            builder.setMessage("Is " + name + " present today ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", (dialog, which) -> {


                String url = sContext.getString(R.string.Mark_Present_URL);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        response -> Toast.makeText(sContext, response, Toast.LENGTH_LONG).show(), error -> {
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("rollno", rollno);
                        params.put("isPresent", "true");
                        params.put("name",name);
                        return params;
                    }
                };
                SingletonClass.getInstance(sContext.getApplicationContext()).addToRequestQueue(stringRequest);


            });
            builder.setNeutralButton("Cancel", (dialog, which) -> {
            });

            AlertDialog alert = builder.create();
            alert.setTitle("Attendance");

            alert.show();
        });
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
