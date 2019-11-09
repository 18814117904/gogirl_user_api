package com.gogirl.gogirl_user.service.balance;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gogirl.gogirl_user.constant.Constant;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;

@Component
public class ConsumeWechat  extends CalculateBalance {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public CustomerBalance calculateBalance(int amount) {
		logger.info("微信支付，不扣余额,不打折");
//		balance.setBalance(balance.getBalance()-amount);//微信支付，不扣余额,不打折
		balance.setUpdateTime(new Date());
		balance.setTotalExpenditure(balance.getTotalExpenditure()+amount);
		balance.setVersion(balance.getVersion()+1);
		orderAmount = amount;
		return balance;
	}

	@Override
	public int saveRecord(int charge_balance,String orderId) {
		CustomerBalanceRecord customerBalanceRecord = new CustomerBalanceRecord(
				balance.getCustomerId(), Constant.CONSUME_WECHAT, -1, new Date(), 
				balance.getBalance(),charge_balance,orderId,3,
				new Double(1),0,remark);
		customerBalanceRecord.setDepartmentId(departmentId);
		balanceRecordDao.insertSelective(customerBalanceRecord);
		return customerBalanceRecord.getId();
	}

}
