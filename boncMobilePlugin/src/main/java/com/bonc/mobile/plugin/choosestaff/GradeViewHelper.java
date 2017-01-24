package com.bonc.mobile.plugin.choosestaff;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by cuibg on 2016/10/31.
 * 层级展示工具类
 */

public class GradeViewHelper {
    public static final String getCheckedListKey = "getCheckedListKey";
    public static final String chooseType = "chooseType";
    public static final int singleType = 1;//1为单选
    public static final int multipleType = 2;//2为多选
    public static final String nodeMap = "nodeMap";//封装好的map Key
    public static final String haveCheckedMapKey = "haveCheckedMapKey";//已经选中的人员Key
    public static final String userSearchActivity = "userSearchActivity";//搜索界面
    public static final String scanSelectedActivity = "scanSelectedActivity";//浏览界面
    public static final String chooseStaffActivity = "chooseStaffActivity";//选择人员界面

    public static String childKey = "CHILD";
    public static String idKey = "ID";
    public static String nameKey = "NAME";
    public static String pIdKey = "PID";
    public static String jobNumKey = "JOBNUM";
    public static String iconKey = "HEADIMAGE";
    public static String rootId;
    public static List<String> haveCheckedList = new ArrayList<String>();

    public static Resources currentResources;
    public static String packageName;

    /**
     * 构建数据模型
     *
     * @param list
     * @return 返回的是模型的数据Map<String,Node>
     */
    public static Map<String, Node> structureData(List<Object> list) {
        int size = list.size();
        Map<String, Node> structureMap = new HashMap<String, Node>();
        boolean isLeaf = false;
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = (Map<String, Object>) list.get(i);
            List<String> childList = new ArrayList<String>();
            if (map.containsKey(childKey)) {
                childList = (List<String>) map.get(childKey);
                Collections.sort(childList, new Comparator<String>() {
                    @Override
                    public int compare(String lhs, String rhs) {
                        return lhs.compareTo(rhs);
                    }
                });
                isLeaf = false;
            } else {
                isLeaf = true;
            }
            String id = (String) map.get(idKey);
            String pId = (String) map.get(pIdKey);
            String name = (String) map.get(nameKey);
            String jobNum = (String) map.get(jobNumKey);
            String iconUrl = (String) map.get(iconKey);
            Node node = new Node(childList, iconUrl, id, isLeaf, jobNum, name, pId);
            if (TextUtils.isEmpty(pId) && TextUtils.isEmpty(rootId)) {
                rootId = id;
            }
            structureMap.put(id, node);

        }
        return structureMap;
    }

    /**
     * 得到所有的叶子数据放入map
     *
     * @param nodeMap
     * @return 叶子数据
     */
    public static Map<String, Node> getLeafDatas(Map<String, Node> nodeMap) {
        Map<String, Node> leafDatas = new HashMap<String, Node>();
        Set<String> nodeMapKey = nodeMap.keySet();
        Iterator<String> iterator = nodeMapKey.iterator();
        while (iterator.hasNext()) {
            String leafKey = iterator.next();
            Node node = nodeMap.get(leafKey);
            if (node.isLeaf()) {
                leafDatas.put(GradeViewHelper.getInnerId(leafKey), node);
            }
        }
        return leafDatas;
    }

    /**
     * 得到根节点
     *
     * @return
     */
    public static Node getRootNode(Map<String, Node> nodeMap) {
        return nodeMap.get(rootId);
    }


    /**
     * 控件传出去的数据
     *
     * @param nodeDatasMap
     * @return
     */
    public static Map<String, Node> getCheckedData(Map<String, Node> nodeDatasMap) {
        if (GradeViewHelper.haveCheckedList != null) {
            int size = GradeViewHelper.haveCheckedList.size();
            HashMap<String, Node> haveCheckedMap = new HashMap<>();
            for (int i = 0; i < size; i++) {
                String userId = GradeViewHelper.haveCheckedList.get(i);
                haveCheckedMap.put(GradeViewHelper.getInnerId(userId), nodeDatasMap.get(userId));
            }
            return haveCheckedMap;
        }
        return null;
    }

    /**
     * 添加id前面的前缀
     */
    public static String addIdPrefix(String id) {
        String uId = "u" + id;
        return uId;
    }

    /**
     * 去除id前缀
     */
    public static String getInnerId(String uId) {
        String innerId = uId.replaceAll("[a-zA-Z]", "");
        return innerId;
    }


    /**
     * 通过资源名称获取对应的ID
     *
     * @param resName
     * @param resType
     * @return
     */
    public static int getResId(String resName, String resType) {
        if (currentResources != null) {
            return currentResources.getIdentifier(resName, resType, packageName);
        }
        return 0;
    }

    /**
     * 设置背景图片
     *
     * @param view   当前视图
     * @param draKey grade中xml资源名字
     */
    public static void setBackgroudDra(View view, String draKey, Context context) {
        if (currentResources != null) {
            int resourceId = getResId(draKey,"string");
            if (resourceId != 0) {
                String draName = currentResources.getString(resourceId);
                int id = getResId(draName, "drawable");
                if (id != 0) {
                    view.setBackgroundDrawable(currentResources.getDrawable(id));
                }
            }
        }
    }

    /**
     * imageDrawable
     *
     * @param imageView
     * @param draKey
     * @param context
     */
    public static void setImageDra(ImageView imageView, String draKey, Context context) {
        if (currentResources != null) {
            int resourceId = getResId(draKey,"string");
            if (resourceId != 0) {
                String draName = currentResources.getString(resourceId);
                int id = getResId(draName, "drawable");
                if (id != 0) {
                    imageView.setImageDrawable(currentResources.getDrawable(id));
                }
            }
        }
    }

    /**
     * 设置背景色
     *
     * @param view
     * @param colorName
     */
    public static void setBackgroundColor(View view, String colorName) {
        if (currentResources != null) {
            int color_id = getResId(colorName, "color");
            if (color_id != 0) {
                view.setBackgroundColor(currentResources.getColor(color_id));
            }
        }
    }

    /**
     * checkbox 背景显示
     *
     * @param checkBox
     * @param draKey
     */
    public static void setCheckedButtonDrawable(CheckBox checkBox, String draKey, Context context) {
        if (currentResources != null) {
            int resourceId = getResId(draKey,"string");
            if (resourceId != 0) {
                String draName = currentResources.getString(resourceId);
                int id = getResId(draName, "drawable");
                if (id != 0) {
                    checkBox.setButtonDrawable(currentResources.getDrawable(id));
                }
            }
        }
    }

    /**
     * 设置字体颜色
     *
     * @param textView
     * @param resName
     */
    public static void setTextColor(TextView textView, String resName) {
        if (currentResources != null) {
            int id = getResId(resName, "color");
            if (id != 0) {
                textView.setTextColor(currentResources.getColor(id));
            }
        }
    }

}
