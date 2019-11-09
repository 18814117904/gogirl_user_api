package com.gogirl.gogirl_user.entity;

import java.util.Date;

public class ActivityHelp {
    private Integer id;

    private Integer helpId;

    private Integer acceptId;

    private Date helpTime;

    private Integer activityId;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getHelpId() {
        return helpId;
    }

    public void setHelpId(Integer helpId) {
        this.helpId = helpId;
    }

    public Integer getAcceptId() {
        return acceptId;
    }

    public void setAcceptId(Integer acceptId) {
        this.acceptId = acceptId;
    }

    public Date getHelpTime() {
        return helpTime;
    }

    public void setHelpTime(Date helpTime) {
        this.helpTime = helpTime;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}