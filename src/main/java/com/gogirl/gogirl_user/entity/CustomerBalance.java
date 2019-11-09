package com.gogirl.gogirl_user.entity;

import java.util.Date;

public class CustomerBalance {
    private Integer customerId;

    private Integer balance;

    private Double currentDiscount;

    private Date firstChargeTime;

    private Date updateTime;

    private Integer totalCharge;

    private Integer totalBestow;

    private Integer totalExpenditure;

    private Integer version;

    private String level;
    
    private String refereeId;
    private StoreUser referee;
    private Double discountRate;
    
    
    public CustomerBalance() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CustomerBalance(Integer customerId, Integer balance,
			Date firstChargeTime, Date updateTime, Integer totalCharge,
			Integer totalExpenditure, Integer version,Double currentDiscount,Integer totalBestow,String level) {
		super();
		this.customerId = customerId;
		this.balance = balance;
		this.firstChargeTime = firstChargeTime;
		this.updateTime = updateTime;
		this.totalCharge = totalCharge;
		this.totalExpenditure = totalExpenditure;
		this.version = version;
		this.currentDiscount = currentDiscount;
		this.totalBestow = totalBestow;
		this.level = level;
		
	}

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Double getCurrentDiscount() {
        return currentDiscount;
    }

    public void setCurrentDiscount(Double currentDiscount) {
        this.currentDiscount = currentDiscount;
    }

    public Date getFirstChargeTime() {
        return firstChargeTime;
    }

    public void setFirstChargeTime(Date firstChargeTime) {
        this.firstChargeTime = firstChargeTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(Integer totalCharge) {
        this.totalCharge = totalCharge;
    }

    public Integer getTotalBestow() {
        return totalBestow;
    }

    public void setTotalBestow(Integer totalBestow) {
        this.totalBestow = totalBestow;
    }

    public Integer getTotalExpenditure() {
        return totalExpenditure;
    }

    public void setTotalExpenditure(Integer totalExpenditure) {
        this.totalExpenditure = totalExpenditure;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }


	public String getRefereeId() {
		return refereeId;
	}
	public void setRefereeId(String refereeId) {
		this.refereeId = refereeId;
	}
	public StoreUser getReferee() {
		return referee;
	}
	public void setReferee(StoreUser referee) {
		this.referee = referee;
	}
	
	
	public Double getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}
	@Override
	public String toString() {
		return "CustomerBalance [customerId=" + customerId + ", balance="
				+ balance + ", currentDiscount=" + currentDiscount
				+ ", firstChargeTime=" + firstChargeTime + ", updateTime="
				+ updateTime + ", totalCharge=" + totalCharge
				+ ", totalBestow=" + totalBestow + ", totalExpenditure="
				+ totalExpenditure + ", version=" + version + ", level="
				+ level + "]";
	}
    
}