package com.bonc.mobile.plugin.msgautofill;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.CursorLoader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 短信验证码获取
 * Created by cuibg on 2017/1/24.
 */

public class SmsContentObserver extends ContentObserver {
    private  String containContent;

    public SmsContentObserver(Handler handler) {
        super(handler);
    }

    public static final String SMS_URI_INBOX = "content://sms/inbox";
    private Context context;
    private String smsContent;
    private MsgContentListener msgContentListener;
    private String mobileNumber;
    /**
     * 如果没有指定号码，mobileNumber需要传null
     *
     * @param context
     * @param handler
     * @param mobileNumber
     */
    public SmsContentObserver(Context context, Handler handler, String mobileNumber,String containContent) {
        super(handler);
        this.context = context;
        this.mobileNumber = mobileNumber;
        this.containContent = containContent;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        // 读取收件箱中指定号码的短信
        CursorLoader cursorLoader = null;
        if (TextUtils.isEmpty(mobileNumber)) {
            cursorLoader = new CursorLoader(context, Uri.parse(SMS_URI_INBOX), new String[]{"_id", "address", "body", "read"}, "read=?", new String[]{"0"}, "date desc");
        } else {
            cursorLoader = new CursorLoader(context, Uri.parse(SMS_URI_INBOX), new String[]{"_id", "address", "body", "read"}, "address=? and read=?",
                    new String[]{mobileNumber, "0"}, "date desc");
        }
        Cursor cursor = cursorLoader.loadInBackground();
        if (cursor != null) {// 如果短信为未读模式
            if (cursor.moveToFirst()) {
                String smsbody = cursor.getString(cursor.getColumnIndex("body"));
                if(!TextUtils.isEmpty(smsbody) && smsbody.contains(containContent)){
                    String regEx = "[^0-9]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(smsbody.toString());
                    smsContent = m.replaceAll("").trim().toString();
                    msgContentListener.setMsgContent(smsContent);
                }
            }
        }
        if (!cursor.isClosed()) {
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
     * @param msgContentListenr
     * @param smsContentObserver
     */
    public void setMsgContentListenr(MsgContentListener msgContentListenr, SmsContentObserver smsContentObserver) {
        this.msgContentListener = msgContentListenr;
        context.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, smsContentObserver);
    }

}
