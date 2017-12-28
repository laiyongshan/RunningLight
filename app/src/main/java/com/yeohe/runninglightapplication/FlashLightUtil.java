package com.yeohe.runninglightapplication;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.TransitionDrawable;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.List;

/**
 * Created by Administrator on 2017/12/26.
 * 闪光灯控制工具类
 */

public class FlashLightUtil {



    //开启闪光灯
    public static void turnLightOn(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        // Check if camera flash exists
        if (flashModes == null) {
            // Use the screen as a flashlight (next best thing)
            return;
        }
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            } else {
            }
        }
    }

    //关闭闪光灯
    public static void turnLightOff(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        // Check if camera flash exists
        if (flashModes == null) {
            return;
        }
        if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
        // Turn off the flash
        if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(parameters);
        } else {

            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void shanFlashLight(CameraManager manager, final Camera camera, final int hz){
                    try {
                        if(Build.VERSION.SDK_INT <23) {
                            turnLightOn(camera);
                            Thread.sleep(3000 / hz);
                            turnLightOff(camera);
                            Thread.sleep(3000 / hz);
                        }else{
                                if (Build.VERSION.SDK_INT >=23) {
                                    manager.setTorchMode("0", true);
                                    manager.setTorchMode("1",true);
                                    Thread.sleep(3000 / hz);
                                    manager.setTorchMode("0", false);
                                    manager.setTorchMode("1",false);
                                    Thread.sleep(3000 / hz);
                                }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
    }


}
