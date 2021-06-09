package com.example.apicrudproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class UserUpdateActivity extends AppCompatActivity {
    EditText etName, etAge, etGender;
    Button btnUpdate;
    private  int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form_update);

        etName = findViewById(R.id.etName_form);
        etAge = findViewById(R.id.etAge_form);
        etGender = findViewById(R.id.etGender_form);
        btnUpdate = findViewById(R.id.btnUpdate_form);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("BUNDLE");
//      Hiển thị thông tin ở giao diện sửa
        if(bundle != null){
            etName.setText(bundle.getString("NAME"));
            etAge.setText(String.valueOf(bundle.getInt("AGE")));
            etGender.setText(bundle.getBoolean("GENDER")? "Nam":"Nữ");
            id = bundle.getInt("ID");

        }

        btnUpdate.setOnClickListener(v-> {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(UserUpdateActivity.this);
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("name", etName.getText().toString());
                jsonBody.put("age", Integer.parseInt(etAge.getText().toString()));
                jsonBody.put("gender", etGender.getText().toString().equalsIgnoreCase("Nam")? true: false);

                final String requestBody = jsonBody.toString();


                StringRequest stringRequest = new StringRequest(Request.Method.PUT, MainActivity.url+ '/' + id, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        v.getContext().startActivity(intent);
                        Toast.makeText(v.getContext(), "Update thành công", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);
                            // can get more details such as response.headers
                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                };

                requestQueue.add(stringRequest);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

    }
}
