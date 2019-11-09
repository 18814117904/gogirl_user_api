package com.gogirl.gogirl_user.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gogirl.gogirl_user.entity.StoreUser;
@Mapper
public interface CountMapper {
	Integer countCustomerBalanceNum(@Param("departmentId")Integer departmentId, @Param("startTime")Date startTime,@Param("endTime")Date endTime);
	Integer countSumCharge(@Param("departmentId")Integer departmentId, @Param("startTime")Date startTime,@Param("endTime")Date endTime);
	Integer countSumBalance(@Param("departmentId")Integer departmentId, @Param("startTime")Date startTime,@Param("endTime")Date endTime);
	Integer countConsumeCustomerNum(@Param("departmentId")Integer departmentId, @Param("startTime")Date startTime,@Param("endTime")Date endTime);
	Integer countCustomerNum(@Param("departmentId")Integer departmentId, @Param("startTime")Date startTime,@Param("endTime")Date endTime);
	List<Map<String, Object>> countCategorySalesRatio(@Param("departmentId")Integer departmentId, @Param("startTime")Date startTime,@Param("endTime")Date endTime);
	Double countRepeatPurchaseRate(@Param("departmentId")Integer departmentId, @Param("startTime")Date startTime,@Param("endTime")Date endTime);
	List<Map<String, Object>> countCustomerUnitPrice(@Param("departmentId")Integer departmentId, @Param("startTime")Date startTime,@Param("endTime")Date endTime);
	Integer countUnRecordCustomerInfoNum(@Param("departmentId")Integer departmentId);
}