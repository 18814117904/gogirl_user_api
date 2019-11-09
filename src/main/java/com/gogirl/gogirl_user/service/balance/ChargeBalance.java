package com.gogirl.gogirl_user.service.balance;

import java.util.Date;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gogirl.gogirl_user.constant.Constant;
import com.gogirl.gogirl_user.dao.CustomerBalanceMapper;
import com.gogirl.gogirl_user.dao.CustomerBalanceRecordMapper;
import com.gogirl.gogirl_user.dao.DiscountConfigMapper;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;
import com.gogirl.gogirl_user.entity.DiscountConfig;
@Component
public class ChargeBalance extends CalculateBalance {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	CustomerBalanceMapper balanceDao;
	
	
	@Override
	public CustomerBalance calculateBalance(int charge_balance) {//计算余额
		orderAmount = charge_balance;
		balance.setBalance(balance.getBalance()+charge_balance);
		balance.setUpdateTime(new Date());
		balance.setTotalCharge(balance.getTotalCharge()+charge_balance);
		balance.setVersion(balance.getVersion()+1);
		DiscountConfig discountConfig = discountConfigDao.selectByCharge(charge_balance);
		//冲送，且记录打折信息
		if(discountConfig!=null){
			balance.setBalance(balance.getBalance()+discountConfig.getBestowAmount());
			balance.setCurrentDiscount(discountConfig.getDiscount());
			balance.setTotalBestow(balance.getTotalBestow()+discountConfig.getBestowAmount());
			bestowAmount = discountConfig.getBestowAmount();//记录赠送金额，待会儿设置到交易记录
			balance.setLevel(discountConfig.getLevel());
		}else{
			logger.info("充值金额"+charge_balance+"找不到折扣配置，转为不送且不打折");
		}
		
		/*设置折扣率*/
		balance.setDiscountRate(countDiscountRate(balance.getCustomerId(),orderAmount,bestowAmount));

		return balance;
	}

	@Override
	public int saveRecord(int charge_balance,String orderId) {//保存记录到记录表
		CustomerBalanceRecord customerBalanceRecord = new CustomerBalanceRecord(
				balance.getCustomerId(), Constant.CHARGE_BALANCE, 1, new Date(), 
				balance.getBalance(),charge_balance,orderId,3,
				balance.getCurrentDiscount(),bestowAmount,remark);
		customerBalanceRecord.setDepartmentId(departmentId);
		balanceRecordDao.insertSelective(customerBalanceRecord);
		return customerBalanceRecord.getId();
	}


}
