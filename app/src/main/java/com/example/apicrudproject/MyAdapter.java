package com.example.apicrudproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    final List<User> listUser;
    private Context context;
    int layout;

    public MyAdapter( Context context,List<User> listUser, int layout) {
        this.listUser = listUser;
        this.context = context;

        this.layout = layout;
    }

    @Override
    public int getCount() {
        return listUser.size();
    }

    @Override
    public Object getItem(int i) {
        return listUser.get(i);
    }

    @Override
    public long getItemId(int i) {
        return listUser.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1;
        if (view == null){
            view1 = View.inflate(viewGroup.getContext(), R.layout.item, null);
        }else view1 = view;

        User city = (User) getItem(i);
        TextView tvName = view1.findViewById(R.id.tvName);
        TextView tvAge = view1.findViewById(R.id.tvAge);
        TextView tvGender = view1.findViewById(R.id.tvGioiTinh);
        ImageView ivRemove = view1.findViewById(R.id.ivRemove);
        ImageView ivEdit = view1.findViewById(R.id.ivEdit);

        int id = listUser.get(i).getId();

        tvName.setText(city.getName());
        tvAge.setText(String.valueOf(city.getAge()));
        tvGender.setText(city.isGender()? "Nam":"Nữ");

        ivRemove.setOnClickListener(v -> {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.DELETE, MainActivity.url + '/' + id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(v.getContext(), "Xóa thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    context.startActivity(intent);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(v.getContext(), "Error by Post data!", Toast.LENGTH_SHORT).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
            requestQueue.add(stringRequest);
        });

        ivEdit.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), UserUpdateActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("ID",city.getId());
            bundle.putString("NAME",city.getName());
            bundle.putInt("AGE",city.getAge());
            bundle.putBoolean("GENDER",city.isGender());
            intent.putExtra("BUNDLE",bundle);
            context.startActivity(intent);
        });
        return view1;
    }
}
