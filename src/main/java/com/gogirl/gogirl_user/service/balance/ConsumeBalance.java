package com.gogirl.gogirl_user.service.balance;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.gogirl.gogirl_user.constant.Constant;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;
import com.gogirl.gogirl_user.entity.DiscountConfig;

@Component
public class ConsumeBalance  extends CalculateBalance {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public CustomerBalance calculateBalance(int amount) throws Exception{
//		if(balance.getCurrentDiscount()!=null&&balance.getCurrentDiscount()!=0){
//			logger.info("消费金额："+amount+"，当前折扣："+balance.getCurrentDiscount());
//			amount = (int)Math.floor(balance.getCurrentDiscount()*amount);
//			logger.info("折后价："+amount);
//		}else{
//			logger.info("不打折");
//		}
		if(balance.getBalance()<amount){
			throw new Exception("余额不足");
		}
		orderAmount = amount;
		balance.setBalance(balance.getBalance()-amount);
		balance.setUpdateTime(new Date());
		balance.setTotalExpenditure(balance.getTotalExpenditure()+amount);
		balance.setVersion(balance.getVersion()+1);
		return balance;
	}

	@Override
	public int saveRecord(int charge_balance,String orderId) {
		CustomerBalanceRecord customerBalanceRecord = new CustomerBalanceRecord(
				balance.getCustomerId(), Constant.CONSUME_BALANCE, -1, new Date(), 
				balance.getBalance(),orderAmount,orderId,3,
				balance.getCurrentDiscount(),0,remark);
		customerBalanceRecord.setDepartmentId(departmentId);
		balanceRecordDao.insertSelective(customerBalanceRecord);
		return customerBalanceRecord.getId();
	}

}
