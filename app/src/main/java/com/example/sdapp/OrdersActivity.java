package com.example.sdapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class OrdersActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView listview1;
    private JsonRe jsonRe = new JsonRe();
    private List<HashMap<String, Object>> orders = new ArrayList<>();
    private OrderListAdapter orderListAdapter = null;
    private String order_platform="淘宝";
    private TextView textView;
    private Button return_btn,add_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        listview1 = (ListView)findViewById(R.id.listview1);
        textView = (TextView)findViewById(R.id.textView);
        return_btn = (Button)findViewById(R.id.return_btn);
        add_btn = (Button)findViewById(R.id.add_btn);
        return_btn.setOnClickListener(this);
        add_btn.setOnClickListener(this);

        listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(OrdersActivity.this, OrderActivity.class);
                intent.putExtra("oid",orders.get(position).get("oid").toString());
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_to_left);
            }
        });

        Intent intent = getIntent();
        order_platform = intent.getStringExtra("order_platform");
        textView.setText(order_platform);
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("order_platform",order_platform);
        }catch (JSONException e){
            e.printStackTrace();
        }
        getOrders(jsonObject);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.return_btn:
                finish();
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_to_right);
                break;
            case R.id.add_btn:
                Intent intent = new Intent(OrdersActivity.this,AddNewOrderActivity.class);
                intent.putExtra("order_platform",order_platform);
                startActivityForResult(intent,1);
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_to_left);
                break;

        }
    }

    private void getOrders(final JSONObject jsonObject){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpGetContext httpGetContext = new HttpGetContext();
                String res = httpGetContext.getData("http://47.98.239.237/SDAPP/php_file/query_orders.php",jsonObject);
                orders.clear();
                orders.addAll(jsonRe.ordersData(res));
//                Log.i("ccc",orders.toString());
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 0:
                    if(orderListAdapter==null){
                        orderListAdapter = new OrderListAdapter(OrdersActivity.this,orders);
                        listview1.setAdapter(orderListAdapter);
                    }else{
                        orderListAdapter.notifyDataSetChanged();
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
            overridePendingTransition(R.anim.slide_left_in,R.anim.slide_to_right);
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //此处可以根据两个Code进行判断，本页面和结果页面跳过来的值
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("order_platform",order_platform);
        }catch (JSONException e){
            e.printStackTrace();
        }
        getOrders(jsonObject);
    }
}
