package com.gogirl.gogirl_user.entity;

import java.util.Date;

public class ActivityChanceCustomer {
    private Integer customerId;

    private Date lotteryTime;

    private Integer isBlessRegist;

    private Integer girlfriendsHelp;

    private Integer isSignIn;

    private Integer totalLottery;

    private Integer usedLottery;

    private Integer bigLottery;
    private Integer prizeCode;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Date getLotteryTime() {
        return lotteryTime;
    }

    public void setLotteryTime(Date lotteryTime) {
        this.lotteryTime = lotteryTime;
    }

    public Integer getIsBlessRegist() {
        return isBlessRegist;
    }

    public void setIsBlessRegist(Integer isBlessRegist) {
        this.isBlessRegist = isBlessRegist;
    }

    public Integer getGirlfriendsHelp() {
        return girlfriendsHelp;
    }

    public void setGirlfriendsHelp(Integer girlfriendsHelp) {
        this.girlfriendsHelp = girlfriendsHelp;
    }

    public Integer getIsSignIn() {
        return isSignIn;
    }

    public void setIsSignIn(Integer isSignIn) {
        this.isSignIn = isSignIn;
    }

    public Integer getTotalLottery() {
        return totalLottery;
    }

    public void setTotalLottery(Integer totalLottery) {
        this.totalLottery = totalLottery;
    }

    public Integer getUsedLottery() {
        return usedLottery;
    }

    public void setUsedLottery(Integer usedLottery) {
        this.usedLottery = usedLottery;
    }

    public Integer getBigLottery() {
        return bigLottery;
    }

    public void setBigLottery(Integer bigLottery) {
        this.bigLottery = bigLottery;
    }

	public Integer getPrizeCode() {
		return prizeCode;
	}

	public void setPrizeCode(Integer prizeCode) {
		this.prizeCode = prizeCode;
	}
    
}