package com.gogirl.gogirl_user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gogirl.gogirl_user.entity.ActivityChanceCustomer;
@Mapper
public interface ActivityChanceCustomerMapper {
    int deleteByPrimaryKey(Integer customerId);

    int insert(ActivityChanceCustomer record);

    int insertSelective(ActivityChanceCustomer record);

    ActivityChanceCustomer selectByPrimaryKey(Integer customerId);

    int updateByPrimaryKeySelective(ActivityChanceCustomer record);

    int updateByPrimaryKey(ActivityChanceCustomer record);

    int mergeCustomer(@Param("fromCustomerId")Integer fromCustomerId,@Param("toCustomerId")Integer toCustomerId);

}