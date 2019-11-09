package com.gogirl.gogirl_user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.CustomerBalanceRecord;
@Mapper
public interface CustomerBalanceRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerBalanceRecord record);

    int insertSelective(CustomerBalanceRecord record);

    CustomerBalanceRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerBalanceRecord record);

    int updateByPrimaryKey(CustomerBalanceRecord record);

    CustomerBalanceRecord selectByOrderId(String orderId);

	List<CustomerBalanceRecord> getBalanceRecord(CustomerBalanceRecord customerBalanceRecord);
	List<CustomerBalanceRecord> getBalanceRecordCard(CustomerBalanceRecord customerBalanceRecord);
}