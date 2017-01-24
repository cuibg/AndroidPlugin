package com.bonc.mobile.plugin.choosestaff;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bonc.mobile.plugin.utils.MResource;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by cuibg on 2016/10/27.
 * 选择添加人员界面
 */

public class DisplayDeptActivity extends ChooseStaffBase implements View.OnClickListener {
    private RecyclerView recyclerView;
    private GradeView grade_view;
    private GradeViewAdapter gradeViewAdapter;
    private ImageView grade_image_back, grade_bottom_image;
    private Map<String, Node> nodeDatasMap;
    private int selectType = GradeViewHelper.singleType;
    private EditText grade_search_text;
    private TextView grade_commit_text;
    private final int scanRequsetCode = 0x0001;//跳转浏览选中人页面请求码
    private final int searchRequestCode = 0x0002;//跳转查找人请求码
    private View gradeHeadNumberRel;
    private List<String> checkedUserIdList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        grade_view = new GradeView(this);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        grade_view.setLayoutParams(layoutParams);
        setContentView(grade_view);

        getIntentData();
        initWidget();
        displayData();
    }


    /**
     * 得到传过来的数据，并处理数据
     */
    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            nodeDatasMap = (Map<String,Node>) intent.getSerializableExtra(GradeViewHelper.nodeMap);//构造后的数据模型
            checkedUserIdList = (List<String>)intent.getSerializableExtra(GradeViewHelper.getCheckedListKey);//选中的list集合
            selectType = intent.getIntExtra(GradeViewHelper.chooseType, GradeViewHelper.singleType);
            if(checkedUserIdList!=null&&(GradeViewHelper.singleType!=selectType)){
                int size = checkedUserIdList.size();
                for (int i = 0; i < size; i++) {
                    String innerId = checkedUserIdList.get(i);
                    //将id转换为可操作的id
                    String id = GradeViewHelper.addIdPrefix(innerId);
                    GradeViewHelper.haveCheckedList.add(id);
                }
            }
        }
    }

    /**
     * 初始化控件
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void initWidget() {
        grade_view.setBackGroudColor(context);

        grade_image_back = grade_view.getBackView();//返回图
        grade_image_back.setOnClickListener(this);

        grade_search_text = grade_view.getSerchText();//搜索
        grade_search_text.setCursorVisible(false);
        grade_search_text.setInputType(InputType.TYPE_NULL);
        grade_search_text.setOnClickListener(this);

        grade_view.getBackText().setVisibility(View.GONE);//返回字不显示
        grade_bottom_image = grade_view.getGradeBottomImage();//人头图片
        grade_commit_text = grade_view.getCommitText();//提交
        grade_commit_text.setOnClickListener(this);//提交事件

        gradeHeadNumberRel = grade_view.getGradeHeadNumberRel();
        gradeHeadNumberRel.setOnClickListener(this);//点击底部人头跳转
        if (GradeViewHelper.multipleType == selectType) {
            grade_view.getGradeNumbeRel().setVisibility(View.VISIBLE);
            setBottomState();
        } else {
            grade_view.getGradeNumbeRel().setVisibility(View.GONE);
        }

    }

    /**
     * 获取传过来的数据
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void displayData() {
        recyclerView = ((RecyclerView) grade_view.getRecycleView());//列表
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        gradeViewAdapter = new GradeViewAdapter(this, grade_view, nodeDatasMap,mVImageLoader, selectType, GradeViewHelper.chooseStaffActivity);
        gradeViewAdapter.setGroupBackImageListener();
        recyclerView.setAdapter(gradeViewAdapter);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == MResource.getIdByName(context, "id", "grade_image_back")) {//退出
            finish();
        } else if (id == MResource.getIdByName(context, "id", "grade_search_text")) {//搜索
            Intent intent = new Intent(this, SearchStaffActivity.class);
            intent.putExtra(GradeViewHelper.nodeMap, (Serializable) nodeDatasMap);
            intent.putExtra(GradeViewHelper.chooseType, selectType);
            startActivityForResult(intent, searchRequestCode);
        } else if (id == MResource.getIdByName(context, "id", "grade_head_number_rel")) {//浏览
            Intent intent = new Intent(this, ScanSelectedActivity.class);
            intent.putExtra(GradeViewHelper.nodeMap, (Serializable) nodeDatasMap);
            startActivityForResult(intent, scanRequsetCode);
        } else if (id == MResource.getIdByName(context, "id", "grade_commit_text")) {//确定
            passData();
        }
    }

    /**
     * 传递数据到上一个activity,同时finish这个activity
     */
    public void passData() {
        Map<String, Node> haveCheckedMap = GradeViewHelper.getCheckedData(nodeDatasMap);
        Intent intent = new Intent();
        intent.putExtra(GradeViewHelper.haveCheckedMapKey, (Serializable) haveCheckedMap);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == scanRequsetCode && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
        if (requestCode == searchRequestCode && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        } else if (requestCode == scanRequsetCode || requestCode == searchRequestCode) {
            gradeViewAdapter.notifyDataSetChanged();
            setBottomState();
        }
    }

    /**
     * 设置底部状态
     */
    public void setBottomState() {
        int size = GradeViewHelper.haveCheckedList.size();
        if (size != 0) {
            grade_view.getNumberDeatilText().setText("已选择" + size + "人");
            TextView numberText = grade_view.getNumberText();
            numberText.setVisibility(View.VISIBLE);
            if(size<100){
                numberText.setText(String.valueOf(size));
            }else{
                numberText.setText("99+");
            }
            gradeHeadNumberRel.setClickable(true);
            GradeViewHelper.setBackgroudDra(grade_bottom_image,  ChooseStaffResKey.bottomCheckDraKey,context);
        } else {
            grade_view.getNumberDeatilText().setText("");
            grade_view.getNumberText().setVisibility(View.GONE);
            gradeHeadNumberRel.setClickable(false);
            GradeViewHelper.setBackgroudDra(grade_bottom_image, ChooseStaffResKey.bottomUnCheckDraKey,context);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        GradeViewHelper.haveCheckedList.clear();
        GradeViewHelper.currentResources=null;
        GradeViewHelper.packageName = null;
    }
}
