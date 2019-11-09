package com.gogirl.gogirl_user.entity;

import java.util.Date;

public class CustomerDetail {
    private Integer customerId;

    private String wechatId;

    private String referee;

    private String firstServeTime;

    private Integer age;

    private String birthday;

    private String maritalStatus;

    private String haveChildren;

    private String hobby;

    private String colourOfSkin;

    private String typeOfNailBed;

    private String nailType;

    private Integer nailHardness;

    private Integer residence;

    private Integer workAddress;

    private Integer occupation;

    private Integer frequencyOfNail;

    private String keyPointsOfSalon;

    private String likeStyle;

    private String customerConcerns;

    private String solution;

    private String eyelashCondition;

    private String eyeShape;

    private String questionnaireImgUrl;
    private String learnAboutUs;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId == null ? null : wechatId.trim();
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee == null ? null : referee.trim();
    }


    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }



    public String getFirstServeTime() {
		return firstServeTime;
	}

	public void setFirstServeTime(String firstServeTime) {
		this.firstServeTime = firstServeTime;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus == null ? null : maritalStatus.trim();
    }

    public String getHaveChildren() {
        return haveChildren;
    }

    public void setHaveChildren(String haveChildren) {
        this.haveChildren = haveChildren == null ? null : haveChildren.trim();
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby == null ? null : hobby.trim();
    }

    public String getColourOfSkin() {
        return colourOfSkin;
    }

    public void setColourOfSkin(String colourOfSkin) {
        this.colourOfSkin = colourOfSkin == null ? null : colourOfSkin.trim();
    }

    public String getTypeOfNailBed() {
        return typeOfNailBed;
    }

    public void setTypeOfNailBed(String typeOfNailBed) {
        this.typeOfNailBed = typeOfNailBed == null ? null : typeOfNailBed.trim();
    }

    public String getNailType() {
        return nailType;
    }

    public void setNailType(String nailType) {
        this.nailType = nailType == null ? null : nailType.trim();
    }

    public Integer getNailHardness() {
        return nailHardness;
    }

    public void setNailHardness(Integer nailHardness) {
        this.nailHardness = nailHardness;
    }

    public Integer getResidence() {
        return residence;
    }

    public void setResidence(Integer residence) {
        this.residence = residence;
    }

    public Integer getWorkAddress() {
        return workAddress;
    }

    public void setWorkAddress(Integer workAddress) {
        this.workAddress = workAddress;
    }

    public Integer getOccupation() {
        return occupation;
    }

    public void setOccupation(Integer occupation) {
        this.occupation = occupation;
    }

    public Integer getFrequencyOfNail() {
        return frequencyOfNail;
    }

    public void setFrequencyOfNail(Integer frequencyOfNail) {
        this.frequencyOfNail = frequencyOfNail;
    }

    public String getKeyPointsOfSalon() {
        return keyPointsOfSalon;
    }

    public void setKeyPointsOfSalon(String keyPointsOfSalon) {
        this.keyPointsOfSalon = keyPointsOfSalon == null ? null : keyPointsOfSalon.trim();
    }

    public String getLikeStyle() {
        return likeStyle;
    }

    public void setLikeStyle(String likeStyle) {
        this.likeStyle = likeStyle == null ? null : likeStyle.trim();
    }

    public String getCustomerConcerns() {
        return customerConcerns;
    }

    public void setCustomerConcerns(String customerConcerns) {
        this.customerConcerns = customerConcerns == null ? null : customerConcerns.trim();
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution == null ? null : solution.trim();
    }

    public String getEyelashCondition() {
        return eyelashCondition;
    }

    public void setEyelashCondition(String eyelashCondition) {
        this.eyelashCondition = eyelashCondition == null ? null : eyelashCondition.trim();
    }

    public String getEyeShape() {
        return eyeShape;
    }

    public void setEyeShape(String eyeShape) {
        this.eyeShape = eyeShape == null ? null : eyeShape.trim();
    }

    public String getQuestionnaireImgUrl() {
        return questionnaireImgUrl;
    }

    public void setQuestionnaireImgUrl(String questionnaireImgUrl) {
        this.questionnaireImgUrl = questionnaireImgUrl == null ? null : questionnaireImgUrl.trim();
    }

	public String getLearnAboutUs() {
		return learnAboutUs;
	}

	public void setLearnAboutUs(String learnAboutUs) {
		this.learnAboutUs = learnAboutUs;
	}
    
}