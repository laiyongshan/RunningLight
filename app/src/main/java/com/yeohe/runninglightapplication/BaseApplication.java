package com.yeohe.runninglightapplication;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.holidaycheck.permissify.DialogText;
import com.holidaycheck.permissify.PermissifyConfig;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/12/26.
 */

public class BaseApplication extends Application {

    public static Context context;
    private static Resources resource;
    private static BaseApplication baseApplication;

    public static synchronized BaseApplication getInstance() {
        return baseApplication;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getBaseContext();
        baseApplication = this;
        SharedPreferenceUtil.initSharedPreference(getApplicationContext());


        PermissifyConfig permissifyConfig = new PermissifyConfig.Builder()
                .withDefaultTextForPermissions(new HashMap<String, DialogText>() {{
                    put(Manifest.permission_group.CAMERA, new DialogText(R.string.camera_rationale, R.string.camera_deny_dialog));
                }})
                .build();

        PermissifyConfig.initDefault(permissifyConfig);

    }
}
