package com.yeohe.runninglightapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import java.io.IOException;
import java.io.StreamCorruptedException;


public class SharedPreferenceUtil {

    // 用户名key
    public final static String KEY_NAME = "KEY_NAME";
    public final static String KEY_AUTO = "KEY_AUTO";
    public final static String KEY_LOGIN = "KEY_LOGIN";
    public final static String KEY_LEVEL = "KEY_LEVEL";
    public final static String KEY_DELIVERY = "KEY_DELIVERY";


    public final static String KEY_TOKEN="KEY_TOKEN";
    public final static String KEY_USER_NAME="KEY_USER_NAME";
    public final static String KEY_CAR_ID="KEY_CAR_ID";

    public final static String KEY_TEXT="KEY_TEXT";
    public final static String KEY_TEXT_COLOR="KEY_TEXT_COLOR";
    public final static String KEY_SCREEN_COLOR="KEY_SCREEN_COLOR";
    public final static String KEY_IS_SHAN="KEY_IS_SHAN";
    public final static String KEY_HZ="KEY_HZ";
    public final static String KEY_IS_FLASH="KEY_IS_FLASH";


    private static SharedPreferenceUtil spUtils;
    private SharedPreferences sp;


    //

    /**
     *
     * 初始化，一般在应用启动之后就要初始化
     *
     * @param context 此处的context要用application的全局上下文,
     *                避免static强类型一直持有activity的引用,造成内存泄露
     */
    public static synchronized void initSharedPreference(Context context) {
        if (spUtils == null) {
            spUtils = new SharedPreferenceUtil(context);
        }
    }


    /**
     * 获取唯一的instance
     *
     * @return
     */

    public static synchronized SharedPreferenceUtil getInstance() {
        if (spUtils == null) {
            spUtils = new SharedPreferenceUtil(BaseApplication.getInstance());
        }

        return spUtils;
    }

    public SharedPreferenceUtil(Context context) {
        sp = context.getSharedPreferences("SharedPreferenceUtil", Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPref() {
        return sp;
    }





    public synchronized void setToken(String token){

        SharedPreferences.Editor editor=sp.edit();
        editor.putString(KEY_TOKEN,token);
        editor.commit();
    }

    //读取token值
    public synchronized String getToken(){
        return sp.getString(KEY_TOKEN,"");
    }

    //退出删除token值
    public synchronized void DeleteToken(){
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(KEY_TOKEN,"");
        editor.commit();
    }


    public synchronized void putAutoLogin(Boolean AutoLogin) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY_AUTO, AutoLogin);
        editor.commit();
    }

    public synchronized Boolean getAutoLogin() {
        Boolean flag = sp.getBoolean(KEY_AUTO, false);
        Log.i("flag", flag + "flag");
        return flag;
    }

    public synchronized void setIsLogin(Boolean AutoLogin) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY_LOGIN, AutoLogin);
        editor.commit();
    }

    public synchronized Boolean getIsLogin() {
        Boolean flag = sp.getBoolean(KEY_LOGIN, false);
        return flag;
    }


    //存储输入的文字
    public synchronized void setTextData(String text){
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(KEY_TEXT,text);
        editor.commit();
    }

    public synchronized String getTextData(){
        return sp.getString(KEY_TEXT,"");
    }

    //设置字体颜色
    public synchronized  void setKeyTextColor(int textColorId){
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt(KEY_TEXT_COLOR,textColorId);
        editor.commit();
    }

    public synchronized int getKeyTextColor(){
        return sp.getInt(KEY_TEXT_COLOR, Color.WHITE);
    }

    //设置屏幕颜色
    public synchronized  void setKeyScreenColor(int screenColorId){
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt(KEY_SCREEN_COLOR,screenColorId);
        editor.commit();
    }

    public synchronized int getKeyScreenColor(){
        return sp.getInt(KEY_SCREEN_COLOR, Color.BLACK);
    }

    //屏幕是否闪烁
    public synchronized void setKeyIsShan(boolean isShan){
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean(KEY_IS_SHAN,isShan);
        editor.commit();
    }

    public synchronized  boolean getKeyIsShan(){
        return sp.getBoolean(KEY_IS_SHAN,false);
    }

    //闪光灯闪烁频率
    public synchronized  void setKeyHz(int hz){
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt(KEY_HZ,hz);
        editor.commit();
    }

    public synchronized  int getKeyHz(){
        return sp.getInt(KEY_HZ,3);
    }

    //是否开启闪光灯
    public synchronized  void setKeyIsFlash(boolean isFlash){
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean(KEY_IS_FLASH,isFlash);
        editor.commit();
    }

    public boolean getKeyIsFlash(){
        return sp.getBoolean(KEY_IS_FLASH,false);
    }

//
//    public synchronized DeliveryMessage getDeliveryMessage() {
//        DeliveryMessage deliveryMessage = new DeliveryMessage();
//        //获取序列化的数据
//        String str = sp.getString(SharedPreferenceUtil.KEY_DELIVERY, "");
//        if (TextUtils.isEmpty(str)) {
//            return null;
//        }
//        try {
//            Object obj = SerializableUtil.strToObj(str);
//            if (obj != null) {
//                deliveryMessage = (DeliveryMessage) obj;
//            }
//        } catch (StreamCorruptedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return deliveryMessage;
//
//    }
//
//    public synchronized void putDeliveryMessage(DeliveryMessage deliveryMessage) {
//        SharedPreferences.Editor editor = sp.edit();
//        String str = "";
//        try {
//            str = SerializableUtil.objToStr(deliveryMessage);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        editor.putString(KEY_DELIVERY, str);
//        editor.commit();
//    }


}
