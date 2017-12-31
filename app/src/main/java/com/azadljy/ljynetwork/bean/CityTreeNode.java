package com.azadljy.ljynetwork.bean;

import com.azadljy.ljynetwork.city.Cityinfo;

/**
 * 作者：Ljy on 2017/12/31.
 * 功能：
 */


public class CityTreeNode {
    private Cityinfo cityinfo;
    private CityTreeNode children;
    private CityTreeNode sibling;

    public Cityinfo getCityinfo() {
        return cityinfo;
    }

    public void setCityinfo(Cityinfo cityinfo) {
        this.cityinfo = cityinfo;
    }

    public CityTreeNode getChildren() {
        return children;
    }

    public void setChildren(CityTreeNode children) {
        this.children = children;
    }

    public CityTreeNode getSibling() {
        return sibling;
    }

    public void setSibling(CityTreeNode sibling) {
        this.sibling = sibling;
    }
}
