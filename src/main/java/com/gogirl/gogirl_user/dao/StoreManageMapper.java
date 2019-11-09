package com.gogirl.gogirl_user.dao;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.StoreManage;

@Mapper
public interface StoreManageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StoreManage record);

    int insertSelective(StoreManage record);

    StoreManage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StoreManage record);

    int updateByPrimaryKey(StoreManage record);
}