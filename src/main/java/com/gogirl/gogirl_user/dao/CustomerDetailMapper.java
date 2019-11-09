package com.gogirl.gogirl_user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.CustomerDetail;
@Mapper
public interface CustomerDetailMapper {
    int deleteByPrimaryKey(Integer customerId);

    int insert(CustomerDetail record);

    int insertSelective(CustomerDetail record);

    CustomerDetail selectByPrimaryKey(Integer customerId);

    int updateByPrimaryKeySelective(CustomerDetail record);

    int updateByPrimaryKey(CustomerDetail record);
}