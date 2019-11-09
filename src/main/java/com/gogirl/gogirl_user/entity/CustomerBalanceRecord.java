package com.gogirl.gogirl_user.entity;

import java.util.Date;

public class CustomerBalanceRecord {
    private Integer id;

    private Integer customerId;

    private Integer source;

    private Integer type;

    private Date time;

    private Integer currentBalance;

    private Integer orderAmount;

    private Double discount;

    private Integer bestowAmount;

    private String orderId;

    private Integer orderState;

    private String ip;
    private Integer departmentId;
    private String departmentName;
    
    private String remark;
    private String refereeId;
    private StoreUser referee;
    public CustomerBalanceRecord() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomerBalanceRecord(Integer customerId, Integer source,
			Integer type, Date time, Integer currentBalance,
			Integer orderAmount, String orderId, Integer orderState,Double discount,Integer bestowAmount,String remark) {
		super();
		this.customerId = customerId;
		this.source = source;
		this.type = type;
		this.time = time;
		this.currentBalance = currentBalance;
		this.orderAmount = orderAmount;
		this.orderId = orderId;
		this.orderState = orderState;
		this.discount = discount;
		this.bestowAmount = bestowAmount;
		this.remark = remark;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(Integer currentBalance) {
        this.currentBalance = currentBalance;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getBestowAmount() {
        return bestowAmount;
    }

    public void setBestowAmount(Integer bestowAmount) {
        this.bestowAmount = bestowAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
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

	public StoreUser getReferee() {
		return referee;
	}

	public void setReferee(StoreUser referee) {
		this.referee = referee;
	}

	public String getRefereeId() {
		return refereeId;
	}

	public void setRefereeId(String refereeId) {
		this.refereeId = refereeId;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}





    
}