package com.bonc.mobile.plugin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bonc.mobile.plugin.utils.MResource;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MResource.getIdByName(this,"layout","activity_main"));
    }
}
