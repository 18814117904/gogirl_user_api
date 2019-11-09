package com.gogirl.gogirl_user.service.integral;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.gogirl.gogirl_user.constant.Constant;
import com.gogirl.gogirl_user.dao.CustomerIntegralRecordMapper;
import com.gogirl.gogirl_user.entity.CustomerIntegral;
import com.gogirl.gogirl_user.entity.CustomerIntegralRecord;
@Component
public class MinusIntegral extends CalculateIntegral {
	@Resource
	CustomerIntegralRecordMapper integralRecordDao;

	public MinusIntegral(CustomerIntegral integral) {
		super(integral);
	}
	
	public MinusIntegral() {
		super();
	}

	@Override
	public CustomerIntegral calculateIntegral(int minus_integral) {//计算余额
		integral.setIntegral(integral.getIntegral()-minus_integral);
		integral.setUpdateTime(new Date());
		integral.setTotalUseIntegral(integral.getTotalUseIntegral()+minus_integral);
		integral.setVersion(integral.getVersion()+1);
		return integral;
	}

	@Override
	public int saveRecord(int value,String orderId) {//保存记录到记录表
		CustomerIntegralRecord customerIntegralRecord = new CustomerIntegralRecord(
				integral.getCustomerId(), Constant.MINUS_INTEGRAL, -1,integral.getIntegral(), new Date(),value,orderId,3);
		return integralRecordDao.insertSelective(customerIntegralRecord);
	}


}
