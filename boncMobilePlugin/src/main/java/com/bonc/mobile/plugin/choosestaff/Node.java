package com.bonc.mobile.plugin.choosestaff;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cuibg on 2016/10/31.
 * 树形结构模型
 */

public class Node implements Serializable {
    private String id;
    private String pId;
    private String iconUrl;
    private String name;
    private String jobNumber;
    private List<String> childList;
    private boolean isLeaf;//是否是叶子节点

    public Node(List<String> childList, String iconUrl, String id, boolean isLeaf, String jobNumber, String name, String pId) {
        this.childList = childList;
        this.iconUrl = iconUrl;
        this.id = id;
        this.isLeaf = isLeaf;
        this.jobNumber = jobNumber;
        this.name = name;
        this.pId = pId;
    }

    public List<String> getChildList() {
        return childList;
    }

    public void setChildList(List<String> childList) {
        this.childList = childList;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
