package com.example.sdapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class AddNewOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private MyEditText place_order_date,order_platform,shopname,issue_platform,contact,
            shopID,complimentary_items,pattern,requirements,payment_amount,refund_way,
            refund_date,repurchase_span;
    private Button commit_btn,return_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_order);
        place_order_date = (MyEditText)findViewById(R.id.place_order_date);
        order_platform = (MyEditText)findViewById(R.id.order_platform);
        shopname = (MyEditText)findViewById(R.id.shopname);
        issue_platform = (MyEditText)findViewById(R.id.issue_platform);
        contact = (MyEditText)findViewById(R.id.contact);
        shopID = (MyEditText)findViewById(R.id.shopID);
        complimentary_items = (MyEditText)findViewById(R.id.complimentary_items);
        pattern = (MyEditText)findViewById(R.id.pattern);
        requirements = (MyEditText)findViewById(R.id.requirements);
        payment_amount = (MyEditText)findViewById(R.id.payment_amount);
        refund_way = (MyEditText)findViewById(R.id.refund_way);
        refund_date = (MyEditText)findViewById(R.id.refund_date);
        repurchase_span = (MyEditText)findViewById(R.id.repurchase_span);
        commit_btn = (Button)findViewById(R.id.commit_btn);
        return_btn = (Button)findViewById(R.id.return_btn);
        commit_btn.setOnClickListener(this);
        return_btn.setOnClickListener(this);

        Intent intent = getIntent();
        order_platform.setText(intent.getStringExtra("order_platform"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        place_order_date.setText(simpleDateFormat.format(date));
        setfocus(shopname);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.commit_btn:
                JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("place_order_date",place_order_date.getText());
                    jsonObject.put("order_platform",order_platform.getText());
                    jsonObject.put("shopname",shopname.getText());
                    jsonObject.put("issue_platform",issue_platform.getText());
                    jsonObject.put("contact",contact.getText());
                    jsonObject.put("shopID",shopID.getText());
                    jsonObject.put("complimentary_items",complimentary_items.getText());
                    jsonObject.put("pattern",pattern.getText());
                    jsonObject.put("requirements",requirements.getText());
                    jsonObject.put("payment_amount",payment_amount.getText());
                    jsonObject.put("refund_way",refund_way.getText());
                    jsonObject.put("refund_date",refund_date.getText());
                    jsonObject.put("repurchase_span",repurchase_span.getText());
                }catch (JSONException e){
                    e.printStackTrace();
                }
                addOrder(jsonObject);
                break;
            case R.id.return_btn:
                finish();
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_to_right);
                break;
        }
    }

    private void addOrder(final JSONObject jsonObject){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpGetContext httpGetContext = new HttpGetContext();
                String res = httpGetContext.getData("http://47.98.239.237/SDAPP/php_file/add_order.php",jsonObject);
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 0:
                    Toast.makeText(AddNewOrderActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.slide_left_in,R.anim.slide_to_right);
                    break;

            }
            return false;
        }
    });

    private void setfocus(View view) {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }


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
}
