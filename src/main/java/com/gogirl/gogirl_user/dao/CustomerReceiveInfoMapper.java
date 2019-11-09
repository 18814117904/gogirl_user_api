package com.gogirl.gogirl_user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gogirl.gogirl_user.entity.CustomerReceiveInfo;

@Mapper
public interface CustomerReceiveInfoMapper {
    int deleteByPrimaryKey(Integer customerId);

    int insert(CustomerReceiveInfo record);

    int insertSelective(CustomerReceiveInfo record);

    CustomerReceiveInfo selectByPrimaryKey(Integer customerId);

    int updateByPrimaryKeySelective(CustomerReceiveInfo record);

    int updateByPrimaryKey(CustomerReceiveInfo record);
    int mergeCustomer(@Param("fromCustomerId")Integer fromCustomerId,@Param("toCustomerId")Integer toCustomerId);
}