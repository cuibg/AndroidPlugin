package com.bonc.mobile.plugin.fingerplugin;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.view.View;
import android.widget.TextView;

import com.bonc.mobile.plugin.utils.MResource;

/**
 * 指纹识别回调
 *
 * @author cuibg
 */

public class FingerCallback extends FingerprintManagerCompat.AuthenticationCallback {
    private final FingerHelper fingerInstance;//帮助类单例
    private Context context;
    private FingerInterface fingerInterface;//指纹回调接口
    private DialogFragment dialogFragment;//验证指纹弹框

    public FingerCallback(Context context, DialogFragment dialogFragment) {
        super();
        this.context = context;
        this.dialogFragment = dialogFragment;
        fingerInstance = FingerHelper.getFingerInstance();
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        super.onAuthenticationError(errMsgId, errString);
        //errMsgId 5 代表取消，7代表错误次数过多
        if (fingerInterface != null) {
            fingerInterface.setOnFingerError(errMsgId, errString);
        }
        if (dialogFragment != null) {
            View view = dialogFragment.getView();
            if (view != null) {
                TextView textView = (TextView) view.findViewById(MResource.getIdByName(context, "id", "finger_prompt_text"));
                textView.setText(errString + "");
                fingerInstance.setVerifyAnimation(textView);
            }
        }
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        if (fingerInterface != null) {
            fingerInterface.setOnFingerFailed();
        }
        if (dialogFragment != null) {
            View view = dialogFragment.getView();
            TextView textView = (TextView) view.findViewById(MResource.getIdByName(context, "id", "finger_prompt_text"));
            textView.setText(context.getResources().getString(MResource.getIdByName(context, "string", "finger_retry")));
            fingerInstance.setVerifyAnimation(textView);
        }
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        if (fingerInterface != null) {
            fingerInterface.setOnFingerSuccess(result);
        }
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        super.onAuthenticationHelp(helpMsgId, helpString);
        if (fingerInterface != null) {
            fingerInterface.setOnAuthenticationHelp(helpMsgId, helpString);
        }
    }

    /**
     * 用于接口回调
     *
     * @param fingerInterface
     */
    public void setFingerInterface(FingerInterface fingerInterface) {
        this.fingerInterface = fingerInterface;
    }
}
