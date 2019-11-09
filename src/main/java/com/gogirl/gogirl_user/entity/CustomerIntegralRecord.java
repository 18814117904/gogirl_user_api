package com.gogirl.gogirl_user.entity;

import java.util.Date;

public class CustomerIntegralRecord {
    private Integer id;

    private Integer customerId;

    private Integer source;

    private Integer type;

    private Integer currentIntegral;

    private Date updateTime;

    private Integer orderAmount;

    private String orderId;

    private String ip;

    private String remark;

    private Integer orderState;



	public CustomerIntegralRecord(Integer customerId, Integer source,
			Integer type, Integer currentIntegral, Date updateTime,
			Integer orderAmount, String orderId, Integer orderState) {
		super();
		this.customerId = customerId;
		this.source = source;
		this.type = type;
		this.currentIntegral = currentIntegral;
		this.updateTime = updateTime;
		this.orderAmount = orderAmount;
		this.orderId = orderId;
		this.orderState = orderState;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCurrentIntegral() {
        return currentIntegral;
    }

    public void setCurrentIntegral(Integer currentIntegral) {
        this.currentIntegral = currentIntegral;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }
}