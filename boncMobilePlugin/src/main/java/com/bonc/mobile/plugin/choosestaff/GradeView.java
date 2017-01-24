package com.bonc.mobile.plugin.choosestaff;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bonc.mobile.plugin.utils.MResource;

/**
 * Created by cuibg on 2016/10/26.
 * 分层次的RecycleView列表
 * <p>
 *     1. Node构造数据结构
 *     2. GradeViewHelper 控件工具类，主要处理封装数据，设置背景色
 *     3. 颜色资源文件和drawable名字全放到grade_view_colors.xml中了，以来此xml
 *     4. ChooseStaffActivity控件主界面，ScanSlectedActivity浏览选中人员，UserSearchActivity用户搜索界面
 *     5. GradeViewAdapter GradeView的适配器
 *     6. CircleImageView,圆角imageview
 * </p>
 */

public class GradeView extends RelativeLayout {
    private RecyclerView grade_recyclerview;
    private ImageView grade_bottom_image, grade_image_back;
    private EditText grade_search_text;
    private TextView grade_group_name_text, grade_number_detail_text, grade_commit_text, grade_back_text, grade_number_text;
    private LinearLayout grade_group_name_lin;
    private RelativeLayout grade_number_rel, grade_head_number_rel;
    private ImageView grade_group_back_image;
    private LinearLayout grade_back_lin;
    private View grade_center_layout;
    private View gradview_layout;
    private View grade_split_line;

    public GradeView(Context context) {
        super(context);
        init(context, null);
    }

    public GradeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GradeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(final Context context, AttributeSet attrs) {
        final LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(MResource.getIdByName(context, "layout", "grade_view_layout"), this);
        gradview_layout = findViewById(MResource.getIdByName(context,"id","gradview_layout"));
        grade_back_lin = ((LinearLayout) findViewById(MResource.getIdByName(context, "id", "grade_back_lin")));
        grade_image_back = ((ImageView) findViewById(MResource.getIdByName(context, "id", "grade_image_back")));//返回图
        grade_search_text = (EditText) findViewById(MResource.getIdByName(context, "id", "grade_search_text"));//搜索
        grade_back_text = ((TextView) findViewById(MResource.getIdByName(context, "id", "grade_back_text")));//返回的字体
        grade_center_layout = findViewById(MResource.getIdByName(context,"id","grade_center_layout"));//中间的背景色
        grade_split_line = findViewById(MResource.getIdByName(context,"id","grade_split_line"));

        grade_group_name_lin = ((LinearLayout) findViewById(MResource.getIdByName(context, "id", "grade_group_name_lin")));//显示部门名称的内容

        grade_group_back_image = ((ImageView) findViewById(MResource.getIdByName(context, "id", "grade_group_back_image")));//返回上一级部门图片
        grade_group_name_text = ((TextView) findViewById(MResource.getIdByName(context, "id", "grade_group_name_text")));//部门名称
        grade_recyclerview = (RecyclerView) findViewById(MResource.getIdByName(context, "id", "grade_recyclerview"));//列表

        grade_bottom_image = (ImageView) findViewById(MResource.getIdByName(context, "id", "grade_bottom_image"));//人头
        grade_number_text = (TextView) findViewById(MResource.getIdByName(context, "id", "grade_number_text"));//人员数
        grade_number_detail_text = ((TextView) findViewById(MResource.getIdByName(context, "id", "grade_number_detail_text")));//显示具体人员信息
        grade_commit_text = (TextView) findViewById(MResource.getIdByName(context, "id", "grade_commit_text"));//提交


        grade_number_rel = ((RelativeLayout) findViewById(MResource.getIdByName(context, "id", "grade_number_rel")));//底部布局
        grade_head_number_rel = ((RelativeLayout) findViewById(MResource.getIdByName(context, "id", "grade_head_number_rel")));//显示人员个数和图标
    }

    /**
     * 返回tab的视图
     * @return
     */
    public LinearLayout getTabBar() {
        return grade_back_lin;
    }
    /**
     * 返回
     *
     * @return
     */
    public ImageView getBackView() {
        return grade_image_back;
    }


    /**
     * 得到搜索的EditText
     *
     * @return
     */
    public EditText getSerchText() {
        return grade_search_text;
    }

    /**
     * 返回字体显示
     *
     * @return
     */
    public TextView getBackText() {
        return grade_back_text;
    }


    /**
     * 返回显示部门的组件
     *
     * @return
     */
    public View getGradeGroupNameLin() {
        return grade_group_name_lin;
    }

    /**
     * 返回那条分割线
     * @return
     */
    public View getGradeSplitLine(){
        return grade_split_line;
    }
    /**
     * 返回上一级部门图片
     *
     * @return
     */
    public ImageView getGradeGroupBackImage() {
        return grade_group_back_image;
    }
    /**
     * 得到部门名称
     *
     * @return
     */
    public TextView getGroupNameText() {
        return grade_group_name_text;
    }

    /**
     * 得到布局中的RecyclerView
     *
     * @return RecyclerView
     */
    public RecyclerView getRecycleView() {
        return grade_recyclerview;
    }

    /**
     * 选中具体人员个数（具体详情）
     *
     * @return
     */
    public TextView getNumberDeatilText() {
        return grade_number_detail_text;
    }

    /**
     * 选中的人员的数目
     *
     * @return
     */
    public TextView getNumberText() {
        return grade_number_text;
    }

    /**
     * 提交
     *
     * @return
     */
    public TextView getCommitText() {
        return grade_commit_text;
    }

    /**
     * 底部布局
     *
     * @return
     */
    public View getGradeNumbeRel() {
        return grade_number_rel;
    }

    /**
     * 显示人员个数和图标的布局
     *
     * @return
     */
    public View getGradeHeadNumberRel() {
        return grade_head_number_rel;
    }

    /**
     * 底部头像
     * @return
     */
    public ImageView getGradeBottomImage(){
        return grade_bottom_image;
    }

    /**
     * 设置默认的颜色
     * @param context 上下文对象
     */
    public void setBackGroudColor(Context context){
        //背景色
        GradeViewHelper.setBackgroundColor(gradview_layout, ChooseStaffResKey.gradebgColorKey);
        GradeViewHelper.setBackgroundColor(grade_back_lin, ChooseStaffResKey.naviBgColorKey);
        GradeViewHelper.setBackgroundColor(grade_search_text, ChooseStaffResKey.searchBgColor);
        GradeViewHelper.setBackgroundColor(grade_group_name_lin, ChooseStaffResKey.groupNameBgColorKey);
        GradeViewHelper.setBackgroundColor(grade_split_line, ChooseStaffResKey.spliteColorKey);
        GradeViewHelper.setBackgroundColor(grade_number_detail_text, ChooseStaffResKey.detNumBgColorKey);
        GradeViewHelper.setBackgroundColor(grade_commit_text, ChooseStaffResKey.commitBgColorKey);
        //字体颜色
        GradeViewHelper.setTextColor(grade_search_text, ChooseStaffResKey.searchTxColorKey);
        GradeViewHelper.setTextColor(grade_back_text, ChooseStaffResKey.backTxColorKey);
        GradeViewHelper.setTextColor(grade_group_name_text, ChooseStaffResKey.groupNameColorKey);
        GradeViewHelper.setTextColor(grade_number_detail_text, ChooseStaffResKey.detNumTxColorKey);
        GradeViewHelper.setTextColor(grade_number_text, ChooseStaffResKey.numTxColorKey);
        GradeViewHelper.setTextColor(grade_commit_text, ChooseStaffResKey.commitTxColorKey);
        //背景图
        GradeViewHelper.setImageDra(grade_image_back, ChooseStaffResKey.backDraKey,context);
        GradeViewHelper.setImageDra(grade_group_back_image, ChooseStaffResKey.groupDraKey,context);
        GradeViewHelper.setBackgroudDra(grade_number_text, ChooseStaffResKey.numTxBgDra,context);
    }
}

