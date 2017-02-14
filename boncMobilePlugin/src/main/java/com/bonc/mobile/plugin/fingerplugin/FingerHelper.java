package com.bonc.mobile.plugin.fingerplugin;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
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
    private CustomFingerDialog customFingerDialog;

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
     * 判断手机是否支持指纹识别或者是否已经录入指纹
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkSupportFinger(Context context) {
        try {
            Class.forName("android.hardware.fingerprint.FingerprintManager");
            FingerprintManager manager = (FingerprintManager) context.getSystemService(FINGERPRINT_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED) {
                if (manager.isHardwareDetected() && manager.hasEnrolledFingerprints()) {
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置指纹监听
     *
     * @param context
     * @return
     */
    public FingerCallback setFingerListener(Context context) {
        customFingerDialog = new CustomFingerDialog();
        customFingerDialog.setCancelable(false);
        customFingerDialog.show(((FragmentActivity)context).getSupportFragmentManager(), "fingerDialog");
        final FingerprintManagerCompat manager = FingerprintManagerCompat.from(context);
        final FingerCallback fingerCallback = new FingerCallback(context, customFingerDialog);
        if (mCancellationSignal == null) {
            mCancellationSignal = new CancellationSignal();
        }
        manager.authenticate(null, 0, mCancellationSignal, fingerCallback, null);
        return fingerCallback;
    }

    /**
     * 返回CustomFingerDialog对象
     * @return
     */
    public CustomFingerDialog getDialogFragment(){
        return customFingerDialog;
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
