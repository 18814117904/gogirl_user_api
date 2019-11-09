package com.gogirl.gogirl_user.entity;

import java.math.BigDecimal;
import java.util.Date;

public class StoreManage {
    private Integer id;

    private String storeNo;

    private String logo;

    private String name;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private String address;

    private String master;

    private Integer employeeNumber;

    private String customerServiceTelphone;

    private String contactName;

    private String contactTelphone;

    private Integer businessDay;

    private Date businessStartTime;

    private Date businessEndTime;

    private String remark;

    private Date createTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(String storeNo) {
        this.storeNo = storeNo == null ? null : storeNo.trim();
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master == null ? null : master.trim();
    }

    public Integer getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Integer employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getCustomerServiceTelphone() {
        return customerServiceTelphone;
    }

    public void setCustomerServiceTelphone(String customerServiceTelphone) {
        this.customerServiceTelphone = customerServiceTelphone == null ? null : customerServiceTelphone.trim();
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName == null ? null : contactName.trim();
    }

    public String getContactTelphone() {
        return contactTelphone;
    }

    public void setContactTelphone(String contactTelphone) {
        this.contactTelphone = contactTelphone == null ? null : contactTelphone.trim();
    }

    public Integer getBusinessDay() {
        return businessDay;
    }

    public void setBusinessDay(Integer businessDay) {
        this.businessDay = businessDay;
    }

    public Date getBusinessStartTime() {
        return businessStartTime;
    }

    public void setBusinessStartTime(Date businessStartTime) {
        this.businessStartTime = businessStartTime;
    }

    public Date getBusinessEndTime() {
        return businessEndTime;
    }

    public void setBusinessEndTime(Date businessEndTime) {
        this.businessEndTime = businessEndTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	@Override
	public String toString() {
		return "StoreManage [id=" + id + ", storeNo=" + storeNo + ", logo="
				+ logo + ", name=" + name + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", address=" + address
				+ ", master=" + master + ", employeeNumber=" + employeeNumber
				+ ", customerServiceTelphone=" + customerServiceTelphone
				+ ", contactName=" + contactName + ", contactTelphone="
				+ contactTelphone + ", businessDay=" + businessDay
				+ ", businessStartTime=" + businessStartTime
				+ ", businessEndTime=" + businessEndTime + ", remark=" + remark
				+ ", createTime=" + createTime + "]";
	}
    
}