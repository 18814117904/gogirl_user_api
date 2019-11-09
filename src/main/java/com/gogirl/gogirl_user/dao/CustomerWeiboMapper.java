package com.gogirl.gogirl_user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gogirl.gogirl_user.entity.CustomerWeibo;
@Mapper
public interface CustomerWeiboMapper {
    int deleteByPrimaryKey(Integer customerId);

    int insert(CustomerWeibo record);

    int insertSelective(CustomerWeibo record);

    CustomerWeibo selectByPrimaryKey(Integer customerId);

    int updateByPrimaryKeySelective(CustomerWeibo record);

    int updateByPrimaryKey(CustomerWeibo record);
    int mergeCustomer(@Param("fromCustomerId")Integer fromCustomerId,@Param("toCustomerId")Integer toCustomerId);
}