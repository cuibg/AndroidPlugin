package com.bonc.mobile.plugin.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.webkit.WebView;

import com.baidu.location.LocationClient;
import com.bonc.mobile.plugin.choosestaff.DisplayDeptActivity;

import org.json.JSONException;

import java.util.List;

import rock.qrcodelibrary.CaptureActivity;

import static android.app.Activity.RESULT_CANCELED;
import static com.bonc.mobile.plugin.web.WebPluginKey.CAMERA_REQUEST_CODE;
import static com.bonc.mobile.plugin.web.WebPluginKey.STAFF_REQUST_CODE;
import static com.bonc.mobile.plugin.web.WebPluginKey.webCommandKey;
import static com.bonc.mobile.plugin.web.WebPluginKey.webObjectKey;
import static com.bonc.mobile.plugin.web.WebPluginKey.webParamsKey;

/**
 * Created by cuibg on 2017/1/9.
 * 调用webview插件工具类
 */

public class WebPluginHelper {
    private LocationClient mLocationClient;
    private WebAccelerometerPlugin webAccelerometerListener;
    private WebTakePhotoPlugin webTakePhotoPlugin;
    private WebDeviceInfoPlugin webDeviceInfoPlugin;
    private WebContactsPlugin webContactsPlugin;
    private WebScanPlugin webScanPlugin;
    private WebChooseStaffPlugin webChooseStaffPlugin;


    private static WebPluginHelper webPluginHelper;

    /**
     * 拿到WebPluginHelper对象
     * @return
     */
    public static WebPluginHelper getInstance() {
        if (webPluginHelper == null) {
            webPluginHelper = new WebPluginHelper();
            return webPluginHelper;
        } else {
            return webPluginHelper;
        }
    }

    /**
     * webView传过来的动作url转换成稀疏数组
     *
     * @param url
     * @return actionArray 动作对象
     */
    public SparseArray<String> getActionSparse(String url) {
        SparseArray<String> actionArray = new SparseArray<String>(3);
        if (url != null && url.startsWith(WebPluginKey.webServiceKey)) {
            int index = url.indexOf("?");
            String subUrl = url.substring(index + 1, url.length());
            String[] splitInfo = subUrl.split("[&]");
            String object = splitInfo[0].split("[=]")[1];//js传过来的行为对象
            String command = splitInfo[1].split("[=]")[1];//动作
            String params = "";
            if (splitInfo.length == 3) {
                params = splitInfo[2].split("[=]")[1];//参数
            }
            actionArray.put(webObjectKey, object);
            actionArray.put(webCommandKey, command);
            actionArray.put(webParamsKey, params);
        }
        return actionArray;
    }


    /**
     * 调起组件服务
     *
     * @param webView
     * @param actionArray
     * @param context
     * @param staffData   数据源（选择人员组件），如果没有传null
     */
    public boolean startCallComponent(WebView webView, SparseArray<String> actionArray, Context context, List<Object> staffData) {
        if (actionArray != null && actionArray.size() > 0) {
            String object = actionArray.get(webObjectKey);
            String command = actionArray.get(webCommandKey);
            String params = actionArray.get(webParamsKey);
            switch (object) {
                //定位
                case WebPluginKey.locAction:
                    if (WebPluginKey.locStartCom.equals(command)) {
                        if (mLocationClient == null) {
                            mLocationClient = new LocationClient(context);
                        }
                        WebLocationListener myListener = new WebLocationListener(webView);
                        mLocationClient.registerLocationListener(myListener); // 注册监听函数
                        myListener.setLocationOption(mLocationClient);
                        mLocationClient.start();
                    } else if (WebPluginKey.locStopCom.equals(command)) {
                        if (mLocationClient != null) {
                            mLocationClient.stop();
                        }
                    }
                    break;
                case WebPluginKey.devAction://设备信息
                    if (webDeviceInfoPlugin == null) {
                        webDeviceInfoPlugin = new WebDeviceInfoPlugin();
                    }
                    if (WebPluginKey.devInfoCom.equals(command)) {
                        webDeviceInfoPlugin.getDeViceInfo(webView, context);
                    } else if (WebPluginKey.devVibrateCom.equals(command)) {
                        webDeviceInfoPlugin.setVibrate(params, context);
                    }
                    break;
                case WebPluginKey.accAction://加速度计
                    if (WebPluginKey.accStartCom.equals(command)) {
                        if (webAccelerometerListener == null) {
                            webAccelerometerListener = new WebAccelerometerPlugin();
                        }
                        webAccelerometerListener.startAccelerometer(webView, context);
                    } else if (WebPluginKey.accStopCom.equals(command)) {
                        if (webAccelerometerListener != null) {
                            webAccelerometerListener.stopAccelerometer();
                        }
                    }
                    break;
                case WebPluginKey.cameraAction://照相机
                    if (WebPluginKey.cameraTakeCom.equals(command)) {
                        if (webTakePhotoPlugin == null) {
                            webTakePhotoPlugin = new WebTakePhotoPlugin();
                        }
                        webTakePhotoPlugin.putParams(params);
                        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        ((Activity) context).startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                    }
                    break;
                case WebPluginKey.qrCodeAction://二维码
                    if (WebPluginKey.qrCodeScanCom.equals(command)) {
                        if (webScanPlugin == null) {
                            webScanPlugin = new WebScanPlugin();
                        }
                        Intent intent = new Intent(context, CaptureActivity.class);
                        ((Activity) context).startActivityForResult(intent, WebPluginKey.QRCODE_REQUEST_CODE);
                    }
                    break;
                case WebPluginKey.contactAction://联系人
                    if (webContactsPlugin == null) {
                        webContactsPlugin = new WebContactsPlugin();
                    }
                    if (WebPluginKey.contactNewCom.equals(command)) {
                        webContactsPlugin.addContact(context);
                    } else if (WebPluginKey.contactChooseCom.equals(command)) {
                        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        ((Activity) context).startActivityForResult(intent, WebPluginKey.CONTACT_REQEST_CODE);
                    }
                    break;
                case WebPluginKey.orgPickAction://选择staff组件
                    if (webChooseStaffPlugin == null) {
                        webChooseStaffPlugin = new WebChooseStaffPlugin();
                    }
                    if (WebPluginKey.orgPickShowCom.equals(command)) {
                        webChooseStaffPlugin.setParams(params);
                        if (staffData != null && !staffData.isEmpty()) {
                            Intent intent = new Intent(context, DisplayDeptActivity.class);
                            ((Activity) context).startActivityForResult(webChooseStaffPlugin.strcIntent(intent, staffData), STAFF_REQUST_CODE);
                        }
                    }
                    break;
                default:
                    break;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * 跳转到activity后返回的数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @param webView
     * @param context
     */
    public void passActivityResultToJs(int requestCode, int resultCode, Intent data, WebView webView, Context context) {

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case WebPluginKey.CAMERA_REQUEST_CODE://拍照回调
                    if (webTakePhotoPlugin != null) {
                        webTakePhotoPlugin.passPictureToJs(data, webView);
                    }
                    break;
                case WebPluginKey.QRCODE_REQUEST_CODE://二维码回调
                    if (webScanPlugin != null) {
                        webScanPlugin.putQrcodeTojs(data, webView);
                    }
                    break;
                case WebPluginKey.CONTACT_REQEST_CODE://联系人信息回调
                    if (webContactsPlugin != null) {
                        Uri uriContact = data.getData();
                        Cursor cursorContact = context.getContentResolver().query(uriContact, null, null, null, null);
                        try {
                            webContactsPlugin.getContactInfo(webView, context, cursorContact);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case WebPluginKey.STAFF_REQUST_CODE://选择staff成功后回调
                    if (webChooseStaffPlugin != null) {
                        webChooseStaffPlugin.passDataToJs(webView, data);
                    }
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            switch (requestCode) {
                case WebPluginKey.CAMERA_REQUEST_CODE://拍照取消操作
                    if (webTakePhotoPlugin != null) {
                        webTakePhotoPlugin.cancelPassPicture(webView);
                    }
                    break;
                case WebPluginKey.QRCODE_REQUEST_CODE://二维码操作
                    if (webScanPlugin != null) {
                        webScanPlugin.cancelScan(webView);
                    }
                    break;
                case WebPluginKey.CONTACT_REQEST_CODE://获取联系人信息
                    if (webContactsPlugin != null) {
                        webContactsPlugin.cancelGetConcact(webView);
                    }
                    break;
                case WebPluginKey.STAFF_REQUST_CODE://取消获取联系人
                    if (webChooseStaffPlugin != null) {
                        webChooseStaffPlugin.cancelPassToJs(webView);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 调用定位组件的时候需要在onDestroy()方法中调用
     */
    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stop();
        }
    }
}
