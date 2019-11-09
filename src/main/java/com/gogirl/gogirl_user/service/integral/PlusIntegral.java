package com.gogirl.gogirl_user.service.integral;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.gogirl.gogirl_user.constant.Constant;
import com.gogirl.gogirl_user.dao.CustomerIntegralRecordMapper;
import com.gogirl.gogirl_user.entity.CustomerIntegral;
import com.gogirl.gogirl_user.entity.CustomerIntegralRecord;
@Component
public class PlusIntegral extends CalculateIntegral {
	@Resource
	CustomerIntegralRecordMapper integralRecordDao;

	public PlusIntegral(CustomerIntegral integral) {
		super(integral);
	}
	
	public PlusIntegral() {
		super();
	}

	@Override
	public CustomerIntegral calculateIntegral(int plus_integral) {//计算余额
		integral.setIntegral(integral.getIntegral()+plus_integral);
		integral.setUpdateTime(new Date());
		integral.setTotalIntegral(integral.getTotalIntegral()+plus_integral);
		integral.setVersion(integral.getVersion()+1);
		return integral;
	}

	@Override
	public int saveRecord(int value,String orderId) {//保存记录到记录表
		CustomerIntegralRecord customerIntegralRecord = new CustomerIntegralRecord(
				integral.getCustomerId(), Constant.PLUS_INTEGRAL, 1,integral.getIntegral(), new Date(),value,orderId,3);
		return integralRecordDao.insertSelective(customerIntegralRecord);
	}


}
