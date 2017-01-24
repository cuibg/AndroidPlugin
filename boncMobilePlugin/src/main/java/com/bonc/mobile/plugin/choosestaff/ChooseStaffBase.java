package com.bonc.mobile.plugin.choosestaff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 选择人员组件的基类
 * @author cuibg
 *
 */
@SuppressLint({ "InlinedApi", "UseSparseArrays", "NewApi" })
public class ChooseStaffBase extends AppCompatActivity  {
	protected RequestQueue mQueue;
	protected com.android.volley.toolbox.ImageLoader mVImageLoader;
	protected Context context;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		mQueue = Volley.newRequestQueue(this);
		if(mVImageLoader==null){
			mVImageLoader = new com.android.volley.toolbox.ImageLoader(mQueue, new BitmapCache());
		}
	}
}