package com.gogirl.gogirl_user.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gogirl.gogirl_user.dao.CustomerIntegralMapper;
import com.gogirl.gogirl_user.dao.CustomerIntegralRecordMapper;
import com.gogirl.gogirl_user.dao.CustomerMapper;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;
import com.gogirl.gogirl_user.entity.CustomerIntegral;
import com.gogirl.gogirl_user.entity.CustomerIntegralRecord;
import com.gogirl.gogirl_user.service.integral.CalculateIntegral;
import com.gogirl.gogirl_user.service.integral.PlusIntegral;
import com.gogirl.gogirl_user.util.IntegralLock;
import com.gogirl.gogirl_user.util.IntegralOrderLock;
@Service
public class CustomerIntegralService {
	@Autowired
	CustomerIntegralMapper integralDao;
	@Autowired
	CustomerIntegralRecordMapper integralRecordDao;
	@Autowired
	CustomerMapper customerMapper;
	IntegralOrderLock integralOrderLock = IntegralOrderLock.getInsatance();
	IntegralLock integralLock = IntegralLock.getInsatance();
	
	@Transactional
	public int integralChange(int customer_id,int value,CalculateIntegral calculateIntegral,int type,String orderId) throws Exception {
		integralOrderLock.lock(orderIdGetInt(orderId));
		integralLock.lock(customer_id);
//		查询该标识状态：未处理、处理中、已处理、已回滚
//		1.该笔交易异常：有该标识&&处理中
//		2.结束且返回已处理：有该标识&&已处理、
//		3.进入处理程序：无标识、有该标识&&已回滚
		CustomerIntegralRecord record = integralRecordDao.selectByOrderId(orderId);
		if(record!=null){
			if(record.getOrderState()==2){
				throw new Exception("订单处理中");
			}else if(record.getOrderState()==3){
				throw new Exception("订单已处理，请勿重复提交");
			}else if(record.getOrderState()==1||record.getOrderState()==4){
				//end
			}else{
				throw new Exception("订单状态异常");
			}
		}
		
		
		CustomerIntegral integral = integralDao.selectByPrimaryKey(customer_id);//若数据库不存在该对象，则生成
		if(integral==null){
			integral = new CustomerIntegral(customer_id, 0, new Date(), new Date(), 0, 0, 0);
		}

		calculateIntegral.setIntegral(integral);//设置当前积分
		
		CustomerIntegral finalIntegral = calculateIntegral.calculateIntegral(value);//根据类型计算余额
		calculateIntegral.saveRecord(value,orderId);//充值\消费记录保存到数据库
		int row = 0;
		if(integral.getVersion()==1){//插入该用户余额
			row = integralDao.insert(finalIntegral);
		}else{//更新该用户余额
			row = integralDao.updateByPrimaryKeySelective(finalIntegral);
		}
		integralLock.unlock(customer_id);//为用户id解锁
		integralOrderLock.unlock(orderIdGetInt(orderId));
		return row;
	}
	public CustomerIntegral getIntegral(int id) {
		CustomerIntegral integral = integralDao.selectByPrimaryKey(id);
		if(integral==null){
			integral=new CustomerIntegral(id, 0, null, null, 0, 0, 0);
		}
		return integral;
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

}
