package com.yeohe.runninglightapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.holidaycheck.permissify.PermissifyActivity;
import com.holidaycheck.permissify.PermissifyManager;

/**
 * Created by Administrator on 2017/12/26.
 */

public class H_RunLightActivity extends PermissifyActivity {

    private LinearLayout bg_layout;
    private TextView tv_marguee;
    private String text;

    private Camera mCamera = null;
    private  SharedPreferenceUtil spUtils=SharedPreferenceUtil.getInstance();
    public  boolean isFlashLight = spUtils.getKeyIsFlash(); // 定义开关状态，状态为false，打开状态，状态为true，关闭状态
    public  boolean isShanScreen=spUtils.getKeyIsShan();//屏幕亮度调节

    private static final int CAMERA_PERMISSION_REQUEST_ID = 2;

    private static CameraManager manager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_h_runlight);

        Toast.makeText(this,"屏幕是否闪烁:"+isShanScreen,Toast.LENGTH_LONG).show();

        manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        if(Build.VERSION.SDK_INT>=23) {
            //申请权限
            getPermissifyManager().callWithPermission(this, CAMERA_PERMISSION_REQUEST_ID, Manifest.permission.CAMERA);
        }else {
            mCamera = Camera.open();
            flashThread.start();
            ShanThread.start();
            setWindowBrightness(255);
        }

        text=getIntent().getStringExtra("text");
        intiTextView();//初始化
    }

    public void intiTextView() {
        bg_layout=findViewById(R.id.bg_layout);
        bg_layout.setBackgroundColor(SharedPreferenceUtil.getInstance().getKeyScreenColor());

        tv_marguee = findViewById(R.id.tv_marquee);
        tv_marguee.setTextColor(SharedPreferenceUtil.getInstance().getKeyTextColor());
        if(text!=null)
            tv_marguee.setText(text);
        tv_marguee.setSingleLine(true);//设置单行显示
        tv_marguee.setHorizontallyScrolling(true);//设置水平滚动效果
        tv_marguee.setMarqueeRepeatLimit(-1);//设置滚动次数，-1为无限滚动，1为滚动1次
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
    @Override
    public void onCallWithPermissionResult(int callId, PermissifyManager.CallRequestStatus status) {
        super.onCallWithPermissionResult(callId, status);

        if (callId == CAMERA_PERMISSION_REQUEST_ID) {
            switch (status) {
                case PERMISSION_GRANTED:
                    //getUserLocation();
                    mCamera = Camera.open();
                    flashThread.start();
                    ShanThread.start();
                    setWindowBrightness(255);
                    break;
                case PERMISSION_DENIED_ONCE:
                    //do some custom logic
                    break;
                case PERMISSION_DENIED_FOREVER:
                    //do some custom logic
                case SHOW_PERMISSION_RATIONALE:
                    //do some custom logic
            }
        }
    }

}
