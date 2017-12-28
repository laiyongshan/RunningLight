package com.yeohe.runninglightapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rey.material.widget.FloatingActionButton;
import com.rey.material.widget.RadioButton;
import com.rey.material.widget.Switch;

import cn.qqtheme.framework.picker.ColorPicker;

public class MainActivity extends AppCompatActivity  {

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;

    private EditText et;
    private TextView count_tv;

    private RelativeLayout ziti_color_layout;
    private ImageView ziti_color_iv;
    private ImageView pingmu_color_iv;

    private LinearLayout hz_layout;
    private EditText hz_et;
    private Switch isFlash_sw2;

    private Switch isShan_sw1;
    private boolean isShan;//记录屏幕是否闪烁


    RadioButton rb1;
    RadioButton rb2;

    private FloatingActionButton run_fab;

    private static SharedPreferenceUtil spUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spUtils=SharedPreferenceUtil.getInstance();

       initToolBar();
       initViews();
    }

    //
    private void initToolBar(){
        //设置Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);//决定左上角的图标是否可以点击
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//决定左上角图标的右侧是否有向左的小箭头
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(view);
            }
        });
        mToolbar.setTitle("跑马溜溜");

    }

    Intent intent;
    //初始化控件
    private void initViews(){
        rb1 = (RadioButton)findViewById(R.id.switches_rb1);
        rb2 = (RadioButton)findViewById(R.id.switches_rb2);
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    rb1.setChecked(rb1 == buttonView);
                    rb2.setChecked(rb2 == buttonView);
                }
            }
        };
        rb1.setOnCheckedChangeListener(listener);
        rb2.setOnCheckedChangeListener(listener);


        count_tv=findViewById(R.id.count_tv);

        et=findViewById(R.id.et);//编写框
        et.setText(SharedPreferenceUtil.getInstance().getTextData()+"");
        count_tv.setText(et.getText().length()+"/25");
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                count_tv.setText(et.getText().toString().length()+"/25");
            }
        });

        ziti_color_layout=findViewById(R.id.ziti_color_layout);

        pingmu_color_iv=findViewById(R.id.pingmu_color_iv);
        pingmu_color_iv.setBackgroundColor(SharedPreferenceUtil.getInstance().getKeyScreenColor());
        ziti_color_iv=findViewById(R.id.ziti_color_iv);
        ziti_color_iv.setBackgroundColor(SharedPreferenceUtil.getInstance().getKeyTextColor());

        isShan_sw1=findViewById(R.id.isShan_sw1);//屏幕闪烁否
        isShan_sw1.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked){
                    isShan=true;
                }else{
                    isShan=false;
                }
            }
        });

        hz_layout=findViewById(R.id.hz_layout);
        hz_et=findViewById(R.id.hz_et);
        hz_et.setText(SharedPreferenceUtil.getInstance().getKeyHz()+"");
        isFlash_sw2=findViewById(R.id.isFlash_sw2);
        isFlash_sw2.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                if(checked){
                    hz_layout.setVisibility(View.VISIBLE);
                }else{
                    hz_layout.setVisibility(View.INVISIBLE);
                }
            }
        });


        run_fab=findViewById(R.id.run_fab);
        run_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb1.isChecked()) {
                    intent=new Intent(MainActivity.this, H_RunLightActivity.class);
                }else if(rb2.isChecked()){
                    intent=new Intent(MainActivity.this, V_RunLightActivity.class);
                }

                if(!et.getText().toString().trim().equals("")){

                    spUtils.setTextData(et.getText().toString());
                    spUtils.setKeyIsFlash(isFlash_sw2.isChecked());
                    spUtils.setKeyIsShan(isShan);

                    if(!hz_et.getText().toString().trim().equals(""))
                        spUtils.setKeyHz(Integer.valueOf(hz_et.getText().toString()));

                    intent.putExtra("text",et.getText().toString());
                    startActivity(intent);
                    finish();
                }else{
                    SnackBarUtils.makeShort(view,"输入文字为空！").show();
                }
            }
        });

    }


    //选择字体颜色
    public void chooseZitiColor(final View view){
        ColorPicker picker = new ColorPicker(this);
        picker.setInitColor(0xFFDD00DD);
        picker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
            @Override
            public void onColorPicked(int pickedColor) {
//                SnackBarUtils.makeShort(view,ConvertUtils.toColorString(pickedColor));
//                Toast.makeText(MainActivity.this,ConvertUtils.toColorString(pickedColor),Toast.LENGTH_LONG).show();
                ziti_color_iv.setBackgroundColor(pickedColor);
                spUtils.setKeyTextColor(pickedColor);
            }
        });
        picker.show();
    }


    //选择屏幕背景颜色
    public void choosePinmuColor(View view){
        ColorPicker picker = new ColorPicker(this);
        picker.setInitColor(0xFFDD00DD);
        picker.setOnColorPickListener(new ColorPicker.OnColorPickListener() {
            @Override
            public void onColorPicked(int pickedColor) {
//                SnackBarUtils.makeShort(view,ConvertUtils.toColorString(pickedColor));
//                Toast.makeText(MainActivity.this,ConvertUtils.toColorString(pickedColor),Toast.LENGTH_LONG).show();
                pingmu_color_iv.setBackgroundColor(pickedColor);
                spUtils.setKeyScreenColor(pickedColor);
            }
        });
        picker.show();
    }



    private long lastBackKeyDownTick = 0;
    public static final long MAX_DOUBLE_BACK_DURATION = 2000;
    public void onBackPressed(View view) {
        long currentTick = System.currentTimeMillis();
        if (currentTick - lastBackKeyDownTick > MAX_DOUBLE_BACK_DURATION) {
            SnackBarUtils.makeShort(view, "再按一次退出").success();
            lastBackKeyDownTick = currentTick;
        } else {
            finish();
            System.exit(0);
        }
    }


}
