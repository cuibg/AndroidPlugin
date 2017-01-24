package com.bonc.mobile.plugin.web;

import android.content.Intent;
import android.text.TextUtils;
import android.webkit.WebView;

import com.bonc.mobile.plugin.choosestaff.GradeViewHelper;
import com.bonc.mobile.plugin.choosestaff.Node;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.bonc.mobile.plugin.web.WebPluginKey.boncAppEngine;
import static com.bonc.mobile.plugin.web.WebPluginKey.cancleHandler;
import static com.bonc.mobile.plugin.web.WebPluginKey.errorHandler;
import static com.bonc.mobile.plugin.web.WebPluginKey.orgPickAction;
import static com.bonc.mobile.plugin.web.WebPluginKey.orgPickIcon;
import static com.bonc.mobile.plugin.web.WebPluginKey.orgPickId;
import static com.bonc.mobile.plugin.web.WebPluginKey.orgPickJobNum;
import static com.bonc.mobile.plugin.web.WebPluginKey.orgPickName;
import static com.bonc.mobile.plugin.web.WebPluginKey.successHandler;
import static com.bonc.mobile.plugin.web.WebPluginKey.webDescription;
import static com.bonc.mobile.plugin.web.WebPluginKey.webJs;


/**
 * Created by cuibg on 2016/12/7.
 * 网页调起选择联系人组件
 */

public class WebChooseStaffPlugin {
    private String params;

    public WebChooseStaffPlugin() {

    }

    public void setParams(String params) {
        this.params = params;
    }


    /**
     * 封装Intent
     *
     * @param intent
     * @param list
     * @return
     */
    public Intent strcIntent(Intent intent, List<Object> list) {

        ArrayList<String> staffList = new ArrayList<>();
        String chooseType = "";
        if (!TextUtils.isEmpty(params)) {
            try {
                JSONArray jsJSonArray = new JSONArray(params);
                JSONArray staffArray = jsJSonArray.getJSONArray(0);
                chooseType = jsJSonArray.getString(1);
                int length = staffArray.length();
                for (int i = 0; i < length; i++) {
                    staffList.add(staffArray.get(i).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Map<String, Node> nodeMap = GradeViewHelper.structureData(list);
        intent.putExtra(GradeViewHelper.nodeMap, (Serializable) nodeMap);
        intent.putExtra(GradeViewHelper.chooseType, chooseType == "true" ? GradeViewHelper.multipleType : GradeViewHelper.singleType);//多选，如果不设置的话，默认是多选
        intent.putExtra(GradeViewHelper.getCheckedListKey, (Serializable) staffList);
        return intent;
    }

    /**
     * 把获取数据传到js
     *
     * @param webView
     * @param data
     */
    public void passDataToJs(WebView webView, Intent data) {

        if (data != null) {
            Map<String, Node> haveCheckedMap = (Map<String, Node>) data.getSerializableExtra(GradeViewHelper.haveCheckedMapKey);
            ArrayList<String> passToJsList = new ArrayList<>();
            if (!haveCheckedMap.isEmpty()) {
                Set<String> checkedKeySet = haveCheckedMap.keySet();
                Iterator<String> iterator = checkedKeySet.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    passToJsList.add(strucStaffData(haveCheckedMap.get(key)).toString());
                }
            }
            String jsonUrl = boncAppEngine+"."+orgPickAction+"."+successHandler+"("+passToJsList.toString()+")";
            webView.loadUrl(webJs + jsonUrl);
        } else {
            errorPassToJs(webView,"选择失败");
        }
    }

    /**
     * 取消传递数据到Js
     *
     * @param webView
     */
    public void cancelPassToJs(WebView webView) {
        JSONObject jsJson = new JSONObject();
        try {
            jsJson.put(webDescription, "取消选择");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonUrl = boncAppEngine+"."+orgPickAction+"."+cancleHandler+"("+jsJson.toString()+")";
        webView.loadUrl(webJs + jsonUrl);
    }

    /**
     * 获取数据过程中出错处理
     * @param webView
     * @param description
     */
    public void errorPassToJs(WebView webView, String description){
        JSONObject jsJson = new JSONObject();
        try {
            jsJson.put(webDescription, description);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonUrl = boncAppEngine+"."+orgPickAction+"."+errorHandler+"("+jsJson.toString()+")";
        webView.loadUrl(webJs + jsonUrl);
    }
    /**
     * 在node中拿出数据封装成json对象
     *
     * @param node
     */
    public JSONObject strucStaffData(Node node) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(orgPickId, GradeViewHelper.getInnerId(node.getId()));
            jsonObject.put(orgPickIcon, node.getIconUrl());
            jsonObject.put(orgPickName, node.getName());
            jsonObject.put(orgPickJobNum, node.getJobNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
