package com.bonc.mobile.plugin.fingerplugin;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.view.View;
import android.view.animation.TranslateAnimation;

import static android.content.Context.FINGERPRINT_SERVICE;

/**
 * 指纹识别帮助类
 *
 * @author cuibg
 */

public class FingerHelper {
    private static FingerHelper fingerHelper = null;
    private CancellationSignal mCancellationSignal;

    /**
     * 得到FingerHelper单例
     * @return
     */
    public static FingerHelper getFingerInstance() {
        if (fingerHelper == null) {
            fingerHelper = new FingerHelper();
        }
        return fingerHelper;
    }

    /**
     * 判断手机上是否有指纹
     *
     * @return 0表示手机支持指纹而且已经录入指纹;1表示指手机支持指纹但是没有录入指纹;2表示手机不支持指纹功能
     */
    @TargetApi(Build.VERSION_CODES.M)
    public int checkSupportFinger(Context context) {
        try {
            Class.forName("android.hardware.fingerprint.FingerprintManager");
            FingerprintManager manager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED) {
                if(!manager.isHardwareDetected()){
                    return 2;
                }else if(manager.isHardwareDetected()){
                    if(manager.hasEnrolledFingerprints()){
                        return 0;
                    }
                    return 1;
                };
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 2;
    }

    /**
     * 设置指纹监听
     *
     * @param context
     * @return
     */
    public FingerCallback setFingerListener(Context context, DialogFragment customFingerDialog) {
        final FingerprintManagerCompat manager = FingerprintManagerCompat.from(context);
        final FingerCallback fingerCallback = new FingerCallback(context, customFingerDialog);
        if (mCancellationSignal == null) {
            mCancellationSignal = new CancellationSignal();
        }
        manager.authenticate(null, 0, mCancellationSignal, fingerCallback, null);
        return fingerCallback;
    }

    /**
     * 取消指纹监听
     */
    public void cancelFingerListener() {
        if (mCancellationSignal != null && !mCancellationSignal.isCanceled()) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }

    /**
     * 返回CustomFingerDialog对象
     * @return
     */
    public CustomFingerDialog showAndReturnDialogFragment(Context context){
        CustomFingerDialog customFingerDialog = new CustomFingerDialog();
        customFingerDialog.setCancelable(false);
        customFingerDialog.show(((FragmentActivity)context).getSupportFragmentManager(), "fingerDialog");
        return customFingerDialog;
    }

    /**
     * 仿支付宝手势识别字体动画
     *
     * @param view
     */
    public void setVerifyAnimation(View view) {
        TranslateAnimation translateAnimation = new TranslateAnimation(-80f, 80f, 0, 0);
        translateAnimation.setDuration(100);
        translateAnimation.setRepeatCount(1);
        view.setAnimation(translateAnimation);
        translateAnimation.start();
    }
}
