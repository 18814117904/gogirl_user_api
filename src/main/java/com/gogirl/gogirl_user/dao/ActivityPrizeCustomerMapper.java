package com.gogirl.gogirl_user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.gogirl.gogirl_user.entity.ActivityPrize;
import com.gogirl.gogirl_user.entity.ActivityPrizeCustomer;

@Mapper
public interface ActivityPrizeCustomerMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActivityPrizeCustomer record);

    int insertSelective(ActivityPrizeCustomer record);

    ActivityPrizeCustomer selectByPrimaryKey(Integer id);
    ActivityPrizeCustomer selectByActivityPrizeCustomer(ActivityPrizeCustomer activityPrizeCustomer);
    
    int updateByPrimaryKeySelective(ActivityPrizeCustomer record);

    int updateByPrimaryKey(ActivityPrizeCustomer record);

	List<ActivityPrize> getPrizeList(@Param(value = "nickname")String nickname, @Param(value = "phone")String phone, @Param(value = "pid")Integer pid, @Param(value = "prizeCode")Integer prizeCode);
	List<ActivityPrize> myPrizeList(Integer customerId);
	List<ActivityPrize> getNewPrizeList();
	List<ActivityPrizeCustomer> getAllFokas();

	void setFokaState3(@Param(value = "customerId")Integer customerId,@Param(value = "prizeId")Integer prizeId);
    int mergeCustomer(@Param("fromCustomerId")Integer fromCustomerId,@Param("toCustomerId")Integer toCustomerId);
}