package com.gogirl.gogirl_user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.CustomerIntegralRecord;
@Mapper
public interface CustomerIntegralRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerIntegralRecord record);

    int insertSelective(CustomerIntegralRecord record);

    CustomerIntegralRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerIntegralRecord record);

    int updateByPrimaryKey(CustomerIntegralRecord record);

	CustomerIntegralRecord selectByOrderId(String orderId);
}