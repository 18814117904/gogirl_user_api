package com.gogirl.gogirl_user.entity;

public class ActivityPrize {
    private Integer id;

    private String prizeName;

    private Integer issueObject;

    private String issueRules;

    private Double prizeWeight;

    private Integer prizeAmount;

    private Integer type;

    private Integer isFokas;
    
    private String picturePath;
    private String prizeRemark;

    private int num;
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName == null ? null : prizeName.trim();
    }

    public Integer getIssueObject() {
        return issueObject;
    }

    public void setIssueObject(Integer issueObject) {
        this.issueObject = issueObject;
    }

    public String getIssueRules() {
        return issueRules;
    }

    public void setIssueRules(String issueRules) {
        this.issueRules = issueRules == null ? null : issueRules.trim();
    }

    public Double getPrizeWeight() {
        return prizeWeight;
    }

    public void setPrizeWeight(Double prizeWeight) {
        this.prizeWeight = prizeWeight;
    }

    public Integer getPrizeAmount() {
        return prizeAmount;
    }

    public void setPrizeAmount(Integer prizeAmount) {
        this.prizeAmount = prizeAmount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsFokas() {
        return isFokas;
    }

    public void setIsFokas(Integer isFokas) {
        this.isFokas = isFokas;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath == null ? null : picturePath.trim();
    }

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getPrizeRemark() {
		return prizeRemark;
	}

	public void setPrizeRemark(String prizeRemark) {
		this.prizeRemark = prizeRemark;
	}
    
}