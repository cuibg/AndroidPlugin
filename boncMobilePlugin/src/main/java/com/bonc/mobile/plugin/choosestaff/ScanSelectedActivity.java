package com.bonc.mobile.plugin.choosestaff;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bonc.mobile.plugin.utils.MResource;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by cuibg on 2016/10/27.
 * 浏览选中人员界面
 */
public class ScanSelectedActivity extends ChooseStaffBase implements View.OnClickListener {

    private GradeView gradeView;
    private RecyclerView recyclerView;
    private Map<String, Node> serialbeNodeMap;
    private TextView numberDeatilText;//显示选中人员的具体信息
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gradeView = new GradeView(this);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        gradeView.setLayoutParams(layoutParams);
        setContentView(gradeView);
        getIntentData();
        initWidget();
    }

    /**
     * 拿到传过来的数据
     */

    private void getIntentData() {
        Intent intent = getIntent();
        if(intent!=null){
            serialbeNodeMap = ((Map<String, Node>) intent.getSerializableExtra(GradeViewHelper.nodeMap));
        }
    }

    /**
     * 初始化组件
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public void initWidget(){
        gradeView.setBackGroudColor(context);

        gradeView.getSerchText().setVisibility(View.GONE);
        gradeView.getBackText().setVisibility(View.GONE);
        gradeView.getGradeGroupNameLin().setVisibility(View.GONE);//显示部门那行消失
        gradeView.getGradeSplitLine().setVisibility(View.GONE);//分割线消失
        gradeView.getGradeHeadNumberRel().setVisibility(View.GONE);//人头和小红点消失
        numberDeatilText = gradeView.getNumberDeatilText();
        numberDeatilText.setText("当前已选择" + GradeViewHelper.haveCheckedList.size() + "人");

        gradeView.getCommitText().setOnClickListener(this);
        gradeView.getBackView().setOnClickListener(this);
        recyclerView = gradeView.getRecycleView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        GradeViewAdapter adapter = new GradeViewAdapter(this,gradeView,serialbeNodeMap,mVImageLoader,0,GradeViewHelper.scanSelectedActivity);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id== MResource.getIdByName(context,"id","grade_image_back")){//点击返回离开
            setResult(RESULT_CANCELED);
            finish();
        }else if(id==MResource.getIdByName(context,"id","grade_commit_text")){
            Intent intent = new Intent();
            Map<String, Node> haveCheckedMap = GradeViewHelper.getCheckedData(serialbeNodeMap);
            intent.putExtra(GradeViewHelper.haveCheckedMapKey,(Serializable)haveCheckedMap);
            setResult(RESULT_OK,intent);
            finish();
        }
    }

    /**
     * 底部状态显示
     */
    public void setBottomState(int size){
            numberDeatilText.setText("当前已选择" + size + "人");
    }
}
