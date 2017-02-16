package com.bonc.mobile.plugin.web;

/**
 * Created by cuibg on 2016/12/8.
 * WebPlugin中的action、commond、params中的Key
 */

public class WebPluginKey {
    public static final String webServiceKey = "mobile-service";
    //动作行为的key
    public static final String locAction = "locationManager";
    public static final String locStartCom="start";
    public static final String locStopCom = "stop";
    public static final String locLatitude ="latitude";
    public static final String locLongitude ="longitude";
    public static final String locSpeed ="speed";
    public static final String locTimestamp ="timestamp";


    public static final String devAction = "device";
    public static final String devInfoCom = "getDeviceInfo";
    public static final String devVibrateCom = "vibrate";
    public static final String devManufacturer="manufacturer";
    public static final String devPlatform="platform";
    public static final String devModel="model";
    public static final String devDetailModel="detailModel";
    public static final String devUuid="uuid";
    public static final String devName="name";
    public static final String devVersion="version";

    public static final String accAction = "accelerometer";
    public static final String accStartCom = "start";
    public static final String accStopCom = "stop";
    public static final String accX = "x";
    public static final String accY = "y";
    public static final String accZ = "z";
    public static final String accTimeStamp = "timestamp";



    public static final String cameraAction = "capture_activity_layout";
    public static final String cameraTakeCom  = "takePhoto";
    public static final String cameraDataURL  = "dataURL";
    public static final String cameraType  = "type";
    public static final String cameraBase64 = "dataBase64String";

    public static final String qrCodeAction = "codeScanner";
    public static final String qrCodeScanCom = "scan";
    public static final String qrCodeInfo = "codeInfo";

    public static final String contactAction = "contacts";
    public static final String contactNewCom = "newContact";
    public static final String contactChooseCom = "chooseContact";

    public static final String orgPickAction = "organizerPicker";
    public static final String orgPickShowCom = "show";
    public static final String orgPickId = "id";
    public static final String orgPickIcon = "iconURL";
    public static final String orgPickName = "name";
    public static final String orgPickJobNum = "jobNumber";

    public static final String webDescription = "description";
    public static final String webJs = "javascript:";
    public static final String boncAppEngine = "boncAppEngine";
    public static final String successHandler = "successHandler";
    public static final String errorHandler = "errorHandler";
    public static final String cancleHandler = "cancleHandler";
    //请求码
    public static final int CAMERA_REQUEST_CODE = 0x0001 << 2;
    public static final int QRCODE_REQUEST_CODE = 0x0002 << 2;
    public static final int CONTACT_REQEST_CODE = 0x0003 << 2;
    public static final int STAFF_REQUST_CODE = 0x0004 << 2;
    //动作行为
    public static final int webObjectKey = 0x0005<<2;
    public static final int webCommandKey = 0x0006<<2;
    public static final int webParamsKey = 0x0007<<2;

}
