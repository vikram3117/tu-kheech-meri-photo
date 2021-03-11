package com.example.uploadphoto;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<User> arr;
    LayoutInflater okay;
    CustomAdapter(Context context,ArrayList<User> arr)
    {
        this.context=context;
        this.arr=arr;
        okay= LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return arr.size();
    }

    @Override
    public Object getItem(int position) {
        return arr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int i, View convertView, ViewGroup parent)
    {
        convertView = okay.inflate(R.layout.my_layout,null);
        ImageView img=(ImageView) convertView.findViewById(R.id.img);
        TextView link=(TextView) convertView.findViewById(R.id.link);
        /*--img.setImageURI(""+arr.get(i).link);*/
        link.setText(""+arr.get(i).link);
        return convertView;
    }
}
