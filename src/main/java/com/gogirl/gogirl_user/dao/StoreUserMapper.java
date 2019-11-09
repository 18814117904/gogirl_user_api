package com.gogirl.gogirl_user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.StoreUser;
@Mapper
public interface StoreUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StoreUser record);

    int insertSelective(StoreUser record);

    StoreUser selectByPrimaryKey(Integer id);
    List<StoreUser> getAllStoreUser();

    int updateByPrimaryKeySelective(StoreUser record);

    int updateByPrimaryKeyWithBLOBs(StoreUser record);

    int updateByPrimaryKey(StoreUser record);
}