package com.gogirl.gogirl_user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gogirl.gogirl_user.entity.CustomerDepartmentRelevance;
import com.gogirl.gogirl_user.entity.CustomerDepartmentRelevanceKey;
@Mapper
public interface CustomerDepartmentRelevanceMapper {
    int deleteByPrimaryKey(CustomerDepartmentRelevanceKey key);

    int insert(CustomerDepartmentRelevance record);

    int insertSelective(CustomerDepartmentRelevance record);

    CustomerDepartmentRelevance selectByPrimaryKey(CustomerDepartmentRelevanceKey key);

    int updateByPrimaryKeySelective(CustomerDepartmentRelevance record);

    int updateByPrimaryKey(CustomerDepartmentRelevance record);
    int mergeCustomer(@Param("fromCustomerId")Integer fromCustomerId,@Param("toCustomerId")Integer toCustomerId);
}