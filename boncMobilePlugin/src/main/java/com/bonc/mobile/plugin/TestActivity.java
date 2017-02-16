package com.bonc.mobile.plugin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bonc.mobile.plugin.scancoder.CaptureActivity;
import com.bonc.mobile.plugin.utils.MResource;

public class TestActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MResource.getIdByName(this,"layout","activity_main"));
        textView = ((TextView) findViewById(MResource.getIdByName(this, "id", "textview")));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TestActivity.this, CaptureActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            String result=data.getStringExtra("result");
            Toast.makeText(this,result,Toast.LENGTH_LONG).show();
        }
    }
}
