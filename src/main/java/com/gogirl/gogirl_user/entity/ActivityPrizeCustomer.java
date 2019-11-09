package com.gogirl.gogirl_user.entity;

import java.util.Date;

public class ActivityPrizeCustomer {
    private Integer id;

    private Integer customerId;

    private Date createTime;

    private Integer prizeId;

    private Integer status;
    private Integer num;
    private Integer prizeTypeNum = 0;
    private Integer prizeCode;
    
    Customer customer;
    ActivityPrize activityPrize;
    
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Integer prizeId) {
        this.prizeId = prizeId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public ActivityPrize getActivityPrize() {
		return activityPrize;
	}

	public void setActivityPrize(ActivityPrize activityPrize) {
		this.activityPrize = activityPrize;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getPrizeTypeNum() {
		return prizeTypeNum;
	}

	public void setPrizeTypeNum(Integer prizeTypeNum) {
		this.prizeTypeNum = prizeTypeNum;
	}

	public Integer getPrizeCode() {
		return prizeCode;
	}

	public void setPrizeCode(Integer prizeCode) {
		this.prizeCode = prizeCode;
	}
    
}