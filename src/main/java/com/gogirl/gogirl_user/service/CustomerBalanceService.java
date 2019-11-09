package com.gogirl.gogirl_user.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.gogirl.gogirl_user.constant.GogirlProperties;
import com.gogirl.gogirl_user.controller.DiscountConfigController;
import com.gogirl.gogirl_user.dao.CustomerBalanceMapper;
import com.gogirl.gogirl_user.dao.CustomerBalanceRecordMapper;
import com.gogirl.gogirl_user.dao.CustomerIntegralMapper;
import com.gogirl.gogirl_user.dao.CustomerMapper;
import com.gogirl.gogirl_user.dao.DiscountConfigMapper;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
import com.gogirl.gogirl_user.entity.Customer;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;
import com.gogirl.gogirl_user.entity.DiscountConfig;
import com.gogirl.gogirl_user.entity.JsonResult;
import com.gogirl.gogirl_user.service.balance.CalculateBalance;
import com.gogirl.gogirl_user.service.balance.ChargeBalance;
import com.gogirl.gogirl_user.util.BalanceLock;
import com.gogirl.gogirl_user.util.BalanceOrderLock;
@Service
public class CustomerBalanceService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	CustomerBalanceMapper balanceDao;
	@Autowired
	CustomerBalanceRecordMapper balanceRecordDao;
	@Autowired
	DiscountConfigController discountConfigController;
	@Autowired
	CouponService couponService;
	@Autowired
	CustomerMapper customerMapper;
	BalanceOrderLock balanceOrderLock = BalanceOrderLock.getInsatance();
	BalanceLock balanceLock = BalanceLock.getInsatance();
	//余额变动
	@Transactional
	public int balanceChange(HttpServletRequest request,Integer customer_id,Integer charge_balance,CalculateBalance calculateBalance,Integer type,String orderId,Integer couponRelevanceId) throws Exception {
		balanceOrderLock.lock(orderIdGetInt(orderId));//为订单id上锁，防多次提交
		balanceLock.lock(customer_id);//为用户id上锁，防并发修改同一用户
//		查询该标识状态：未处理、处理中、已处理、已回滚
//		1.该笔交易异常：有该标识&&处理中
//		2.结束且返回已处理：有该标识&&已处理、
//		3.进入处理程序：无标识、有该标识&&已回滚
		int row = 0;
		try {
		CustomerBalanceRecord record = balanceRecordDao.selectByOrderId(orderId);
		if(record!=null){
			if(record.getOrderState()==2){
				balanceLock.unlock(customer_id);//为用户id解锁
				balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
				throw new Exception("订单处理中");
			}else if(record.getOrderState()==3){
				balanceLock.unlock(customer_id);//为用户id解锁
				balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
				throw new Exception("订单已处理，请勿重复提交");
			}else if(record.getOrderState()==1||record.getOrderState()==4){
				//end
			}else{
				balanceLock.unlock(customer_id);//为用户id解锁
				balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
				throw new Exception("订单状态异常");
			}
		}
		CustomerBalance balance = balanceDao.selectByCustomerId(customer_id);//若数据库不存在该对象，则生成
//		if(balance==null&&(charge_balance==null||charge_balance==0)){//如果没有会员卡,且订单金额为0,直接返回成功
//			logger.info("没有会员卡,且订单金额为0,直接返回成功");
//			balanceLock.unlock(customer_id);//为用户id解锁
//			balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
//			return 1;
//		}
//		if(balance==null){
//			balance = new CustomerBalance(customer_id, 0, new Date(), new Date(), 0, 0, 0,0.0,0,"普通会员");
//		}
//		calculateBalance.setCouponRelevance(couponService.selectRelevanceByPrimaryKey(couponRelevanceId));
		Double double_balance = charge_balance/100.0;
		JsonResult jsonResult = discountConfigController.countPay(customer_id,couponRelevanceId,double_balance);
		if(jsonResult.getSuccess()&&jsonResult.getData()!=null){
			 HashMap linkedHashMap = (HashMap) jsonResult.getData();	
			 double discountPrice = (double) linkedHashMap.get("discountPrice");
			charge_balance = (int) Math.ceil((double_balance-discountPrice)*100);
			calculateBalance.setDiscountPrice(discountPrice);
		}
		if(balance==null&&(charge_balance==null||charge_balance==0)){//如果没有会员卡,且订单金额为0,直接返回成功
			logger.info("没有会员卡,且订单金额为0,直接返回成功");
			balanceLock.unlock(customer_id);//为用户id解锁
			balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
			return 1;
		}
		if(balance==null&&calculateBalance.getType()!=null&&calculateBalance.getType()==-1&&calculateBalance.getSource()!=null&&calculateBalance.getSource()!=2){
			logger.info("没有会员卡,消费类型不为会员卡,直接返回成功");
			balanceLock.unlock(customer_id);//为用户id解锁
			balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
			return 1;
		}
		if(balance==null){
			balance = new CustomerBalance(customer_id, 0, new Date(), new Date(), 0, 0, 0,0.0,0,"普通会员");
		}
		calculateBalance.setBalance(balance);//设置当前余额
		
		
		
		
		CustomerBalance finalBalance = calculateBalance.calculateBalance(charge_balance);//根据类型计算余额
		int recordId = calculateBalance.saveRecord(calculateBalance.getOrderAmount(),orderId);//充值\消费记录保存到数据库
		calculateBalance.setRecordId(recordId);
			if(balance.getVersion()==1){//插入该用户余额
				row = balanceDao.insertSelective(finalBalance);
			}else{//更新该用户余额
				row = balanceDao.updateByPrimaryKeySelective(finalBalance);
			}
		} catch (Exception e) {
			logger.info(e==null?"e==null":e.getMessage());
			balanceLock.unlock(customer_id);//为用户id解锁
			balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
			throw e;
		}
		balanceLock.unlock(customer_id);//为用户id解锁
		balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
		return row;
	}
//	撤回收款加钱
	public JsonResult withdrawBalance(String orderId) {
		balanceOrderLock.lock(orderIdGetInt(orderId));//为订单id上锁，防多次提交
		//找到消费记录，拿到金额，删除消费记录
		CustomerBalanceRecord customerBalanceRecord = new CustomerBalanceRecord();
		customerBalanceRecord.setOrderId(orderId);
		List<CustomerBalanceRecord> list = balanceRecordDao.getBalanceRecord(customerBalanceRecord);
		int row = 0;
		if(list==null||list.size()<1){
			balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
			return new JsonResult(true,"撤回收款成功，无该订单扣款记录，不需要改余额表",null);
		}
		CustomerBalanceRecord customerRecordForId = list.get(0);
		if(customerRecordForId==null){
			balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
			return new JsonResult(true,"撤回收款成功，无该订单扣款记录，不需要改余额表",null);
		}
		balanceLock.lock(customerRecordForId.getCustomerId());//为用户id上锁，防并发修改同一用户
		//查余额
		CustomerBalance cb = getCustomerBalance2(customerRecordForId.getCustomerId());
		int sumWithdrawAmount = 0;
		for(int i=0;i<list.size();i++){
			CustomerBalanceRecord item = list.get(i);
			if(item==null){
				continue;
			}
			//加上扣款金额到余额
			sumWithdrawAmount=+item.getOrderAmount();
			
			//删除消费记录
			balanceRecordDao.deleteByPrimaryKey(item.getId());
			row++;
		}
		cb.setBalance(cb.getBalance()+sumWithdrawAmount);
		balanceDao.updateByPrimaryKey(cb);

		balanceLock.unlock(customerRecordForId.getCustomerId());//为用户id解锁
		balanceOrderLock.unlock(orderIdGetInt(orderId));//为订单id解锁
		return new JsonResult(true,"撤回收款成功，无该订单扣款记录，不需要改余额表",row);
	}
//	查询用户余额
	public CustomerBalance getCustomerBalance(int customer_id) {
		CustomerBalance balance = balanceDao.selectByCustomerId(customer_id);
		if(balance==null){
			balance=new CustomerBalance(customer_id, 0, null, null, 0, 0, 0,new Double(1),0,"普通会员");
		}
		return balance;
	}
//	查询用户余额
	public CustomerBalance getCustomerBalance2(int customer_id) {
		CustomerBalance balance = balanceDao.selectByCustomerId(customer_id);
		return balance;
	}
	//字符串中所有字符相加得到一个int
	public int orderIdGetInt(String orderId) {
		StringBuffer sb = new StringBuffer(orderId);
		int sum = 0;
		int length = sb.length();
		for(int i=0;i<length;i++){
			sum+=sb.charAt(i);
		}
		return sum;
	}

	public List<CustomerBalanceRecord> getBalanceRecord(CustomerBalanceRecord customerBalanceRecord) {
		return balanceRecordDao.getBalanceRecord(customerBalanceRecord);
	}
	public List<CustomerBalanceRecord> getBalanceRecordCard(CustomerBalanceRecord customerBalanceRecord) {
		return balanceRecordDao.getBalanceRecordCard(customerBalanceRecord);
	}
	
public int  mergeCustomer(int fromCustomerId,int toCustomerId) {
	CustomerBalance c = balanceDao.selectByCustomerId(toCustomerId);
	if(c!=null){
		logger.error("两目标customerId已经存在.");
		return 0;
	}else{
		CustomerBalance cc = new CustomerBalance();
		cc.setCustomerId(fromCustomerId);
		return balanceDao.mergeCustomer(fromCustomerId,toCustomerId);
	}
}
public int deleteRecordByPrimaryKey(int id) {
	return balanceRecordDao.deleteByPrimaryKey(id);
}
}
