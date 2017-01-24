package com.bonc.mobile.plugin.choosestaff;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bonc.mobile.plugin.utils.MResource;
import com.bonc.mobile.plugin.circleimage.CircleImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * Created by cuibg on 2016/10/27.
 * GradeAdapter适配器
 */

public class GradeViewAdapter extends RecyclerView.Adapter<GradeViewAdapter.GradeHolder> {
    private final int boxDefaultTag = -2;
    private String whichActivity;
    private int selectType;
    private TextView groupNameText;//部门显示名称
    private ImageView gradeGroupBackImage;//返回的图片还是顶层的图片
    private Node nodeData;//当前节点数据
    private Map<String, Node> nodeMap;//全部的数据
    private Context context;
    private GradeView gradeView; //自定义的view
    private ImageLoader imageLoader; //加载图片
    private List<String> childList = new ArrayList<String>();//子节点

    /**
     * 显示部门的构造器
     *
     * @param context
     * @param gradeView       自定义的分等级的view
     * @param nodeMap         封装后的数据
     * @param imageLoader     加载图片
     * @param selectType      单选还是多选
     * @param whichActivity   哪个activiy的适配器
     */
    public GradeViewAdapter(final Context context, GradeView gradeView, final Map<String, Node> nodeMap,  ImageLoader imageLoader, int selectType, String whichActivity) {
        this.context = context;
        this.gradeView = gradeView;
        this.nodeMap = nodeMap;
        this.selectType = selectType;
        this.whichActivity = whichActivity;
        this.imageLoader = imageLoader;

        if (GradeViewHelper.chooseStaffActivity.equals(whichActivity)) {//分部门界面的逻辑
            //默认是父节点下的数据
            this.nodeData = GradeViewHelper.getRootNode(nodeMap);
            childList = nodeData.getChildList();
        } else if (GradeViewHelper.scanSelectedActivity.equals(whichActivity)) {//预览界面的逻辑
            this.childList = GradeViewHelper.haveCheckedList;
        }
    }

    /**
     * 查询界面使用适配器
     *
     * @param context
     * @param nodeMap         所有的数据源
     * @param personMatchList 匹配的数据源id列表
     * @param selectType      单选或者是多选
     * @param imageLoader     加载图片
     */
    public GradeViewAdapter(Context context, Map<String, Node> nodeMap,  List<String> personMatchList, ImageLoader imageLoader, int selectType, String whichActivity) {
        this.context = context;
        this.nodeMap = nodeMap;
        this.imageLoader = imageLoader;
        this.childList = personMatchList;
        this.selectType = selectType;
        this.whichActivity = whichActivity;
    }

    @Override
    public GradeHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new GradeHolder(LayoutInflater.from(context).inflate(MResource.getIdByName(context, "layout", "grade_list_item"), viewGroup, false));
    }

    @Override
    public void onBindViewHolder(final GradeHolder gradeHolder, final int position) {
        final Node node = nodeMap.get(childList.get(position));
        //部门显示部门布局，人员显示人员布局
        if (!node.isLeaf()) {
            //设置显示部门布局，隐藏人员布局
            gradeHolder.grade_group_rl.setVisibility(VISIBLE);
            gradeHolder.grade_person_rl.setVisibility(GONE);
            gradeHolder.group_text.setText(node.getName());//设置显示部门
            gradeHolder.grade_group_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GradeViewHelper.setImageDra(gradeGroupBackImage, ChooseStaffResKey.groupBackIvKey,context); //设置返回图片
                    nodeData = node;
                    childList = nodeData.getChildList();
                    groupNameText.setText(nodeData.getName());
                    notifyDataSetChanged();
                    if(gradeView!=null){
                        gradeView.getRecycleView().scrollToPosition(0);
                    }
                }
            });
            // 加载部门显示图片
            String iconUrl = node.getIconUrl();
            //先于加载默认图标
            GradeViewHelper.setImageDra(gradeHolder.group_image, ChooseStaffResKey.adapterGroupIvKey,context);
            if (!TextUtils.isEmpty(iconUrl)) {
                imageLoader.get(iconUrl, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                        Bitmap bitmapContainer = imageContainer.getBitmap();
                        if (bitmapContainer != null) {
                            gradeHolder.group_image.setImageDrawable(new BitmapDrawable(bitmapContainer));
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        GradeViewHelper.setImageDra(gradeHolder.group_image, ChooseStaffResKey.adapterGroupIvKey,context);
                    }
                });
            }
        } else {
            //设置布局展示
            gradeHolder.grade_person_rl.setVisibility(VISIBLE);
            gradeHolder.grade_group_rl.setVisibility(GONE);
            gradeHolder.person_name_text.setText(node.getName());//人员的名字
            gradeHolder.person_jobnum_text.setText(node.getJobNumber());//人员的工号
            //预览状态下，checkbox消失，删除按钮现
            if (GradeViewHelper.scanSelectedActivity.equals(whichActivity)) {
                gradeHolder.person_checkbox.setVisibility(GONE);
                gradeHolder.person_delete_image.setVisibility(VISIBLE);
                GradeViewHelper.setImageDra(gradeHolder.person_delete_image, ChooseStaffResKey.adapterDeleteKey,context);////设置显示可删除的图片
                gradeHolder.person_delete_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int current_position = gradeHolder.getLayoutPosition();
                        childList.remove(current_position);
                        int size = childList.size();
                        ((ScanSelectedActivity) context).setBottomState(size);//点击删除的时候设置底部状态
                        notifyDataSetChanged();
                    }
                });
            } else {
                gradeHolder.person_checkbox.setVisibility(VISIBLE);
                gradeHolder.person_delete_image.setVisibility(GONE);
                //人员换选中或者删除
                if (GradeViewHelper.haveCheckedList != null && GradeViewHelper.haveCheckedList.contains(node.getId())) {
                    gradeHolder.person_checkbox.setChecked(true);
                } else {
                    gradeHolder.person_checkbox.setChecked(false);
                }
                gradeHolder.person_checkbox.setTag(new Integer(position));//处理checkbox错乱问题
                gradeHolder.person_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int tag = (int) buttonView.getTag();
                        if (tag == boxDefaultTag) {
                            return;
                        }
                        if (isChecked) {
                            GradeViewHelper.haveCheckedList.add(node.getId());
                            //如果选择类型为单选，则直接跳出
                            if (selectType == GradeViewHelper.singleType) {
                                if (GradeViewHelper.chooseStaffActivity.equals(whichActivity)) {
                                    ((DisplayDeptActivity) context).passData();
                                } else if (GradeViewHelper.userSearchActivity.equals(whichActivity)) {
                                    ((SearchStaffActivity) context).passData();
                                }
                            }
                        } else {
                            GradeViewHelper.haveCheckedList.remove(node.getId());
                        }
                        if (!GradeViewHelper.userSearchActivity.equals(whichActivity)) {//在搜索的时候不执行这一步
                            ((DisplayDeptActivity) context).setBottomState();
                        }

                    }
                });
            }
            //人员图片
            String iconUrl = node.getIconUrl();
            GradeViewHelper.setImageDra(gradeHolder.person_image, ChooseStaffResKey.adapterUserIvKey,context);
            if (!TextUtils.isEmpty(iconUrl)) {
                imageLoader.get(iconUrl, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                        Bitmap bitmapContainer = imageContainer.getBitmap();
                        if (bitmapContainer != null) {
                            gradeHolder.person_image.setImageDrawable(new BitmapDrawable(bitmapContainer));
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        GradeViewHelper.setImageDra(gradeHolder.person_image, ChooseStaffResKey.adapterUserIvKey , context);
                    }
                });
            }

        }

    }

    @Override
    public int getItemCount() {
        return childList.size();
    }

    @Override
    public void onViewRecycled(GradeHolder holder) {
        CheckBox person_checkbox = holder.person_checkbox;
        person_checkbox.setTag(new Integer(boxDefaultTag));
        super.onViewRecycled(holder);
    }

    /**
     * holder部门的标记
     */
    public class GradeHolder extends RecyclerView.ViewHolder {

        private final ImageView  group_right_image, person_delete_image;
        private final CircleImageView group_image,person_image;
        private final TextView group_text, person_name_text, person_jobnum_text;
        private final RelativeLayout grade_group_rl, grade_person_rl;
        private final CheckBox person_checkbox;
        private final View adapterLine;

        public GradeHolder(View itemView) {
            super(itemView);
            grade_group_rl = ((RelativeLayout) itemView.findViewById(MResource.getIdByName(context, "id", "grade_group_rl")));//部门布局
            group_image = (CircleImageView) itemView.findViewById(MResource.getIdByName(context, "id", "group_image"));//部门头像
            group_right_image = (ImageView) itemView.findViewById(MResource.getIdByName(context, "id", "group_right_image"));
            group_text = (TextView) itemView.findViewById(MResource.getIdByName(context, "id", "group_text"));//名称
            //设置部门和颜色图标
            GradeViewHelper.setBackgroundColor(grade_group_rl, ChooseStaffResKey.adapterGroupBgColorKey);//item 背景色
            GradeViewHelper.setImageDra(group_image,  ChooseStaffResKey.adapterGroupIvKey ,context);
            GradeViewHelper.setImageDra(group_right_image,  ChooseStaffResKey.adapterRightIvKey,context);//右侧箭头图标
            GradeViewHelper.setTextColor(group_text,  ChooseStaffResKey.adapterGroupTxColorKey);//部门显示的字体颜色

            grade_person_rl = ((RelativeLayout) itemView.findViewById(MResource.getIdByName(context, "id", "grade_person_rl")));
            person_image = ((CircleImageView) itemView.findViewById(MResource.getIdByName(context, "id", "person_image")));//头像
            person_name_text = ((TextView) itemView.findViewById(MResource.getIdByName(context, "id", "person_name_text")));//名字
            person_jobnum_text = ((TextView) itemView.findViewById(MResource.getIdByName(context, "id", "person_jobnum_text")));//部门
            person_checkbox = ((CheckBox) itemView.findViewById(MResource.getIdByName(context, "id", "person_checkbox")));//状态
            person_checkbox.setTag(new Integer(boxDefaultTag));//为了处理checkbox出乱问题
            adapterLine = itemView.findViewById(MResource.getIdByName(context, "id", "adapterLine"));
            person_delete_image = ((ImageView) itemView.findViewById(MResource.getIdByName(context, "id", "person_delete_image")));//删除
            //设置用户颜色和字体图标
            GradeViewHelper.setBackgroundColor(grade_person_rl,  ChooseStaffResKey.adapterUserBgColorKey);//user item背景色
            GradeViewHelper.setImageDra(person_image,  ChooseStaffResKey.adapterUserIvKey,context);//用户默认图标
            GradeViewHelper.setImageDra(person_delete_image, ChooseStaffResKey.adapterDeleteKey,context);//删除用户图标
            GradeViewHelper.setTextColor(person_name_text,  ChooseStaffResKey.adapterUserNameTxColorKey);//用户名字颜色
            GradeViewHelper.setTextColor(person_jobnum_text,  ChooseStaffResKey.adapterUserNumTxColorKey);//用户工号
            GradeViewHelper.setCheckedButtonDrawable(person_checkbox,  ChooseStaffResKey.checkboxBgKey,context);//checkbox 背景
            GradeViewHelper.setBackgroundColor(adapterLine, ChooseStaffResKey.gradeSplitLineColorKey);//适配器中那条线的颜色

        }
    }

    /**
     * 对部门返回处理监听
     */
    public void setGroupBackImageListener() {
        gradeGroupBackImage = gradeView.getGradeGroupBackImage();
        groupNameText = gradeView.getGroupNameText();
        groupNameText.setText(nodeData.getName());
        gradeGroupBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pId = nodeData.getpId();
                if (TextUtils.isEmpty(pId)) {
                    GradeViewHelper.setImageDra(gradeGroupBackImage,  ChooseStaffResKey.groupDraKey,context);//设置默认图片
                } else {
                    nodeData = nodeMap.get(pId);
                    if (nodeData.getId().equals(GradeViewHelper.getRootNode(nodeMap).getId())) {
                        GradeViewHelper.setImageDra(gradeGroupBackImage, ChooseStaffResKey.groupDraKey,context);//设置默认图片
                    } else {
                        GradeViewHelper.setImageDra(gradeGroupBackImage,  ChooseStaffResKey.groupBackIvKey,context);//设置返回图片
                    }
                }
                childList = nodeData.getChildList();
                notifyDataSetChanged();
                groupNameText.setText(nodeData.getName());
            }
        });
    }
}