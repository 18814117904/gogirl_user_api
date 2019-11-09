package com.gogirl.gogirl_user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.gogirl.gogirl_user.entity.ActivityPrize;
import com.gogirl.gogirl_user.entity.Customer;

@Mapper
public interface ActivityPrizeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActivityPrize record);

    int insertSelective(ActivityPrize record);

    ActivityPrize selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActivityPrize record);

    int updateByPrimaryKey(ActivityPrize record);
    
    List<ActivityPrize> selectByActivityPrize(ActivityPrize activityPrize);
    List<ActivityPrize> selectAllPrize();

	List<ActivityPrize> getMyFokas(int customerId);
	ActivityPrize getMyPeiQi(int customerId);
}