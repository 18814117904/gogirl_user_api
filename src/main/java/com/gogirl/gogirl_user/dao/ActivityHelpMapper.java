package com.gogirl.gogirl_user.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gogirl.gogirl_user.entity.ActivityHelp;

@Mapper
public interface ActivityHelpMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActivityHelp record);

    int insertSelective(ActivityHelp record);

    ActivityHelp selectByPrimaryKey(Integer id);
    ActivityHelp selectByActivityHelp(ActivityHelp ActivityHelp);

    int updateByPrimaryKeySelective(ActivityHelp record);

    int updateByPrimaryKey(ActivityHelp record);
    int mergeCustomer(@Param("fromCustomerId")Integer fromCustomerId,@Param("toCustomerId")Integer toCustomerId);
    int mergeCustomer2(@Param("fromCustomerId")Integer fromCustomerId,@Param("toCustomerId")Integer toCustomerId);
}