package com.gogirl.gogirl_user.service.balance;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gogirl.gogirl_user.dao.CustomerBalanceMapper;
import com.gogirl.gogirl_user.dao.CustomerBalanceRecordMapper;
import com.gogirl.gogirl_user.dao.DiscountConfigMapper;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;

public abstract class CalculateBalance {
	CustomerBalance balance;
	CouponCustomerRelevance couponRelevance;
	Double discountPrice;
	Integer bestowAmount;
	Integer orderAmount;
	Integer source;
	String referee;
	String remark;	
	Integer type;
	Integer recordId;
	Integer departmentId;
	
	@Resource
	CustomerBalanceRecordMapper balanceRecordDao;
	@Resource
	DiscountConfigMapper discountConfigDao;
	@Resource
	CustomerBalanceMapper customerBalanceMapper;
	
	
	public CalculateBalance() {
		super();
	}
	
	public CalculateBalance(CustomerBalance balance) {
		super();
		this.balance = balance;
	}


	public Double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public abstract CustomerBalance calculateBalance(int amount) throws Exception ;
	public abstract int saveRecord(int charge_balance,String orderId);


	public CustomerBalance getBalance() {
		return balance;
	}

	public void setBalance(CustomerBalance balance) {
		this.balance = balance;
	}
	public Integer getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Integer getBestowAmount() {
		return bestowAmount;
	}

	public void setBestowAmount(Integer bestowAmount) {
		this.bestowAmount = bestowAmount;
	}



	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}



	public String getReferee() {
		return referee;
	}

	public void setReferee(String referee) {
		this.referee = referee;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public CouponCustomerRelevance getCouponRelevance() {
		return couponRelevance;
	}

	public void setCouponRelevance(CouponCustomerRelevance couponRelevance) {
		this.couponRelevance = couponRelevance;
	}

//查到用户的记录,加起来算折扣率
	public double countDiscountRate(Integer customerId,Integer orderAmount,Integer bestowAmount) {
		CustomerBalanceRecord customerBalanceRecord = new CustomerBalanceRecord();
		customerBalanceRecord.setCustomerId(customerId);
		List<CustomerBalanceRecord> list = balanceRecordDao.getBalanceRecord(customerBalanceRecord);
		int row = list.size();
		int sumChargeInt = 0;
		if(orderAmount!=null){
			sumChargeInt = orderAmount;
		}
		int sumbestowInt = 0;
		if(bestowAmount!=null){
			sumbestowInt = bestowAmount;
		}
		for(int i=0;i<row;i++){
			if(list.get(i).getType()>0){
				sumChargeInt+=list.get(i).getOrderAmount();
				sumbestowInt+=list.get(i).getBestowAmount();
			}
		}
		if(sumChargeInt+sumChargeInt>0){
			return (sumChargeInt*1.0)/((sumChargeInt+sumbestowInt)*1.0);
		}else{
			return 0.0;
		}
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	
////根据总充和总送算折扣率
//	public double countDiscountRate(Integer customerId) {
//		
//	}
	
	
}
