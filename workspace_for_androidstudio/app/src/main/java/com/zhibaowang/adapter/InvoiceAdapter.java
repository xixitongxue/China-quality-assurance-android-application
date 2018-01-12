package com.zhibaowang.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhibaowang.app.R;
import com.zhibaowang.model.Bill;

import java.util.ArrayList;

/**
 * Created by zhaoyuntao on 2017/12/25.
 */

public class InvoiceAdapter extends BaseAdapter {
    public ArrayList<Bill>arrayList;
    public InvoiceAdapter(ArrayList<Bill>arrayList){
        this.arrayList=arrayList;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Bill getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_invoice,null);
            TextView textView=convertView.findViewById(R.id.textview_item_listview_invoice);
            viewHolder.textView=textView;
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(arrayList.get(position).getJson());
        return convertView;
    }
    ViewHolder viewHolder;
    class ViewHolder{
        TextView textView;
    }
}
