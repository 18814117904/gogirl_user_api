package com.gogirl.gogirl_user.entity;

public class DiscountConfig {
    private Integer id;

    private Integer totalCharge;

    private Integer chargeAmount;

    private Integer bestowAmount;

    private Double discount;

    private String level;

    private String remark;

    public Integer getId() {
        return id;
    }

    public DiscountConfig() {
		super();
	}



    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTotalCharge() {
        return totalCharge;
    }

    public void setTotalCharge(Integer totalCharge) {
        this.totalCharge = totalCharge;
    }

    public Integer getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(Integer chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public Integer getBestowAmount() {
        return bestowAmount;
    }

    public void setBestowAmount(Integer bestowAmount) {
        this.bestowAmount = bestowAmount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level == null ? null : level.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	@Override
	public String toString() {
		return "DiscountConfig [id=" + id + ", totalCharge=" + totalCharge
				+ ", chargeAmount=" + chargeAmount + ", bestowAmount="
				+ bestowAmount + ", discount=" + discount + ", level=" + level
				+ ", remark=" + remark + "]";
	}
    
}