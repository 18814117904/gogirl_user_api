package com.gogirl.gogirl_user.entity;

import java.util.Date;

public class CustomerIntegral {
    private Integer customerId;

    private Integer integral;

    private Date firstIntegralTime;

    private Date updateTime;

    private Integer totalIntegral;

    private Integer totalUseIntegral;

    private Integer version;

    
    
    public CustomerIntegral(Integer customerId, Integer integral,
			Date firstIntegralTime, Date updateTime, Integer totalIntegral,
			Integer totalUseIntegral, Integer version) {
		super();
		this.customerId = customerId;
		this.integral = integral;
		this.firstIntegralTime = firstIntegralTime;
		this.updateTime = updateTime;
		this.totalIntegral = totalIntegral;
		this.totalUseIntegral = totalUseIntegral;
		this.version = version;
	}

	public CustomerIntegral() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public Date getFirstIntegralTime() {
        return firstIntegralTime;
    }

    public void setFirstIntegralTime(Date firstIntegralTime) {
        this.firstIntegralTime = firstIntegralTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getTotalIntegral() {
        return totalIntegral;
    }

    public void setTotalIntegral(Integer totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public Integer getTotalUseIntegral() {
        return totalUseIntegral;
    }

    public void setTotalUseIntegral(Integer totalUseIntegral) {
        this.totalUseIntegral = totalUseIntegral;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}