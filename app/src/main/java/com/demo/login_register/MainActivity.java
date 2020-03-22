package com.demo.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.demo.login_register.Constance.ADDRESS;
import static com.demo.login_register.Constance.CONTACT_NO;
import static com.demo.login_register.Constance.EMAIL_ID;
import static com.demo.login_register.Constance.PASSWORD;
import static com.demo.login_register.Constance.SHARED_PREFERENCES_NAME;
import static com.demo.login_register.Constance.USER_ID;
import static com.demo.login_register.Constance.USER_NAME;

public class MainActivity extends AppCompatActivity {
    EditText name, pwd, address, email, phone;
    Button btn_submit;
    String str_name, str_pwd, str_address, str_email, str_phone;
    RequestQueue requestQueue;
    String HttpUrl = "http://192.168.64.2/test_json/login/register_login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView txt = findViewById(R.id.go_login);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                finish();
                startActivity(intent);
            }
        });

        name = findViewById(R.id.e_name);
        pwd = findViewById(R.id.e_pwd);
        address = findViewById(R.id.e_address);
        email = findViewById(R.id.e_email);
        phone = findViewById(R.id.e_phone);

        btn_submit = findViewById(R.id.submit_btn);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {

                    str_name = name.getText().toString().trim();
                    str_pwd = pwd.getText().toString().trim();
                    str_address = address.getText().toString().trim();
                    str_email = email.getText().toString().trim();
                    str_phone = phone.getText().toString().trim();
                    FormData();


                } else {

                    Toast.makeText(MainActivity.this, "You are not connected to Internet", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    private void FormData() {
        //String tag_json_obj = "json_string_req";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HttpUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {
                        // Hiding the progress dialog after all task complete.
                        showJason(ServerResponse);
                        Intent intent = new Intent(MainActivity.this, Home.class);
                        finish();
                        startActivity(intent);
                        // Showing response message coming from server.
                        Toast.makeText(MainActivity.this, ServerResponse, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Toast.makeText(MainActivity.this, volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params.
                params.put("name", str_name);
                params.put("password", str_pwd);
                params.put("address", str_address);
                params.put("email", str_email);
                params.put("phone", str_phone);

                Log.d("_POST_PARAMS", "" + params);

                return params;
            }
        };

        //AppController.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Creating RequestQueue.
        requestQueue = Volley.newRequestQueue(MainActivity.this);

        // Adding the StringRequest object into requestQueue.
        requestQueue.add(stringRequest);

    }

    private void showJason(String serverResponse) {
        try {

            Log.d("check_response1", "" + serverResponse);

            JSONObject jsonObject = new JSONObject(serverResponse);
            String check = jsonObject.getString("response");

            JSONObject innerObj = new JSONObject(check);

            String user_id = innerObj.getString("user_id");
            String name = innerObj.getString("name");
            String phone = innerObj.getString("phone");
            String email = innerObj.getString("email");
            String address = innerObj.getString("address");

            Log.d("check_response", "" + check);
            Log.d("email", "" + email);

            if (!check.equalsIgnoreCase("faliure")) {

                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(USER_ID, user_id);
                editor.putString(PASSWORD, "");
                editor.putString(USER_NAME, name);
                editor.putString(CONTACT_NO, phone);
                editor.putString(EMAIL_ID, email);
                editor.putString(ADDRESS, address);
                editor.commit();

                Toast.makeText(this, "saved_successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Home.class);
                finish();
                startActivity(intent);
                Log.d("sdd", "" + user_id);



            } else {
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
