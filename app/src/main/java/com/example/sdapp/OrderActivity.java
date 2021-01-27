package com.example.sdapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener {

    private JsonRe jsonRe = new JsonRe();
    private HashMap<String, Object> order = null;
    private String oid;
    private MyEditText place_order_date,order_platform,shopname,issue_platform,contact,
            shopID,complimentary_items,pattern,requirements,payment_amount,refund_way,
            refund_date,repurchase_span,task_status,is_return_to_payaccount;
    private TextView place_order_date_tv,order_platform_tv,shopname_tv,issue_platform_tv,contact_tv,
            shopID_tv,complimentary_items_tv,pattern_tv,requirements_tv,payment_amount_tv,refund_way_tv,
            refund_date_tv,repurchase_span_tv,task_status_tv,is_return_to_payaccount_tv;
    private Button commit_btn,return_btn,update_btn,delete_btn;
    private ScrollView scrollView1,scrollView2;
    private boolean isEdit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
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
        task_status = (MyEditText)findViewById(R.id.task_status);
        is_return_to_payaccount = (MyEditText)findViewById(R.id.is_return_to_payaccount);

        place_order_date_tv = (TextView) findViewById(R.id.place_order_date_tv);
        order_platform_tv = (TextView)findViewById(R.id.order_platform_tv);
        shopname_tv = (TextView)findViewById(R.id.shopname_tv);
        issue_platform_tv = (TextView)findViewById(R.id.issue_platform_tv);
        contact_tv = (TextView)findViewById(R.id.contact_tv);
        shopID_tv = (TextView)findViewById(R.id.shopID_tv);
        complimentary_items_tv = (TextView)findViewById(R.id.complimentary_items_tv);
        pattern_tv = (TextView)findViewById(R.id.pattern_tv);
        requirements_tv = (TextView)findViewById(R.id.requirements_tv);
        payment_amount_tv = (TextView)findViewById(R.id.payment_amount_tv);
        refund_way_tv = (TextView)findViewById(R.id.refund_way_tv);
        refund_date_tv = (TextView)findViewById(R.id.refund_date_tv);
        repurchase_span_tv = (TextView)findViewById(R.id.repurchase_span_tv);
        task_status_tv = (TextView)findViewById(R.id.task_status_tv);
        is_return_to_payaccount_tv = (TextView)findViewById(R.id.is_return_to_payaccount_tv);

        scrollView1 = (ScrollView)findViewById(R.id.scrollView1);
        scrollView2 = (ScrollView)findViewById(R.id.scrollView2);

        commit_btn = (Button)findViewById(R.id.commit_btn);
        return_btn = (Button)findViewById(R.id.return_btn);
        update_btn = (Button)findViewById(R.id.update_btn);
        delete_btn = (Button)findViewById(R.id.delete_btn);
        commit_btn.setOnClickListener(this);
        return_btn.setOnClickListener(this);
        update_btn.setOnClickListener(this);
        delete_btn.setOnClickListener(this);

        Intent intent = getIntent();
        oid = intent.getStringExtra("oid");
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("oid",oid);
        }catch (JSONException e){
            e.printStackTrace();
        }
        getOrder(jsonObject);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.commit_btn:
                JSONObject jsonObject = new JSONObject();
                try{
                    jsonObject.put("oid",oid);
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
                    jsonObject.put("task_status",task_status.getText());
                    jsonObject.put("refund_way",refund_way.getText());
                    jsonObject.put("refund_date",refund_date.getText());
                    jsonObject.put("repurchase_span",repurchase_span.getText());
                    jsonObject.put("is_return_to_payaccount",is_return_to_payaccount.getText());
                }catch (JSONException e){
                    e.printStackTrace();
                }
//                Log.i("ccc",jsonObject.toString());
                updateOrder(jsonObject);
                Toast.makeText(OrderActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.return_btn:
                finish();
                overridePendingTransition(R.anim.slide_left_in,R.anim.slide_to_right);
                break;
            case R.id.update_btn:
                mHandler.sendEmptyMessage(1);
                break;
            case R.id.delete_btn:
                del_warning();
                break;
        }
    }

    private void getOrder(final JSONObject jsonObject){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpGetContext httpGetContext = new HttpGetContext();
                String res = httpGetContext.getData("http://47.98.239.237/SDAPP/php_file/query_order.php",jsonObject);
                order = jsonRe.orderData(res);
//                Log.i("ccc",order.toString());
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void updateOrder(final JSONObject jsonObject){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpGetContext httpGetContext = new HttpGetContext();
                String res = httpGetContext.getData("http://47.98.239.237/SDAPP/php_file/update_order.php",jsonObject);
                order = jsonRe.orderData(res);
//                Log.i("ccc",order.toString());
                mHandler.sendEmptyMessage(0);
                mHandler.sendEmptyMessage(1);
            }
        }).start();
    }

    private void deleteOrder(final JSONObject jsonObject){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpGetContext httpGetContext = new HttpGetContext();
                String res = httpGetContext.getData("http://47.98.239.237/SDAPP/php_file/delete_order.php",jsonObject);
            }
        }).start();
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 0:
                    place_order_date.setText(order.get("place_order_date").toString());
                    order_platform.setText(order.get("order_platform").toString());
                    shopname.setText(order.get("shopname").toString());
                    issue_platform.setText(order.get("issue_platform").toString());
                    contact.setText(order.get("contact").toString());
                    shopID.setText(order.get("shopID").toString());
                    complimentary_items.setText(order.get("complimentary_items").toString());
                    pattern.setText(order.get("pattern").toString());
                    requirements.setText(order.get("requirements").toString());
                    payment_amount.setText(order.get("payment_amount").toString());
                    refund_way.setText(order.get("refund_way").toString());
                    refund_date.setText(order.get("refund_date").toString());
                    repurchase_span.setText(order.get("repurchase_span").toString());
                    task_status.setText(order.get("task_status").toString());
                    is_return_to_payaccount.setText(order.get("is_return_to_payaccount").toString());

                    place_order_date_tv.setText(order.get("place_order_date").toString());
                    order_platform_tv.setText(order.get("order_platform").toString());
                    shopname_tv.setText(order.get("shopname").toString());
                    issue_platform_tv.setText(order.get("issue_platform").toString());
                    contact_tv.setText(order.get("contact").toString());
                    shopID_tv.setText(order.get("shopID").toString());
                    complimentary_items_tv.setText(order.get("complimentary_items").toString());
                    pattern_tv.setText(order.get("pattern").toString());
                    requirements_tv.setText(order.get("requirements").toString());
                    payment_amount_tv.setText(order.get("payment_amount").toString());
                    refund_way_tv.setText(order.get("refund_way").toString());
                    refund_date_tv.setText(order.get("refund_date").toString());
                    repurchase_span_tv.setText(order.get("repurchase_span").toString());
                    task_status_tv.setText(order.get("task_status").toString());
                    is_return_to_payaccount_tv.setText(order.get("is_return_to_payaccount").toString());
                    break;
                case 1:
                    if(isEdit){
                        update_btn.setText("编辑");
                        scrollView1.setVisibility(View.VISIBLE);
                        scrollView2.setVisibility(View.INVISIBLE);
                    }else{
                        update_btn.setText("取消编辑");
                        scrollView1.setVisibility(View.INVISIBLE);
                        scrollView2.setVisibility(View.VISIBLE);
                    }
                    isEdit = !isEdit;
                    break;
                case 2:
                    finish();
                    overridePendingTransition(R.anim.slide_left_in,R.anim.slide_to_right);
                    break;
            }
            return false;
        }
    });

    /**
     * 删除警告
     */
    private void del_warning(){
        SweetAlertDialog del_alert = new SweetAlertDialog(OrderActivity.this,SweetAlertDialog.WARNING_TYPE);
        del_alert.setTitleText("警告")
                .setContentText("确定删除该条数据吗？")
                .setConfirmText("是")
                .setCancelText("否")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        JSONObject jsonObject = new JSONObject();
                        try{
                            jsonObject.put("oid",oid);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        deleteOrder(jsonObject);
                        sweetAlertDialog.cancel();
                        mHandler.sendEmptyMessage(2);
                    }
                })
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.cancel();
                    }
                });
        del_alert.setCancelable(false);
        del_alert.show();
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
