package com.bonc.mobile.plugin.web;

import android.content.Context;
import android.os.Build;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import static com.bonc.mobile.plugin.web.WebPluginKey.boncAppEngine;
import static com.bonc.mobile.plugin.web.WebPluginKey.devAction;
import static com.bonc.mobile.plugin.web.WebPluginKey.devDetailModel;
import static com.bonc.mobile.plugin.web.WebPluginKey.devManufacturer;
import static com.bonc.mobile.plugin.web.WebPluginKey.devModel;
import static com.bonc.mobile.plugin.web.WebPluginKey.devName;
import static com.bonc.mobile.plugin.web.WebPluginKey.devPlatform;
import static com.bonc.mobile.plugin.web.WebPluginKey.devUuid;
import static com.bonc.mobile.plugin.web.WebPluginKey.devVersion;
import static com.bonc.mobile.plugin.web.WebPluginKey.errorHandler;
import static com.bonc.mobile.plugin.web.WebPluginKey.successHandler;
import static com.bonc.mobile.plugin.web.WebPluginKey.webDescription;
import static com.bonc.mobile.plugin.web.WebPluginKey.webJs;

/**
 * Created by cuibg on 2016/11/22.
 * 获取设备信息并传入到webview，或者调起震动
 */

public class WebDeviceInfoPlugin {
    public WebDeviceInfoPlugin() {
    }

    /**
     * 获取手机信息
     * @param webView
     * @param context
     */
    public void getDeViceInfo(WebView webView, Context context) {
        String manufacturer = Build.MANUFACTURER;
        String name = Build.BRAND;
        String model = android.os.Build.MODEL;
        String version = Build.VERSION.RELEASE;

        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String tmDevice = "" + tm.getDeviceId();
        String tmSerial = "" + tm.getSimSerialNumber();
        String androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String uuid = deviceUuid.toString();//uuid
        JSONObject jsonObject = new JSONObject();
        String jsUrl = "";
        if (TextUtils.isEmpty(manufacturer) && TextUtils.isEmpty(model) && TextUtils.isEmpty(model) && TextUtils.isEmpty(uuid) && TextUtils.isEmpty(version)) {
            try {
                jsonObject.put(webDescription, "对不起，没有获取手机信息，请重新尝试");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsUrl = boncAppEngine+"."+devAction+"."+errorHandler+"("+jsonObject.toString()+")";
        } else {
            try {
                jsonObject.put(devManufacturer, manufacturer);
                jsonObject.put(devPlatform, "android");
                jsonObject.put(devModel, model);
                jsonObject.put(devDetailModel,model);
                jsonObject.put(devUuid, uuid);
                jsonObject.put(devName, name);
                jsonObject.put(devVersion, version);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsUrl = boncAppEngine+"."+devAction+"."+successHandler+"("+jsonObject.toString()+")";
        }
        webView.loadUrl(webJs + jsUrl);
    }

    /**
     * 震动
     * @param params
     * @param context
     */
    public void setVibrate(String params, Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        int frequency = 0;
        try {
            JSONArray paramsArray = new JSONArray(params);
            frequency = (int) paramsArray.get(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (frequency != 0) {
            long[] longs = new long[2 * frequency];
            for (int i = 0; i < 2 * frequency; i++) {
                if (i % 2 == 0) {
                    longs[i] = 500;
                } else {
                    longs[i] = 500;
                }
            }
            vibrator.vibrate(longs, -1);
        } else {
            vibrator.vibrate(1000);
        }
    }
}
