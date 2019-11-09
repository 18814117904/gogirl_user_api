package com.gogirl.gogirl_user.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.gogirl.gogirl_user.dao.DiscountConfigMapper;
import com.gogirl.gogirl_user.entity.Coupon;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.DiscountConfig;
import com.gogirl.gogirl_user.service.discount.CouponDiscount;
import com.gogirl.gogirl_user.service.discount.Discount;

@Service
public class DiscountConfigService {
	@Resource
	DiscountConfigMapper discountConfigDao;
	@Resource
	CustomerBalanceService customerBalanceService;
	
    public int deleteByPrimaryKey(Integer id){
		return discountConfigDao.deleteByPrimaryKey(id);
    }
    public int insert(DiscountConfig record){
    	return discountConfigDao.insert(record);
    }
    public int insertSelective(DiscountConfig record){
    	return discountConfigDao.insertSelective(record);
    }
    public DiscountConfig selectByPrimaryKey(Integer id){
    	return discountConfigDao.selectByPrimaryKey(id);
    }
    public int updateByPrimaryKeySelective(DiscountConfig record){
    	return discountConfigDao.updateByPrimaryKeySelective(record);
    }
    public int updateByPrimaryKey(DiscountConfig record){
    	return discountConfigDao.updateByPrimaryKey(record);
    }
	public List<DiscountConfig> selectAllDiscount() {
		return discountConfigDao.selectAllDiscount();
	}
	public DiscountConfig selectByTotalCharge(int totalCharge) {
		return discountConfigDao.selectByTotalCharge(totalCharge);
	}
	public DiscountConfig selectByCharge(int amount) {
		return discountConfigDao.selectByCharge(amount);
	}
	//根据总充金额折扣，确定折扣
//	public int count(int customerId, int originalPrice) {
//		int discountedPrice=originalPrice;
//		//获取折扣信息
//		CustomerBalance customerBalance= customerBalanceService.getCustomerBalance(customerId);
//		int totalCharge = 0;
//		if(customerBalance!=null){
//			totalCharge = customerBalance.getTotalCharge();
//		}
//		DiscountConfig discountConfig = discountConfigDao.selectByTotalCharge(totalCharge);
//		//打折返回
//		if(discountConfig!=null){
//			Discount discout = new PercentageDiscount(discountConfig.getDiscount());
//			discountedPrice = discout.count(discountedPrice);
//		}
//		return discountedPrice;
//	}
	//根据最后一次充值金额确定折扣，计算折后价
	public Double count(CouponCustomerRelevance couponCustomerRelevance, Double originalPrice) {
		Discount discout = new CouponDiscount(couponCustomerRelevance);
		return discout.count(originalPrice);
//		int discountedPrice=originalPrice;
//		//获取折扣信息
//		CustomerBalance customerBalance= customerBalanceService.getCustomerBalance(customerId);
//		//打折返回
//		if(customerBalance.getCurrentDiscount()!=null&&customerBalance.getCurrentDiscount()!=0){
////			Discount discout = new PercentageDiscount(customerBalance.getCurrentDiscount());
//			
//			discountedPrice = discout.count(discountedPrice);
//		}
//		return discountedPrice;
	}
}
