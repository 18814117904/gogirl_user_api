package com.gogirl.gogirl_user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.CustomerIntegral;
@Mapper
public interface CustomerIntegralMapper {
    int deleteByPrimaryKey(Integer customerId);

    int insert(CustomerIntegral record);

    int insertSelective(CustomerIntegral record);

    CustomerIntegral selectByPrimaryKey(Integer customerId);

    int updateByPrimaryKeySelective(CustomerIntegral record);

    int updateByPrimaryKey(CustomerIntegral record);
}