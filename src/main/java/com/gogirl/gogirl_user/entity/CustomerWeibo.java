package com.gogirl.gogirl_user.entity;

import java.util.Date;

public class CustomerWeibo {
    private Integer customerId;

    private String id;

    private String screenName;

    private String name;

    private Integer province;

    private Integer city;

    private String location;

    private String description;

    private String url;

    private String profileimageUrl;

    private String userDomain;

    private String gender;

    private Integer followersCount;

    private Integer friendsCount;

    private Integer statusesCount;

    private Integer favouritesCount;

    private Date createdAt;

    private Boolean following;

    private Boolean verified;

    private Integer verifiedType;

    private Boolean allowAllActAsg;

    private Boolean allowAllComment;

    private Boolean followMe;

    private String avatarLarge;

    private Integer onlineStatus;

    private Integer biFollowersCount;

    private String remark;

    private String lang;

    private String verifiedReason;

    private String weihao;

    private String statusId;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName == null ? null : screenName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getProfileimageUrl() {
        return profileimageUrl;
    }

    public void setProfileimageUrl(String profileimageUrl) {
        this.profileimageUrl = profileimageUrl == null ? null : profileimageUrl.trim();
    }

    public String getUserDomain() {
        return userDomain;
    }

    public void setUserDomain(String userDomain) {
        this.userDomain = userDomain == null ? null : userDomain.trim();
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender == null ? null : gender.trim();
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(Integer friendsCount) {
        this.friendsCount = friendsCount;
    }

    public Integer getStatusesCount() {
        return statusesCount;
    }

    public void setStatusesCount(Integer statusesCount) {
        this.statusesCount = statusesCount;
    }

    public Integer getFavouritesCount() {
        return favouritesCount;
    }

    public void setFavouritesCount(Integer favouritesCount) {
        this.favouritesCount = favouritesCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getFollowing() {
        return following;
    }

    public void setFollowing(Boolean following) {
        this.following = following;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Integer getVerifiedType() {
        return verifiedType;
    }

    public void setVerifiedType(Integer verifiedType) {
        this.verifiedType = verifiedType;
    }

    public Boolean getAllowAllActAsg() {
        return allowAllActAsg;
    }

    public void setAllowAllActAsg(Boolean allowAllActAsg) {
        this.allowAllActAsg = allowAllActAsg;
    }

    public Boolean getAllowAllComment() {
        return allowAllComment;
    }

    public void setAllowAllComment(Boolean allowAllComment) {
        this.allowAllComment = allowAllComment;
    }

    public Boolean getFollowMe() {
        return followMe;
    }

    public void setFollowMe(Boolean followMe) {
        this.followMe = followMe;
    }

    public String getAvatarLarge() {
        return avatarLarge;
    }

    public void setAvatarLarge(String avatarLarge) {
        this.avatarLarge = avatarLarge == null ? null : avatarLarge.trim();
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public Integer getBiFollowersCount() {
        return biFollowersCount;
    }

    public void setBiFollowersCount(Integer biFollowersCount) {
        this.biFollowersCount = biFollowersCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang == null ? null : lang.trim();
    }

    public String getVerifiedReason() {
        return verifiedReason;
    }

    public void setVerifiedReason(String verifiedReason) {
        this.verifiedReason = verifiedReason == null ? null : verifiedReason.trim();
    }

    public String getWeihao() {
        return weihao;
    }

    public void setWeihao(String weihao) {
        this.weihao = weihao == null ? null : weihao.trim();
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId == null ? null : statusId.trim();
    }
}