package com.gogirl.gogirl_user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.Coupon;

@Mapper
public interface CouponMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Coupon record);

    int insertSelective(Coupon record);

    Coupon selectByPrimaryKey(Integer id);
    List<Coupon> selectByCoupon(Coupon coupon);
    List<Coupon> getAllUseCoupon();
    
    int updateByPrimaryKeySelective(Coupon record);

    int updateByPrimaryKey(Coupon record);
    String getCouponIdFromConfig();
}