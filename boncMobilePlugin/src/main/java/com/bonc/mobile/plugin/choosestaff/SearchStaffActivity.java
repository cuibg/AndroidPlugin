package com.bonc.mobile.plugin.choosestaff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.bonc.mobile.plugin.utils.MResource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by cuibg on 2016/10/27.
 * 控件搜索人员界面
 */
public class SearchStaffActivity extends ChooseStaffBase implements View.OnClickListener {

    private GradeView gradeView;
    private RecyclerView recyclerView;
    private GradeViewAdapter gradeViewAdapter;
    private EditText searchText;
    private Map<String, Node> nodeMap;
    private List<String> personMatchList = new ArrayList<>();
    private int selectType = GradeViewHelper.multipleType;//选中的类型，1为单选，2为多选，默认多选
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
        if (intent != null) {
            nodeMap = ((Map<String, Node>) intent.getSerializableExtra(GradeViewHelper.nodeMap));
            selectType = intent.getIntExtra(GradeViewHelper.chooseType, GradeViewHelper.multipleType);
        }
    }

    public void initWidget() {
        gradeView.setBackGroudColor(context);
        gradeView.getBackView().setVisibility(View.GONE);//返回图片隐藏
        gradeView.getGradeGroupNameLin().setVisibility(View.GONE);//显示那一栏隐藏
        gradeView.getGradeSplitLine().setVisibility(View.GONE);//隐藏分割线
        gradeView.getGradeNumbeRel().setVisibility(View.GONE);//下部布局隐藏

        gradeView.getBackText().setOnClickListener(this);
        searchText = ((EditText) gradeView.getSerchText());
        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == 0) {
                    //收起软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    personMatchList.clear();
                    String editTextContent = v.getText().toString();
                    //拿到符合条目的人员的id
                    Iterator<Map.Entry<String, Node>> iterator = nodeMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, Node> entry = iterator.next();
                        String key = entry.getKey();
                        Node value = entry.getValue();
                        if (value.isLeaf()) {
                            if (TextUtils.isDigitsOnly(editTextContent)) {
                                String jobNumber = value.getJobNumber();
                                if (jobNumber.contains(editTextContent)) {
                                    personMatchList.add(key);
                                }
                            } else {
                                String name = value.getName();
                                if (name.contains(editTextContent)) {
                                    personMatchList.add(key);
                                }
                            }
                        }
                    }
                    gradeViewAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
        //列表展示
        recyclerView = gradeView.getRecycleView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        gradeViewAdapter = new GradeViewAdapter(this, nodeMap,  personMatchList, mVImageLoader, selectType, GradeViewHelper.userSearchActivity);
        recyclerView.setAdapter(gradeViewAdapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == MResource.getIdByName(context, "id", "grade_back_text")) {
            finish();
        }
    }

    /**
     * 传递数据到上一个activity，同时finish这个activity
     */
    public void passData() {
        Map<String, Node> haveCheckedMap = GradeViewHelper.getCheckedData(nodeMap);
        Intent intent = new Intent();
        intent.putExtra(GradeViewHelper.haveCheckedMapKey, (Serializable) haveCheckedMap);
        setResult(RESULT_OK, intent);
        finish();
    }
}
