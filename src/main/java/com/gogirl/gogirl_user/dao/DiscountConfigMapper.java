package com.gogirl.gogirl_user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.DiscountConfig;
@Mapper
public interface DiscountConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(DiscountConfig record);

    int insertSelective(DiscountConfig record);

    DiscountConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DiscountConfig record);

    int updateByPrimaryKey(DiscountConfig record);
    
	List<DiscountConfig> selectAllDiscount();
	DiscountConfig selectByTotalCharge(int totalCharge);
	DiscountConfig selectByCharge(int amount);
}