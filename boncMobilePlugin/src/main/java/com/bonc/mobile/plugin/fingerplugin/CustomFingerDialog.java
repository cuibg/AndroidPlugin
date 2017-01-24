package com.bonc.mobile.plugin.fingerplugin;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.bonc.mobile.plugin.utils.MResource;

/**
 * 自定义一个FingerDialog
 *
 * @author cuibg
 */
public class CustomFingerDialog extends DialogFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(MResource.getIdByName(getContext(),"layout","fragment_custom_dialog"), container);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().findViewById(MResource.getIdByName(getContext(),"id","fingure_cancle_text")).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (7.0f / 10.0f * dm.widthPixels), getDialog().getWindow().getAttributes().height);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == MResource.getIdByName(getContext(),"id","fingure_cancle_text")) {
            FingerHelper fingerInstance = FingerHelper.getFingerInstance();
            fingerInstance.cancelFingerListener();
            this.dismiss();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        this.dismiss();
        FingerHelper fingerInstance = FingerHelper.getFingerInstance();
        fingerInstance.cancelFingerListener();
    }
}
