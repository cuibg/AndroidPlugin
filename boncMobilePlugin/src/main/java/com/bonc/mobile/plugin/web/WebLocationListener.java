package com.bonc.mobile.plugin.web;

import android.webkit.WebView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.json.JSONException;
import org.json.JSONObject;

import static com.bonc.mobile.plugin.web.WebPluginKey.boncAppEngine;
import static com.bonc.mobile.plugin.web.WebPluginKey.errorHandler;
import static com.bonc.mobile.plugin.web.WebPluginKey.locAction;
import static com.bonc.mobile.plugin.web.WebPluginKey.locLatitude;
import static com.bonc.mobile.plugin.web.WebPluginKey.locLongitude;
import static com.bonc.mobile.plugin.web.WebPluginKey.locSpeed;
import static com.bonc.mobile.plugin.web.WebPluginKey.locTimestamp;
import static com.bonc.mobile.plugin.web.WebPluginKey.successHandler;
import static com.bonc.mobile.plugin.web.WebPluginKey.webDescription;
import static com.bonc.mobile.plugin.web.WebPluginKey.webJs;

/**
 * webview定位组件
 * Created by lxs on 2016/8/15 15:42.
 */
public class WebLocationListener implements BDLocationListener {
    private WebView webView;

    public WebLocationListener(WebView webView) {
        this.webView = webView;
    }


    @Override
    public void onReceiveLocation(BDLocation location) {
        if (location == null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(webDescription,"定位失败");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            webView.loadUrl(webJs+boncAppEngine+"."+locAction+"."+errorHandler+"("+jsonObject.toString()+")");
            return;
        }
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        double speed = location.getSpeed();
        String timestamp = location.getTime();
        JSONObject obj = new JSONObject();
        try {
            obj.put(locLatitude, latitude + "");
            obj.put(locLongitude, longitude + "");
            obj.put(locSpeed, speed + "");
            obj.put(locTimestamp, timestamp);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String js = boncAppEngine+"."+locAction+"."+successHandler+"("+obj.toString()+")";
        webView.loadUrl(webJs+ js);
    }
    /**
     * 设置相关参数
     */
    public void setLocationOption(LocationClient mLocationClient) {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
        option.disableCache(true);// 禁止启用缓存定位
        option.setProdName("LocationDemo");
        mLocationClient.setLocOption(option);
    }
}