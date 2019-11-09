package com.gogirl.gogirl_user.entity;

import java.util.Date;

public class CustomerDepartmentRelevance extends CustomerDepartmentRelevanceKey {
    private Integer relevanceSource;

    private Date relevanceTime;

    public Integer getRelevanceSource() {
        return relevanceSource;
    }

    public void setRelevanceSource(Integer relevanceSource) {
        this.relevanceSource = relevanceSource;
    }

    public Date getRelevanceTime() {
        return relevanceTime;
    }

    public void setRelevanceTime(Date relevanceTime) {
        this.relevanceTime = relevanceTime;
    }
}