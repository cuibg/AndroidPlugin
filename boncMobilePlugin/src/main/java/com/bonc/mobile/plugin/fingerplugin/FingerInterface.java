package com.bonc.mobile.plugin.fingerplugin;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

/**
 * 指纹识别接口回调
 * Created by cuibg on 2017/1/22.
 */

public interface FingerInterface {
    /**
     * 错误回调
     * @param errMsgId
     * @param errString
     */
    void setOnFingerError(int errMsgId, CharSequence errString);

    /**
     * 指纹验证成功回调
     * @param result
     */
    void setOnFingerSuccess(FingerprintManagerCompat.AuthenticationResult result);

    /**
     * 帮助信息
     * @param helpMsgId
     * @param helpString
     */
    void setOnAuthenticationHelp(int helpMsgId, CharSequence helpString);

    /**
     * 失败回调
     */
    void setOnFingerFailed();
}
