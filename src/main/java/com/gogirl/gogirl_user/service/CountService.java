package com.gogirl.gogirl_user.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.junit.Test;
import org.springframework.stereotype.Service;

import com.gogirl.gogirl_user.dao.CountMapper;
import com.gogirl.gogirl_user.dao.DiscountConfigMapper;

@Service
public class CountService {
	@Resource
	CountMapper countMapper;

	public Integer countCustomerBalanceNum(Integer departmentId, Date startTime,
			Date endTime) {
		return countMapper.countCustomerBalanceNum(departmentId,startTime,endTime);
	}

	public Integer countSumCharge(Integer departmentId, Date startTime, Date endTime) {
		return countMapper.countSumCharge(departmentId,startTime,endTime);
	}

	public Integer countSumBalance(Integer departmentId, Date startTime,
			Date endTime) {
		return countMapper.countSumBalance(departmentId,startTime,endTime);
	}

	public Integer countConsumeCustomerNum(Integer departmentId, Date startTime,
			Date endTime) {
		return countMapper.countConsumeCustomerNum(departmentId,startTime,endTime);
	}

	public Integer countCustomerNum(Integer departmentId, Date startTime,
			Date endTime) {
		return countMapper.countCustomerNum(departmentId,startTime,endTime);
	}
	public List<Map<String, Object>> countCategorySalesRatio(Integer departmentId, Date startTime,
			Date endTime) {
		return countMapper.countCategorySalesRatio(departmentId,startTime,endTime);
	}
	public Double countRepeatPurchaseRate(Integer departmentId, Date startTime,
			Date endTime) {
		return countMapper.countRepeatPurchaseRate(departmentId,startTime,endTime);
	}
	public List<Map<String, Object>> countCustomerUnitPrice(Integer departmentId, Date startTime,
			Date endTime) {
		return countMapper.countCustomerUnitPrice(departmentId,startTime,endTime);
	}
	public Integer countUnRecordCustomerInfoNum(Integer departmentId) {
		return countMapper.countUnRecordCustomerInfoNum(departmentId);
	}

}
