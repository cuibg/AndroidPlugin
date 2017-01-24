package com.bonc.mobile.plugin.web;

import android.content.Intent;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import static com.bonc.mobile.plugin.web.WebPluginKey.boncAppEngine;
import static com.bonc.mobile.plugin.web.WebPluginKey.cancleHandler;
import static com.bonc.mobile.plugin.web.WebPluginKey.errorHandler;
import static com.bonc.mobile.plugin.web.WebPluginKey.qrCodeAction;
import static com.bonc.mobile.plugin.web.WebPluginKey.qrCodeInfo;
import static com.bonc.mobile.plugin.web.WebPluginKey.successHandler;
import static com.bonc.mobile.plugin.web.WebPluginKey.webDescription;
import static com.bonc.mobile.plugin.web.WebPluginKey.webJs;

/**
 * Created by cuibg on 2016/11/24.
 * 扫描二维码处理类
 */

public class WebScanPlugin {
    public WebScanPlugin() {
    }

    /**
     * 扫描二维码后回调数据
     *
     * @param webView
     * @param data
     */
    public void putQrcodeTojs(Intent data, WebView webView) {
        JSONObject json = new JSONObject();
        String jsUrl = "";
        if (data != null) {
            String result = data.getStringExtra("result");
            try {
                json.put(qrCodeInfo, result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsUrl = boncAppEngine + "." + qrCodeAction + "." + successHandler + "(" + json.toString() + ")";
        } else {
            try {
                json.put(webDescription, "扫描二维码失败");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsUrl = boncAppEngine + "." + qrCodeAction + "." + errorHandler + "(" + json.toString() + ")";
        }
        webView.loadUrl(webJs + jsUrl);
    }

    /**
     * 取消二维码扫描
     *
     * @param webView
     */
    public void cancelScan(WebView webView) {
        JSONObject json = new JSONObject();
        try {
            json.put(webDescription, "取消扫描");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsUrl = boncAppEngine + "." + qrCodeAction + "." + cancleHandler + "(" + json.toString() + ")";
        webView.loadUrl(webJs + jsUrl);
    }
}
