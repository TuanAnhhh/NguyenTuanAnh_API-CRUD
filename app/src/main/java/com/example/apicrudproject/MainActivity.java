package com.example.apicrudproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    List<User> listUser = new ArrayList<User>();
    ListView lvUser;
    public static String url = "https://60ad92df80a61f001733130c.mockapi.io/users";
    Button btnThem;
    EditText etName, etAge, etGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        handleAddUser();



    }
    void init(){
        lvUser = findViewById(R.id.lvUser);
        lvUser.setAdapter(new MyAdapter(this,listUser, R.layout.item));
        btnThem = findViewById(R.id.btnThem);
        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etGender = findViewById(R.id.etGender);
        GetArrayJson(url);
    }
    private void GetArrayJson(String url){
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET,url,null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i=0; i<response.length(); i++){
                                    try {
                                        JSONObject object = (JSONObject) response.get(i);
                                        User u = new User();
                                        u.setId(object.getInt("id"));
                                        u.setName(object.getString("name"));
                                        u.setAge(object.getInt("age"));
                                        u.setGender(object.getBoolean("gender"));

                                        listUser.add(u);
                                        lvUser.setAdapter(new MyAdapter(MainActivity.this,listUser, R.layout.item));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error by get Json Array!", Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
    public void handleAddUser(){
        btnThem.setOnClickListener(v-> {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("name", etName.getText().toString());
                jsonBody.put("age", Integer.parseInt(etAge.getText().toString()));
                jsonBody.put("gender", etGender.getText().toString().equalsIgnoreCase("Nam")? true: false);

                final String requestBody = jsonBody.toString();


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            GetArrayJson(url);
                            lvUser.setAdapter(new MyAdapter(MainActivity.this,listUser, R.layout.item));
                            Toast.makeText(MainActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();

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