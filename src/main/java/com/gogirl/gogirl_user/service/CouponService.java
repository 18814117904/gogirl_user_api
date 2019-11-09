package com.gogirl.gogirl_user.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.gogirl.gogirl_user.constant.GogirlProperties;
import com.gogirl.gogirl_user.dao.CouponCustomerRelevanceMapper;
import com.gogirl.gogirl_user.dao.CouponMapper;
import com.gogirl.gogirl_user.dao.DiscountConfigMapper;
import com.gogirl.gogirl_user.entity.Coupon;
import com.gogirl.gogirl_user.entity.CouponCustomerRelevance;
import com.gogirl.gogirl_user.entity.CustomerBalance;
import com.gogirl.gogirl_user.entity.DiscountConfig;
import com.gogirl.gogirl_user.entity.JsonResult;
import com.gogirl.gogirl_user.service.discount.Discount;

@Service
public class CouponService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	CouponMapper couponDao;
	@Resource
	CouponCustomerRelevanceMapper couponCustomerRelevanceDao;
	@Resource
	public RestTemplate restTemplate;
	@Resource
	GogirlProperties gogirlProperties;

	public List<Coupon> selectByCoupon(Coupon coupon){
		return couponDao.selectByCoupon(coupon);
	}
	public int updateByPrimaryKeySelective(Coupon record){
		return couponDao.updateByPrimaryKeySelective(record);
	}
	public int insertSelective(Coupon record){
		if(record.getState()==null){
			record.setState(1);
		}
		record.setUpdateTime(new Date());
		return couponDao.insertSelective(record);
	}
	public Coupon selectByPrimaryKey(Integer id){
		return  couponDao.selectByPrimaryKey(id);
	}
	public Coupon selectByCouponCustomerRelevance(Integer id){
		return  couponDao.selectByPrimaryKey(id);
	}
	public CouponCustomerRelevance selectRelevanceByPrimaryKey(Integer id){
		return  couponCustomerRelevanceDao.selectByPrimaryKey(id);
	}
    public List<CouponCustomerRelevance> selectByCouponCustomerRelevance(CouponCustomerRelevance record,String phone,String username){
    	return couponCustomerRelevanceDao.selectByCouponCustomerRelevance(record, phone, username);
    }
    public List<CouponCustomerRelevance> selectMyCoupon(Integer customerId){
    	return couponCustomerRelevanceDao.selectMyCoupon(customerId);
    }
    public List<Coupon> getAllUseCoupon(){
    	return couponDao.getAllUseCoupon();
    }
    
	public int updateRelevanceByPrimaryKeySelective(CouponCustomerRelevance record){
		//查询优惠券状态是否从未使用到已使用,若是,则加次数
		if(record!=null&&record.getState()!=null&&record.getState()==2){
			CouponCustomerRelevance ccr = couponCustomerRelevanceDao.selectByPrimaryKey(record.getId());
			if(ccr!=null&&ccr.getState()!=null&&ccr.getState()==1){
				Coupon c= couponDao.selectByPrimaryKey(record.getCouponId());
				if(c==null){
					return 0;
				}
				Coupon changeQuantity = new Coupon();
				changeQuantity.setId(record.getCouponId());
				changeQuantity.setUseQuantity(c.getUseQuantity()+1);
				couponDao.updateByPrimaryKeySelective(changeQuantity);
			}
		}
		
		return couponCustomerRelevanceDao.updateByPrimaryKeySelective(record);
	}
	public int countRelevanceNum(CouponCustomerRelevance record){
		return couponCustomerRelevanceDao.countRelevanceNum(record);
	}
	
	public int insertSelective(Coupon coupon,CouponCustomerRelevance record){
		if(record.getState()==null){
			record.setState(1);
		}
		if(record.getCode()==null){
			record.setCode(getRandomCode());
		}
		if(record.getReceiveTime()==null){
			record.setReceiveTime(new Date());
		}
		if((record.getValidStartTime()==null||record.getValidEndTime()==null)&&coupon!=null){
			if(coupon.getValidType()==1){
				record.setValidStartTime(coupon.getValidStartTime());//优惠券原有开始和结束时间
				record.setValidEndTime(coupon.getValidEndTime());
			}else if(coupon.getValidType()==2){
				long today = new Date().getTime();
				long day7 = today+new Long(86400000)*coupon.getValidDate();
				record.setValidStartTime(new Date(today));//从现在开始
				record.setValidEndTime(new Date(day7));//七天后过期
			}
		}
		
		
		//修改已领取人数
		Coupon c= couponDao.selectByPrimaryKey(record.getCouponId());
		Coupon changeQuantity = new Coupon();
		changeQuantity.setId(record.getCouponId());
		changeQuantity.setReceiveQuantity(c.getReceiveQuantity()+1);
		couponDao.updateByPrimaryKeySelective(changeQuantity);
		
		
		couponCustomerRelevanceDao.insertSelective(record);
		return record.getId();
	}
	public int insertSelectiveList(Coupon coupon,List<CouponCustomerRelevance> list,Integer amount){
		for(int i=0;i<list.size();i++){
			CouponCustomerRelevance record = list.get(i);
			if(record.getState()==null){
				record.setState(1);
			}
//			if(record.getCode()==null){//不需要code
//				record.setCode(getRandomCode());
//			}
			if(record.getReceiveTime()==null){
				record.setReceiveTime(new Date());
			}
			if((record.getValidStartTime()==null||record.getValidEndTime()==null)&&coupon!=null){
				if(coupon.getValidType()==1){
					record.setValidStartTime(coupon.getValidStartTime());//优惠券原有开始和结束时间
					record.setValidEndTime(coupon.getValidEndTime());
				}else if(coupon.getValidType()==2){
					long today = new Date().getTime();
					long day7 = today+new Long(86400000)*coupon.getValidDate();
					record.setValidStartTime(new Date(today));//从现在开始
					record.setValidEndTime(new Date(day7));//七天后过期
				}
			}
			list.set(i, record);
		}
		//修改已领取人数
		Coupon changeQuantity = new Coupon();
		changeQuantity.setId(coupon.getId());
		changeQuantity.setReceiveQuantity(coupon.getReceiveQuantity()+list.size()*amount);
		couponDao.updateByPrimaryKeySelective(changeQuantity);
		
		
		//批量插入到数据库
		int row = 0;
		for(int i=0;i<amount;i++){
			row+=couponCustomerRelevanceDao.insertSelectiveList(list);
		}
		return row;
	}
	public String getRandomCode(){
		String code = couponCustomerRelevanceDao.getRandomCode(Math.random());
		if(code!=null){
			couponCustomerRelevanceDao.deleteCode(code);
		}
		return code;
	}
    @Scheduled(cron="0 0 12 * * ?")//每天12点检查优惠券是否过期,消息模板提醒
//  @Scheduled(cron="0 * * * * ?")
  public void couponExpireRemind(){
  	logger.info("每天定时任务,查看优惠券是否即将过期,发起消息模板提醒");
  	CouponCustomerRelevance record = new CouponCustomerRelevance();
  	couponCustomerRelevanceDao.selectByCouponCustomerRelevance(record, null, null);
  	List<Map<String, String>> dataList = new ArrayList<>();
  	//TODO 处理数据
  	
	restTemplate.postForObject(gogirlProperties.getGOGIRLMP()+"gogirl_mp/wx/template/ticketExpiresMsg?time=5天", 
			 dataList, JsonResult.class );

    }
    @Scheduled(cron="0 0 3 * * ?")
//  @Scheduled(cron="0 * * * * ?")
    public void setCouponExpireJob(){
      	logger.info("每天定时任务,判断优惠券是否过期");
      	couponCustomerRelevanceDao.setCouponExpire(new Date());
      }
	public String getCouponIdFromConfig(){
		String code = couponDao.getCouponIdFromConfig();
		return code;
	}
    
}
