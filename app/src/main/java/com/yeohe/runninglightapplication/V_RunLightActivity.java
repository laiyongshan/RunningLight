package com.yeohe.runninglightapplication;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/12/26.
 */

public class V_RunLightActivity extends AppCompatActivity {
    private ScrollView sc;
    private VerticalScrollTextView tv_marguee;
    private String msg;

    private Camera mCamera = null;
    private  SharedPreferenceUtil spUtils=SharedPreferenceUtil.getInstance();
    public  boolean isFlashLight = spUtils.getKeyIsFlash(); // 定义开关状态，状态为false，打开状态，状态为true，关闭状态
    public  boolean isShanScreen=spUtils.getKeyIsShan();//屏幕亮度调节

    private static CameraManager manager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_o_runlight);

        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        mCamera = Camera.open();
        flashThread.start();
        ShanThread.start();


        msg=getIntent().getStringExtra("text");

        intiTextView();
    }

    public void intiTextView() {
        tv_marguee = findViewById(R.id.tv_marquee);
        tv_marguee.setTextColor(SharedPreferenceUtil.getInstance().getKeyTextColor());

        sc=findViewById(R.id.scrollView);
        sc.setBackgroundColor(SharedPreferenceUtil.getInstance().getKeyScreenColor());

        if(msg!=null)
            tv_marguee.setText(msg);
//        tv_marguee.setMarqueeRepeatLimit(-1);//设置滚动次数，-1为无限滚动，1为滚动1次
    }



    @Override
    protected void onResume() {
        super.onResume();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        flashThread.interrupt();
        ShanThread.interrupt();
        isStop=true;
        if(mCamera!=null)
            mCamera.release();
    }

    volatile boolean isStop=false;
    Thread flashThread=new Thread(new Runnable() {
        @Override
        public void run() {
            while (!isStop&&isFlashLight) {
                FlashLightUtil.shanFlashLight(manager,mCamera, SharedPreferenceUtil.getInstance().getKeyHz());
            }
        }
    });


    int i;
    Thread ShanThread=new Thread(new Runnable() {
        @Override
        public void run() {
            while (!isStop&&isShanScreen)    {
                try {
                    Message msg=new Message();
                    i+=1;
                    msg.what=i;
                    mHandle.sendMessage(msg);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }


    private void setWindowBrightness(int brightness) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }

    Handler mHandle=new Handler(){
      @Override
        public  void handleMessage(Message msg){
          if(msg.what%2==0)
             setWindowBrightness(255);
          else
             setWindowBrightness(10);
      }

    };

}
