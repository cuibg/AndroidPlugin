package com.bonc.mobile.plugin.msgautofill;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;

/**
 * 短信验证码获取
 * Created by cuibg on 2017/1/24.
 */

public class SmsContentObserver extends ContentObserver {

    public static final String SMS_URI_INBOX = "content://sms/inbox";

    private Context context;
    private String smsContent;
    private MsgContentListener msgContentListener;
    private String mobileNumber;
    private String[] projections = {"_id", "body", "date"};

    public SmsContentObserver(Handler handler) {
        super(handler);
    }

    /**
     * 如果没有指定号码，mobileNumber需要传null
     *
     * @param context
     * @param handler
     * @param mobileNumber
     */
    public SmsContentObserver(Context context, Handler handler, String mobileNumber) {
        super(handler);
        this.context = context;
        this.mobileNumber = mobileNumber;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        // 读取收件箱中指定号码的短信
        Cursor cursor = null;
        if (TextUtils.isEmpty(mobileNumber)) {
            cursor = context.getContentResolver().query(Uri.parse(SMS_URI_INBOX), projections,
                   "read = ?", new String[]{"0"}, "date desc");
        } else {
            cursor = context.getContentResolver().query(Uri.parse(SMS_URI_INBOX), projections, "address=? and read=?",
                    new String[]{mobileNumber, "0"}, "date desc");
        }
        if(cursor != null && cursor.moveToFirst()){
            String smsbody = cursor.getString(cursor.getColumnIndex("body"));
            msgContentListener.setMsgContent(smsbody);
        }
        if(cursor != null && !cursor.isClosed()){
            cursor.close();
        }

    }

    /**
     * 接口用来回调信息的
     */
    public interface MsgContentListener {
        void setMsgContent(String msgContent);
    }

    /**
     * 设置回调监听和短信监听
     *
     * @param msgContentListenr
     * @param smsContentObserver
     */
    public void registerMsgContentListenr(MsgContentListener msgContentListenr, SmsContentObserver smsContentObserver) {
        this.msgContentListener = msgContentListenr;
        context.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, this);
    }

    /**
     * 注销监听
     */
    public void unRegisterMsgContentListener(){
        if(msgContentListener != null){
            context.getContentResolver().unregisterContentObserver(this);
        }
    }
}
