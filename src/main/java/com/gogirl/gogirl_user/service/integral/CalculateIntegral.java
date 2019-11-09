package com.gogirl.gogirl_user.service.integral;

import javax.annotation.Resource;

import com.gogirl.gogirl_user.dao.CustomerIntegralRecordMapper;
import com.gogirl.gogirl_user.entity.CustomerIntegral;

public abstract class CalculateIntegral {
	CustomerIntegral integral;
	@Resource
	CustomerIntegralRecordMapper integralRecordDao;

	public CalculateIntegral() {
		super();
	}
	
	public CalculateIntegral(CustomerIntegral integral) {
		super();
		this.integral = integral;
	}


	public abstract CustomerIntegral calculateIntegral(int value);
	public abstract int saveRecord(int value,String orderId);


	public CustomerIntegral getIntegral() {
		return integral;
	}

	public void setIntegral(CustomerIntegral integral) {
		this.integral = integral;
	}


	
}
