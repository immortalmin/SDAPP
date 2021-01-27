package com.example.sdapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button taobao_btn,jingdong_btn,pinduoduo_btn;
    private BlurImageView blurImageView = new BlurImageView();
    private int screen_width,screen_height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        add_btn = (Button)findViewById(R.id.add_btn);
        taobao_btn = (Button)findViewById(R.id.taobao_btn);
        jingdong_btn = (Button)findViewById(R.id.jingdong_btn);
        pinduoduo_btn = (Button)findViewById(R.id.pinduoduo_btn);
//        add_btn.setOnClickListener(this);
        taobao_btn.setOnClickListener(this);
        jingdong_btn.setOnClickListener(this);
        pinduoduo_btn.setOnClickListener(this);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        screen_width = metric.widthPixels;     // 屏幕宽度（像素）
        screen_height = metric.heightPixels;   // 屏幕高度（像素）
        mHandler.obtainMessage(1).sendToTarget();
    }

    public void onClick(View view) {
        switch (view.getId()){
//            case R.id.add_btn:
//                Intent intent = new Intent(MainActivity.this,AddNewOrderActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_to_left);
//                break;
            case R.id.taobao_btn:
                Intent intent = new Intent(MainActivity.this,OrdersActivity.class);
                intent.putExtra("order_platform","淘宝");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_to_left);
                break;
            case R.id.jingdong_btn:
                intent = new Intent(MainActivity.this,OrdersActivity.class);
                intent.putExtra("order_platform","京东");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_to_left);
                break;
            case R.id.pinduoduo_btn:
                intent = new Intent(MainActivity.this,OrdersActivity.class);
                intent.putExtra("order_platform","拼多多");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_right_in,R.anim.slide_to_left);
                break;
        }
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what){
                case 0:
                    ArrayList<Bitmap> bitmaps = (ArrayList<Bitmap>) message.obj;
                    taobao_btn.setBackground(new BitmapDrawable(bitmaps.get(0)));
                    jingdong_btn.setBackground(new BitmapDrawable(bitmaps.get(1)));
                    pinduoduo_btn.setBackground(new BitmapDrawable(bitmaps.get(2)));
//                    ArrayList<Drawable> drawables = (ArrayList<Drawable>) message.obj;
//                    btn_wordlist.setBackground(drawables.get(0));
//                    btn_recite.setBackground(drawables.get(1));
//                    btn_spell.setBackground(drawables.get(2));
//                    btn_test.setBackground(drawables.get(3));
                    break;
                case 1:
                    Resources res = getResources();
                    Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.background);
                    cropBitmap(bmp);
                    break;
            }
            return false;
        }
    });
    /**
     * 图片裁剪
     * @param bitmap
     * @return
     */
    private void cropBitmap(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        Double ratio = w/Double.valueOf(screen_width);
//        int btn_h = btn_wordlist.getHeight();
//        int btn_w = btn_wordlist.getWidth();
        int btn_h = 180;
        int btn_w = 750;
        //half margin
        int MarginAndBtn_h = (int)((btn_h+60)*ratio);
        int MarginAndBtn_w = (int)((btn_w+60)*ratio);
        int justMargin = (int)(60*ratio);
        int justBtn_h = (int)(btn_h*ratio);
        int justBtn_w = (int)(btn_w*ratio);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, w / 2-justBtn_w/2, h/2-justBtn_h*3/2-justMargin,justBtn_w, justBtn_h, null, false);
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap, w / 2-justBtn_w/2, h/2-justBtn_h/2,justBtn_w, justBtn_h, null, false);
        Bitmap bitmap3 = Bitmap.createBitmap(bitmap, w / 2-justBtn_w/2, h/2+justBtn_h/2+justMargin,justBtn_w, justBtn_h, null, false);
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.add(getRoundedCornerBitmap(blurImageView.BoxBlurFilter(bitmap1),80));
        bitmaps.add(getRoundedCornerBitmap(blurImageView.BoxBlurFilter(bitmap2),80));
        bitmaps.add(getRoundedCornerBitmap(blurImageView.BoxBlurFilter(bitmap3),80));

//        ArrayList<Drawable> drawables = new ArrayList<>();
//        drawables.add(blurImageView.BoxBlurFilter(getRoundedCornerBitmap(bitmap1,100)));
//        drawables.add(blurImageView.BoxBlurFilter(getRoundedCornerBitmap(bitmap2,100)));
//        drawables.add(blurImageView.BoxBlurFilter(getRoundedCornerBitmap(bitmap3,100)));
//        drawables.add(blurImageView.BoxBlurFilter(getRoundedCornerBitmap(bitmap4,100)));
        mHandler.obtainMessage(0,bitmaps).sendToTarget();
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundPx){

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
