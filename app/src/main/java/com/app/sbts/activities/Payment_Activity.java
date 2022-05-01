package com.app.sbts.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.app.sbts.R;
import com.app.sbts.classes.SessionManager;
import com.app.sbts.classes.SingletonClass;
import com.app.sbts.databinding.ActivityPaymentBinding;
import com.bumptech.glide.Glide;
import com.cashfree.pg.CFPaymentService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;


public class Payment_Activity extends AppCompatActivity {

    ActivityPaymentBinding binding;
    String TAG = "PAYMENT";
    double amount = 0.0;
    SessionManager sessionManager;

    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        sessionManager = new SessionManager(this);

       if( !sessionManager.isLoggedIn()){
           startActivity(new Intent(Payment_Activity.this,LoginActivity.class));
           finish();
       }
        Uri data = intent.getData();
        amount = Double.parseDouble(data.getLastPathSegment());
        Log.d(TAG,String.valueOf( amount));

        Objects.requireNonNull(binding.busCharge).setText(String.valueOf(amount));
        binding.pay.setOnClickListener(payClickListener);
    }
    View.OnClickListener payClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            binding.loadingAttendeeList.setVisibility(View.VISIBLE);
int orderId = new Random().nextInt();
            JSONObject body = new JSONObject();
            try {
                body.put("orderId", String.valueOf(orderId));
                body.put("orderAmount", amount);
                body.put("orderCurrency", "INR");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            String url = "https://test.cashfree.com/api/v2/cftoken/order";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,body, response -> {
                Log.d(TAG, response.toString());
                String token = "";
                try {
                    token = response.getString("cftoken");
                }catch (JSONException e){
                    e.printStackTrace();
                }

                if(token.isEmpty()){
                    return;
                }
                HashMap<String,String> params = new HashMap<String,String>();
            params.put(CFPaymentService.PARAM_APP_ID,"160073684d17e325cb18d7b158370061");
            params.put(CFPaymentService.PARAM_ORDER_ID,String.valueOf(orderId));
            params.put(CFPaymentService.PARAM_ORDER_AMOUNT,String.valueOf(amount));
            params.put(CFPaymentService.PARAM_CUSTOMER_EMAIL,"abc@gmail.com");
            params.put(CFPaymentService.PARAM_CUSTOMER_PHONE,"2845283946");
            params.put(CFPaymentService.PARAM_CUSTOMER_NAME,"Dummy Doe");
            CFPaymentService.getCFPaymentServiceInstance().doPayment(Payment_Activity.this,params,token,"TEST");
                binding.loadingAttendeeList.setVisibility(View.GONE);
            }, error -> {
                binding.loadingAttendeeList.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("x-client-id", "160073684d17e325cb18d7b158370061");
                    headers.put("x-client-secret", "414934a7a49ca8d107031a2820656cac28e7b024");
                    return headers;
                }
            };
            SingletonClass.getInstance(getApplicationContext()).addToRequestQueue(request);
        }
    };

    @Override
    protected  void  onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Same request code for all payment APIs.
        Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
        Log.d(TAG, "API Response : ");
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle  bundle = data.getExtras();
           String status =  bundle.getString("txStatus");
           if(status.equalsIgnoreCase("SUCCESS")){
               Toast.makeText(Payment_Activity.this,"Payment Successful",Toast.LENGTH_LONG ).show();
              StringRequest stringRequest = new StringRequest(Request.Method.POST,  "https://sbts2022.000webhostapp.com/api/payment_mail.php",
                       response -> {
                           Toast.makeText(Payment_Activity.this,response,Toast.LENGTH_LONG).show();

                       }, error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show()) {
                   @Override
                   protected Map<String, String> getParams() {
                       Map<String, String> params = new HashMap<>();
                       params.put("email", Objects.requireNonNull(sharedPreferences.getString("Email", "NULL")));
                       params.put("amount", String.valueOf(amount));
                       return params;
                   }
               };
               SingletonClass.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
               finish();
           }else {
               Toast.makeText(Payment_Activity.this,status,Toast.LENGTH_LONG ).show();
           }
            if (bundle != null)
                for (String  key  :  bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        Log.d(TAG, key + " : " + bundle.getString(key));
                    }
                }
        }
    }
}