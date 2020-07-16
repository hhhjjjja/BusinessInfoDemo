package com.hustar.businessinfodemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    LayoutInflater inflater = null;
    private ArrayList<ListItem> mData = null;
    private int listCnt = 0;

    public ListAdapter(ArrayList<ListItem> mData) {
        this.mData = mData;
        this.listCnt = this.mData.size();
    }

    @Override
    public int getCount() {
        return listCnt;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            final Context context = viewGroup.getContext();
            if(inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            view = inflater.inflate(R.layout.listview_item, viewGroup, false);
        }
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.imageView2);

        tvName.setText(mData.get(i).getName());

        String sector = mData.get(i).getSecor();
        if(sector.equals("한식")) {
            imageView2.setImageResource(R.drawable.rice_icon);
        } else if(sector.equals("중식")) {
            imageView2.setImageResource(R.drawable.dimsum_icon);
        } else if(sector.equals("일식")) {
            imageView2.setImageResource(R.drawable.sushi_icon);
        } else if(sector.equals("분식")) {
            imageView2.setImageResource(R.drawable.hotdog_icon);
        } else if(sector.equals("양식")) {
            imageView2.setImageResource(R.drawable.pizza_icon);
        } else if(sector.equals("이미용업")) {
            imageView2.setImageResource(R.drawable.scissor_icon);
        } else if(sector.equals("세탁업")) {
            imageView2.setImageResource(R.drawable.laundry_icon);
        } else if(sector.equals("목욕업")) {
            imageView2.setImageResource(R.drawable.bath_icon);
        }

        return view;
    }
}
