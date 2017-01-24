package com.bonc.mobile.plugin.web;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static android.content.Context.SENSOR_SERVICE;
import static com.bonc.mobile.plugin.web.WebPluginKey.accAction;
import static com.bonc.mobile.plugin.web.WebPluginKey.accTimeStamp;
import static com.bonc.mobile.plugin.web.WebPluginKey.accX;
import static com.bonc.mobile.plugin.web.WebPluginKey.accY;
import static com.bonc.mobile.plugin.web.WebPluginKey.accZ;
import static com.bonc.mobile.plugin.web.WebPluginKey.boncAppEngine;
import static com.bonc.mobile.plugin.web.WebPluginKey.errorHandler;
import static com.bonc.mobile.plugin.web.WebPluginKey.successHandler;
import static com.bonc.mobile.plugin.web.WebPluginKey.webDescription;
import static com.bonc.mobile.plugin.web.WebPluginKey.webJs;


/**
 * 加速度计的开启与停止
 * Created by cuibg on 2016/11/22.
 */

public class WebAccelerometerPlugin implements SensorEventListener {
    private WebView webView;
    private SensorManager sensorManager;
    private Sensor sensorDefault;
    private Handler handler = new Handler();
    /**
     * 开启加速度计
     */
    public void startAccelerometer(WebView webView, Context context) {
        this.webView = webView;
        if (sensorManager == null && sensorDefault == null) {
            sensorManager = ((SensorManager) context.getSystemService(SENSOR_SERVICE));
            sensorDefault = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, sensorDefault, SensorManager.SENSOR_DELAY_NORMAL, handler);
        }
    }

    /**
     * 停止加速度计
     */
    public void stopAccelerometer() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            sensorManager = null;
            sensorDefault = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        String jsUrl ="";
        if (event != null) {
            float xValue = event.values[0];
            float yValue = event.values[1];
            float zValue = event.values[2];
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(accX, xValue);
                jsonObject.put(accY, yValue);
                jsonObject.put(accZ, zValue);
                Date date = new Date();
                jsonObject.put(accTimeStamp, String.valueOf(date));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsUrl = boncAppEngine+"."+accAction+"."+successHandler+"("+jsonObject.toString()+")";
        } else {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(webDescription, "未获取加速度");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsUrl = boncAppEngine+"."+accAction+"."+errorHandler+"("+jsonObject.toString()+")";
        }
        webView.loadUrl(webJs + jsUrl);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
