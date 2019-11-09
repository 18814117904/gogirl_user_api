package com.gogirl.gogirl_user.service.discount;

import java.math.BigDecimal;

import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;


public class CouponDiscount implements Discount {
	CouponCustomerRelevance relevance;
	
	
	public CouponDiscount(CouponCustomerRelevance relevance){
		this.relevance = relevance;
	}
	
	@Override
	public Double count(Double originalPrice) {
		if(relevance!=null&&relevance.getCoupon()!=null&&relevance.getCoupon().getDiscountPercent()!=null){
			originalPrice= originalPrice*relevance.getCoupon().getDiscountPercent();
		}
		if(relevance!=null&&relevance.getCoupon()!=null&&relevance.getCoupon().getDiscountAmount()!=null){
			double discountAmount = relevance.getCoupon().getDiscountAmount();
			if(discountAmount>originalPrice){
				return 0.0;
			}else{
				return new BigDecimal(originalPrice).subtract(new BigDecimal(discountAmount)).doubleValue();
			}
		}
		return originalPrice;
	}
}
