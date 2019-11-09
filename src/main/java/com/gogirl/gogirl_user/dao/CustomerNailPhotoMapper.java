package com.gogirl.gogirl_user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.CustomerNailPhoto;
@Mapper
public interface CustomerNailPhotoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerNailPhoto record);

    int insertSelective(CustomerNailPhoto record);

    CustomerNailPhoto selectByPrimaryKey(Integer id);
    List<CustomerNailPhoto> selectByCustomerNailPhoto(CustomerNailPhoto record);

    int updateByPrimaryKeySelective(CustomerNailPhoto record);

    int updateByPrimaryKey(CustomerNailPhoto record);
}