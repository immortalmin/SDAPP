package com.example.sdapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class OrderListAdapter extends BaseAdapter {

    List<HashMap<String, Object>> mdata;
    private LayoutInflater mInflater;//布局装载器对象

    public OrderListAdapter(Context context, List<HashMap<String, Object>> data) {
        this.mdata = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = mInflater.inflate(R.layout.orderitem,null);

        TextView shopname = (TextView)v.findViewById(R.id.shopname);
        TextView shopID = (TextView)v.findViewById(R.id.shopID);
        TextView place_order_date = (TextView)v.findViewById(R.id.place_order_date);
        TextView task_status = (TextView)v.findViewById(R.id.task_status);
        shopname.setText(mdata.get(position).get("shopname").toString());
        shopID.setText(mdata.get(position).get("shopID").toString());
        place_order_date.setText(mdata.get(position).get("place_order_date").toString());
        task_status.setText(mdata.get(position).get("task_status").toString());
        switch (mdata.get(position).get("task_status").toString()){
            case "已结算":
                task_status.setBackgroundColor(Color.parseColor("#787A78"));
                break;
            case "进行中":
                task_status.setBackgroundColor(Color.parseColor("#0EF017"));
                break;

        }
        return v;
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 0:

                    break;
            }
            return false;
        }
    });

    @Override
    //ListView需要显示的数据数量
    public int getCount() {
        return mdata.size();
    }

    @Override
    //指定的索引对应的数据项
    public Object getItem(int position) {
        return mdata.get(position);
    }

    @Override
    //指定的索引对应的数据项ID
    public long getItemId(int position) {
        return position;
    }
}
