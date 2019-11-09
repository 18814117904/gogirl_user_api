package com.gogirl.gogirl_user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gogirl.gogirl_user.entity.CustomerBalance;
@Mapper
public interface CustomerBalanceMapper {
    int deleteByPrimaryKey(Integer customerId);

    int insert(CustomerBalance record);

    int insertSelective(CustomerBalance record);

    CustomerBalance selectByCustomerId(Integer customerId);

    int updateByPrimaryKeySelective(CustomerBalance record);

    int updateByPrimaryKey(CustomerBalance record);
    
    int mergeCustomer(@Param("fromCustomerId")Integer fromCustomerId,@Param("toCustomerId")Integer toCustomerId);
}